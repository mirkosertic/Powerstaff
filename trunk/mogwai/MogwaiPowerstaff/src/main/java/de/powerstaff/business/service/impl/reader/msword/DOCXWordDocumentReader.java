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

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import de.powerstaff.business.service.impl.reader.AbstractDocumentReader;
import de.powerstaff.business.service.impl.reader.ReadResult;

/**
 * DocumentReader for microsoft word documents.
 * 
 * @author sertic
 */
public class DOCXWordDocumentReader extends AbstractDocumentReader {

    public ReadResult getContent(File inputFile) throws Exception {

    	XWPFDocument theDocument = new XWPFDocument(new FileInputStream(inputFile));
    	XWPFWordExtractor theExtractor = new XWPFWordExtractor(theDocument);
    	String theText = theExtractor.getText();
    	theText = theText.replace('|',' ');
    	return new ReadResult(toFlatString(theText));
    }
}