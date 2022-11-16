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
package de.powerstaff.business.service.impl.reader.msword;

import de.powerstaff.business.service.impl.reader.AbstractDocumentReader;
import de.powerstaff.business.service.impl.reader.ReadResult;
import org.apache.poi.hwpf.OldWordFileFormatException;
import org.apache.poi.hwpf.extractor.Word6Extractor;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;

/**
 * DocumentReader for microsoft word documents.
 *
 * @author sertic
 */
public class DOCWordDocumentReader extends AbstractDocumentReader {

    public ReadResult getContent(final File inputFile) throws Exception {

        try {
            final WordExtractor theExtractor = new WordExtractor(new FileInputStream(inputFile));
            String theText = theExtractor.getText();
            theText = theText.replace('|', ' ');
            return new ReadResult(toFlatString(theText));
        } catch (final OldWordFileFormatException e) {
            final Word6Extractor theExtractor = new Word6Extractor(new FileInputStream(inputFile));
            String theText = theExtractor.getText();
            theText = theText.replace('|', ' ');
            return new ReadResult(toFlatString(theText));
        }
    }
}