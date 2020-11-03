/*
  Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.

  This library is free software; you can redistribute it and/or modify it under
  the terms of the GNU Lesser General Public License as published by the Free
  Software Foundation; either version 2.1 of the License, or (at your option)
  any later version.

  This library is distributed in the hope that it will be useful, but WITHOUT
  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
  details.

  You should have received a copy of the GNU Lesser General Public License
  along with this library; if not, write to the Free Software Foundation, Inc.,
  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.business.service;

import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.entity.FreelancerToTag;
import de.powerstaff.business.entity.Tag;
import de.powerstaff.business.service.impl.reader.DocumentReader;
import de.powerstaff.business.service.impl.reader.DocumentReaderFactory;
import de.powerstaff.business.service.impl.reader.ReadResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.List;

public class FreelancerFieldBridge implements FieldBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreelancerFieldBridge.class);

    private void addField(final Document aDocument, final String aFieldname, final String aStringValue, final Field.Store aStoreValue, final Field.Index aIndexValue) {
        if (!StringUtils.isEmpty(aStringValue)) {
            aDocument.add(new Field(aFieldname, aStringValue, aStoreValue, aIndexValue));
        }
    }

    @Override
    public void set(final String aName, final Object aValue, final Document aDocument, final LuceneOptions aOptions) {
        final Freelancer theFreelancer = (Freelancer) aValue;

        final List<FreelancerProfile> theProfiles = DomainHelper.getInstance().getProfileSearchService().loadProfilesFor(theFreelancer);

        final DocumentReaderFactory theReaderFactory = DomainHelper.getInstance().getDocumentReaderFactory();
        theReaderFactory.initialize();

        final StringBuilder theContent = new StringBuilder();
        theContent.append(theFreelancer.getSkills()).append(" ");
        addField(aDocument, ProfileIndexerService.NUM_PROFILES, "" + theProfiles.size(), Field.Store.YES, Field.Index.NOT_ANALYZED);

        int count = 0;
        for (final FreelancerProfile theProfile : theProfiles) {
            count++;
            addField(aDocument, ProfileIndexerService.PROFILE_PATH_PREFIX + count, theProfile.getFileOnserver().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
            final long theLastModified = theProfile.getFileOnserver().lastModified() / 1000;
            addField(aDocument, ProfileIndexerService.PROFILE_MODIFICATION_PREFIX + count, "" + theLastModified, Field.Store.YES, Field.Index.NOT_ANALYZED);
            final DocumentReader theReader = theReaderFactory.getDocumentReaderForFile(theProfile.getFileOnserver());
            if (theReader != null) {
                try {
                    final ReadResult theResult = theReader.getContent(theProfile.getFileOnserver());
                    final String theResultString = theResult.getContent();

                    addField(aDocument, ProfileIndexerService.PROFILE_CHECKSUM_PREFIX + count, DigestUtils
                            .shaHex(theResultString),
                            Field.Store.YES, Field.Index.NOT_ANALYZED);

                    theContent.append(theResultString).append(" ");
                } catch (final Exception e) {
                    LOGGER.warn("Fehler beim Lesen des Profils für Freiberufler " + theFreelancer.getId() + " : " + theFreelancer.getName1(), e);
                }
            }
        }

        final String theStringContent = theContent.toString();

        aDocument.add(new Field(ProfileIndexerService.CONTENT, new StringReader(theStringContent)));
        aDocument.add(new Field(ProfileIndexerService.ORIG_CONTENT, theStringContent, Field.Store.YES, Field.Index.NOT_ANALYZED));

        for (final FreelancerToTag toTag : theFreelancer.getTags()) {
            final Tag tag = toTag.getTag();
            aDocument.add(new Field(ProfileIndexerService.TAGS, Long.toString(tag.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        }
    }
}