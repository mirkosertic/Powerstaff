package de.powerstaff.business.service.impl.reader.msword;

import de.powerstaff.business.service.impl.reader.ReadResult;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.File;

public class DOCWordDocumentReaderTest extends TestCase {

    public void testLoadFile() throws Exception {
        final File file = new File(getClass().getResource("/profil 001.doc").toURI());
        final ReadResult result= new DOCWordDocumentReader().getContent(file);
        Assert.assertEquals("Lala super test", result.getContent().trim());
    }
}