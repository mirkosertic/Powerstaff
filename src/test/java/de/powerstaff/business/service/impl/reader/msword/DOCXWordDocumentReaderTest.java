package de.powerstaff.business.service.impl.reader.msword;

import de.powerstaff.business.service.impl.reader.ReadResult;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.File;

public class DOCXWordDocumentReaderTest extends TestCase {

    public void testLoadFile() throws Exception {
        final File file = new File(getClass().getResource("/profil 001.docx").toURI());
        final ReadResult result= new DOCXWordDocumentReader().getContent(file);
        Assert.assertEquals("Lala super test", result.getContent().trim());
    }
}