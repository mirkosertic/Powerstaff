package de.powerstaff.business.service.impl.reader.txt;

import de.powerstaff.business.service.impl.reader.ReadResult;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.File;

public class TextDocumentReaderTest extends TestCase {

    public void testLoadFile() throws Exception {
        final File file = new File(getClass().getResource("/profil 001.txt").toURI());
        final ReadResult result= new TextDocumentReader().getContent(file);
        Assert.assertEquals("Test TestA", result.getContent().trim());
    }
}