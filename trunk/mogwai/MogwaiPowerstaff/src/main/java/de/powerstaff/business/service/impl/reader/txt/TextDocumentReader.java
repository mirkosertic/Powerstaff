package de.powerstaff.business.service.impl.reader.txt;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.IOUtils;

import de.powerstaff.business.service.impl.reader.AbstractDocumentReader;
import de.powerstaff.business.service.impl.reader.ReadResult;

/**
 * Reader für Text Dokumente.
 * 
 * @author msertic
 */
public class TextDocumentReader extends AbstractDocumentReader {

    public ReadResult getContent(File aAnputFile) throws Exception {
        return new ReadResult(toFlatString(IOUtils.toString(new FileReader(aAnputFile))));
    }

}
