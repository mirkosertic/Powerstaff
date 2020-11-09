package de.powerstaff.business.service.impl.reader.pdf;

import de.powerstaff.business.service.impl.reader.AbstractDocumentReader;
import de.powerstaff.business.service.impl.reader.ReadResult;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

/**
 * Reader fär Text Dokumente.
 *
 * @author msertic
 */
public class PDFDocumentReader extends AbstractDocumentReader {

    public ReadResult getContent(final File aAnputFile) throws Exception {

        try (PDDocument theDocument = PDDocument.load(aAnputFile)) {
            final PDFTextStripper theStripper = new PDFTextStripper();
            final String theText = theStripper.getText(theDocument);
            return new ReadResult(theText);
        }
    }
}
