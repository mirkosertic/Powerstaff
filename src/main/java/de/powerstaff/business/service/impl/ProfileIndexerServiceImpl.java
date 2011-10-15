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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.NumericUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.dao.FreelancerDAO;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ProfileSearchService;
import de.powerstaff.business.service.ServiceLoggerService;
import de.powerstaff.business.service.impl.reader.DocumentReader;
import de.powerstaff.business.service.impl.reader.DocumentReaderFactory;
import de.powerstaff.business.service.impl.reader.ReadResult;

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

	private FreelancerDAO freelancerDAO;

	private ProfileSearchService profileSearchService;

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private static String toIndexFormat(Date aValue) {
		if (aValue == null) {
			return "";
		}
		return DateTools.dateToString(aValue, Resolution.DAY);
	}

	private static String toIndexFormat(Long aNumber) {
		if (aNumber == null) {
			return "";
		}
		return NumericUtils.longToPrefixCoded(aNumber.longValue());
	}

	private static String toIndexFormat(boolean aBoolean) {
		return aBoolean ? "true" : "false";
	}

	public void setFreelancerDAO(FreelancerDAO freelancerDAO) {
		this.freelancerDAO = freelancerDAO;
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

	private void processDeletedOrUpdatedFiles() {

		logger.logInfo("Processing deleted or updated files");

		logger.logInfo("Done with deleted or updated files");
	}

	/**
	 * Run the indexer.
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void runIndexer() {

		if (!systemParameterService.isIndexingEnabled()) {
			logger.logInfo("Indexing disabled");
			return;
		}

		readerFactory.initialize();

		serviceLogger.logStart(SERVICE_ID, "");

		// Jetzt läuft er

		long theStartTime = System.currentTimeMillis();

		logger.logInfo("Running indexing");

		try {

			if (systemParameterService.isDeletedDocumentRemovalEnabled()) {
				processDeletedOrUpdatedFiles();
			} else {
				logger.logInfo("Deleted document removal is disabled");
			}

		} catch (Exception ex) {

			logger.logError("Error on indexing", ex);

		} finally {

			theStartTime = System.currentTimeMillis() - theStartTime;

			logger.logInfo("Indexing finished");

			serviceLogger.logEnd(SERVICE_ID, "Dauer = " + theStartTime + "ms");
		}
	}

	public void setServiceLogger(ServiceLoggerService serviceLogger) {
		this.serviceLogger = serviceLogger;
	}

    @Transactional
	public void rebuildIndex() {
        Session theSession = sessionFactory.getCurrentSession();
        FullTextSession theFt = Search.getFullTextSession(theSession);
        try {
            theFt.purgeAll(Freelancer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            theFt.createIndexer(Freelancer.class).startAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}