package de.powerstaff.business.service;

import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.service.impl.reader.DocumentReader;
import de.powerstaff.business.service.impl.reader.DocumentReaderFactory;
import de.powerstaff.business.service.impl.reader.ReadResult;
import java.io.StringReader;
import java.util.List;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

public class FreelancerFieldBridge implements FieldBridge {

    private void addField(Document aDocument, String aFieldname, String aStringValue, Field.Store aStoreValue, Field.Index aIndexValue) {
        if (!StringUtils.isEmpty(aStringValue)) {
            aDocument.add(new Field(aFieldname, aStringValue, aStoreValue, aIndexValue));
        }
    }

    @Override
    public void set(String aName, Object aValue, Document aDocument, LuceneOptions aOptions) {
        Freelancer theFreelancer = (Freelancer) aValue;

        List<FreelancerProfile> theProfiles = DomainHelper.getInstance().getProfileSearchService().loadProfilesFor(theFreelancer);

        DocumentReaderFactory theReaderFactory = DomainHelper.getInstance().getDocumentReaderFactory();
        theReaderFactory.initialize();

        StringBuilder theContent = new StringBuilder();
        addField(aDocument, ProfileIndexerService.NUM_PROFILES, "" + theProfiles.size(), Field.Store.YES, Field.Index.NOT_ANALYZED);
        addField(aDocument, ProfileIndexerService.UNIQUE_ID, "" + UUID.randomUUID().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);

        int count = 0;
        for (FreelancerProfile theProfile : theProfiles) {
            count++;
            addField(aDocument, ProfileIndexerService.PROFILE_PATH_PREFIX + count, theProfile.getFileOnserver().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED);
            addField(aDocument, ProfileIndexerService.PROFILE_MODIFICATION_PREFIX + count, "" + theProfile.getFileOnserver().lastModified(), Field.Store.YES, Field.Index.NOT_ANALYZED);
            DocumentReader theReader = theReaderFactory.getDocumentReaderForFile(theProfile.getFileOnserver());
            if (theReader != null) {
                try {
                    ReadResult theResult = theReader.getContent(theProfile.getFileOnserver());
                    String theResultString = theResult.getContent();

                    addField(aDocument, ProfileIndexerService.PROFILE_CHECKSUM_PREFIX + count, DigestUtils
                            .shaHex(theResultString),
                            Field.Store.YES, Field.Index.NOT_ANALYZED);

                    theContent.append(theResultString).append(" ");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        String theStringContent = theContent.toString();

        addField(aDocument, ProfileIndexerService.SHACHECKSUM, DigestUtils
                .shaHex(theStringContent),
                Field.Store.YES, Field.Index.NOT_ANALYZED);
        aDocument.add(new Field(ProfileIndexerService.CONTENT, new StringReader(theStringContent)));
    }
}