package de.powerstaff.business.service.impl;

import de.powerstaff.business.service.ProfileIndexerService;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class IndexExtractor {

    public static void main(String[] args) throws IOException {
        File theProfiles = new File("D:\\Daten\\Arbeit\\Projekte\\Synekt\\Profile");
        File theFile = new File("D:\\Temp\\hibernatesearch\\de.powerstaff.business.entity.Freelancer");
        IndexReader theReader = IndexReader.open(FSDirectory.open(theFile));
        for (int i = 0; i < theReader.numDocs(); i++) {
            if (i % 100 == 0) {
                System.out.println("Reading " + i);
            }
            Document theDocument = theReader.document(i);
            String theCode = theDocument.get(ProfileIndexerService.CODE);
            String theOriginalContent = theDocument.get(ProfileIndexerService.ORIG_CONTENT);

            File theSingleProfile = new File(theProfiles, theCode+".txt");
            PrintWriter theWriter = new PrintWriter(new FileWriter(theSingleProfile));
            theWriter.write(theOriginalContent);
            theWriter.close();
        }
    }
}
