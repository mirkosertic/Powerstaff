package de.powerstaff.business.service.impl.reader.pdf;

import java.io.File;
import java.io.StringReader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import de.powerstaff.business.service.impl.reader.AbstractDocumentReader;
import de.powerstaff.business.service.impl.reader.ReadResult;

/**
 * Reader für Text Dokumente.
 * 
 * @author msertic
 */
public class PDFDocumentReader extends AbstractDocumentReader {

    public ReadResult getContent(File aAnputFile) throws Exception {
    	
    	PDDocument theDocument = PDDocument.load(aAnputFile);
    	PDFTextStripper theStripper = new PDFTextStripper();
        return new ReadResult(toFlatString(new StringReader(theStripper.getText(theDocument))));
    }

}
