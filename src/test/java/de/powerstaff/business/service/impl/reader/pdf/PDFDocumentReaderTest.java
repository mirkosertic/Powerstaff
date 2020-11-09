package de.powerstaff.business.service.impl.reader.pdf;

import de.powerstaff.business.service.impl.reader.ReadResult;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.File;

public class PDFDocumentReaderTest extends TestCase {

    public void testLoadFile() throws Exception {
        final File file = new File(getClass().getResource("/profil 001.pdf").toURI());
        final ReadResult result= new PDFDocumentReader().getContent(file);
        Assert.assertEquals("Lala super test", result.getContent().trim());
    }
}