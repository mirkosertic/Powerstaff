package de.powerstaff.business.service.impl.reader.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import de.powerstaff.business.service.impl.reader.DocumentReader;
import de.powerstaff.business.service.impl.reader.ReadResult;

/**
 * Reader für Text Dokumente.
 * 
 * @author msertic
 */
public class TextDocumentReader implements DocumentReader {

    public ReadResult getContent(File aAnputFile) throws Exception {
        StringBuilder theBuilder = new StringBuilder();
        BufferedReader theReader = new BufferedReader(new FileReader(aAnputFile));
        while (theReader.ready()) {
            String theLine = theReader.readLine();
            if (theLine != null) {
                theBuilder.append(theLine);
                theBuilder.append(" ");
            }
        }
        theReader.close();
        return new ReadResult(theBuilder.toString());
    }

}
