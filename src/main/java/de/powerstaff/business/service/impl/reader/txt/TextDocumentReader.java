package de.powerstaff.business.service.impl.reader.txt;

import java.io.File;
import java.io.FileReader;

import de.powerstaff.business.service.impl.reader.AbstractDocumentReader;
import de.powerstaff.business.service.impl.reader.ReadResult;

/**
 * Reader f�r Text Dokumente.
 * 
 * @author msertic
 */
public class TextDocumentReader extends AbstractDocumentReader {

    public ReadResult getContent(File aAnputFile) throws Exception {
        return new ReadResult(toFlatString(new FileReader(aAnputFile)));
    }

}
