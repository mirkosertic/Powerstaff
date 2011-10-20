/**
 * Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.business.service.impl;

import de.mogwai.common.business.service.impl.LogableService;
import de.mogwai.common.logging.Logger;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.business.service.ServiceLoggerService;
import de.powerstaff.business.service.impl.reader.DocumentReaderFactory;
import java.io.File;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.hibernate.*;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.Search;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Mirko Sertic
 */
@Transactional
public class ProfileIndexerServiceImpl extends LogableService implements
        ProfileIndexerService {

    private static final String SERVICE_ID = "ProfileIndexer";

    private DocumentReaderFactory readerFactory;

    private ServiceLoggerService serviceLogger;

    private PowerstaffSystemParameterService systemParameterService;

    private ProfileSearchService profileSearchService;

    private SessionFactory sessionFactory;

    private boolean running;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setReaderFactory(DocumentReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    public void setSystemParameterService(
            PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public void setProfileSearchService(
            ProfileSearchService profileSearchService) {
        this.profileSearchService = profileSearchService;
    }

    /**
     * Run the indexer.
     */
    @Transactional
    public void runIndexer() {

        if (!systemParameterService.isIndexingEnabled()) {
            logger.logInfo("Indexing disabled");
            return;
        }

        if (running) {
            logger.logInfo("Indexing already running");
        }

        running = true;

        logger.logInfo("Running indexing");

        readerFactory.initialize();

        serviceLogger.logStart(SERVICE_ID, "");

        // Jetzt läuft er

        long theStartTime = System.currentTimeMillis();

        try {

            int theFetchSize = 100;
            int theLogCount = theFetchSize * 10;

            Session theHibernateSession = sessionFactory.getCurrentSession();
            FullTextSession theFT = Search.getFullTextSession(theHibernateSession);

            org.hibernate.Query theQuery = theHibernateSession.createQuery("from Freelancer");
            theQuery.setFetchSize(theFetchSize);
            ScrollableResults theResults = theQuery.scroll(ScrollMode.FORWARD_ONLY);
            int counter = 0;
            while (theResults.next()) {
                Freelancer theFreelancer = (Freelancer) theResults.get(0);

                boolean needsToUpdate = true;

                TermQuery theTermQuery = new TermQuery(new Term("id", "" + theFreelancer.getId()));
                FullTextQuery theHibernateQuery = theFT.createFullTextQuery(theTermQuery, Freelancer.class);
                theHibernateQuery.setProjection(FullTextQuery.DOCUMENT);

                for (Object theSingleEntity : theHibernateQuery.list()) {

                    needsToUpdate = false;
                    Object[] theRow = (Object[]) theSingleEntity;
                    Document theDocument = (Document) theRow[0];

                    long theNumberOfProfiles = Long.parseLong(theDocument.get(ProfileIndexerService.NUM_PROFILES));
                    List<FreelancerProfile> theProfiles = profileSearchService.loadProfilesFor(theFreelancer);
                    if (theNumberOfProfiles != theProfiles.size()) {
                        logger.logInfo("Updating freelancer " + theFreelancer.getId() + " as the number of profiles changed from "+ theNumberOfProfiles+" to "+theProfiles.size());
                        needsToUpdate = true;
                    } else {
                        for (int i = 1; i <= theNumberOfProfiles; i++) {
                            String theFileName = theDocument.get(ProfileIndexerService.PROFILE_PATH_PREFIX + i);
                            File theFileOnServer = new File(theFileName);
                            if (theFileOnServer.exists()) {
                                long theModification = Long.parseLong(theDocument.get(ProfileIndexerService.PROFILE_MODIFICATION_PREFIX + i));
                                long theLastModified = theFileOnServer.lastModified() / 1000;
                                if (theModification != theLastModified) {
                                    logger.logInfo("Updating freelancer " + theFreelancer.getId() + " as profile " + theFileOnServer + " was modified");
                                    needsToUpdate = true;
                                }
                            } else {
                                logger.logInfo("Updating freelancer " + theFreelancer.getId() + " as profile " + theFileOnServer + " seems to be deleted");
                                needsToUpdate = true;
                            }
                        }
                    }

                }

                if (needsToUpdate) {
                    theFT.index(theFreelancer);
                }

                if (counter % theLogCount == 0) {
                    logger.logInfo("Processing record " + counter);
                }

                if (counter % theFetchSize == 0) {

                    logger.logDebug("Flushing session and index");
                    theFT.flushToIndexes();
                    theFT.clear();
                    theHibernateSession.clear();
                }
                counter++;
            }

        } catch (Exception ex) {

            logger.logError("Error on indexing", ex);

        } finally {

            theStartTime = System.currentTimeMillis() - theStartTime;

            logger.logInfo("Indexing finished");

            serviceLogger.logEnd(SERVICE_ID, "Dauer = " + theStartTime + "ms");

            running = false;
        }
    }

    public void setServiceLogger(ServiceLoggerService serviceLogger) {
        this.serviceLogger = serviceLogger;
    }

    @Transactional
    public void rebuildIndex() {

        logger.logInfo("Rebuilding index");

        Session theSession = sessionFactory.getCurrentSession();
        FullTextSession theFt = Search.getFullTextSession(theSession);

        MassIndexer theIndexer = theFt.createIndexer(Freelancer.class);
        theIndexer.threadsToLoadObjects(1);
        try {
            theIndexer.purgeAllOnStart(true);
            theIndexer.startAndWait();
        } catch (Exception e) {
            logger.logError("Error creating index", e);
        }
        theFt.flushToIndexes();
        theFt.flush();
        theSession.flush();

        logger.logInfo("Rebuilding index finished");
    }
}