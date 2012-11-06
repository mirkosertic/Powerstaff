package de.powerstaff.business.service.impl.reader.pdf;

import de.powerstaff.business.service.impl.reader.AbstractDocumentReader;
import de.powerstaff.business.service.impl.reader.ReadResult;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.File;

/**
 * Reader f�r Text Dokumente.
 *
 * @author msertic
 */
public class PDFDocumentReader extends AbstractDocumentReader {

    public ReadResult getContent(File aAnputFile) throws Exception {

        PDDocument theDocument = null;
        try {
            theDocument = PDDocument.load(aAnputFile);
            PDFTextStripper theStripper = new PDFTextStripper();
            String theText = theStripper.getText(theDocument);
            return new ReadResult(theText);
        } finally {
            if (theDocument != null) {
                theDocument.close();
            }
        }
    }

}
