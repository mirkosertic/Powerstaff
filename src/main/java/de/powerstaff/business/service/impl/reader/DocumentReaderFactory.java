/**
 * Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.business.service.impl.reader;

import java.io.File;

import de.powerstaff.business.service.impl.reader.msword.DOCWordDocumentReader;
import de.powerstaff.business.service.impl.reader.msword.DOCXWordDocumentReader;
import de.powerstaff.business.service.impl.reader.pdf.PDFDocumentReader;
import de.powerstaff.business.service.impl.reader.txt.TextDocumentReader;

/**
 * Factory for creating document readers.
 * 
 * @author sertic
 */
public final class DocumentReaderFactory {

    private static DocumentReaderFactory me;

    /**
     * This is a singleton.
     */
    private DocumentReaderFactory() {
    }

    public static DocumentReaderFactory getInstance() {

        // Is the singleton already created ?
        if (me == null) {

            // No, create the single and unique instance !
            me = new DocumentReaderFactory();

        }

        return me;
    }

    /**
     * Get the document reader instance for a document.
     * 
     * @param fileName
     *                the source file
     * @return the reader that can read the source file or null if none is
     *         available
     */
    public DocumentReader getDocumentReaderForFile(File fileName) {
        if (fileName.getName().toUpperCase().endsWith(".DOC")) {
            return new DOCWordDocumentReader();
        }
        if (fileName.getName().toUpperCase().endsWith(".DOCX")) {
            return new DOCXWordDocumentReader();
        }
        if (fileName.getName().toUpperCase().endsWith(".TXT")) {
            return new TextDocumentReader();
        }
        if (fileName.getName().toUpperCase().endsWith(".PDF")) {
            return new PDFDocumentReader();
        }
        return null;
    }
}