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
import java.util.HashMap;
import java.util.Map;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.impl.reader.msword.DOCWordDocumentReader;
import de.powerstaff.business.service.impl.reader.msword.DOCXWordDocumentReader;
import de.powerstaff.business.service.impl.reader.pdf.PDFDocumentReader;
import de.powerstaff.business.service.impl.reader.txt.TextDocumentReader;

/**
 * Factory for creating document readers.
 * 
 * @author sertic
 */
public class DocumentReaderFactory extends LogableService {

	private PowerstaffSystemParameterService systemParameterService;

	private Map<String, DocumentReader> availableReaders = new HashMap<String, DocumentReader>();

	public void setSystemParameterService(
			PowerstaffSystemParameterService aService) {
		this.systemParameterService = aService;
	}

	public void initialize() {
		synchronized (availableReaders) {
			availableReaders.clear();
			if (systemParameterService.isDOCFormatEnabled()) {
				availableReaders.put(".DOC", new DOCWordDocumentReader());
			}
			if (systemParameterService.isDOCXFormatEnabled()) {
				availableReaders.put(".DOCX", new DOCXWordDocumentReader());
			}
			if (systemParameterService.isTXTFormatEnabled()) {
				availableReaders.put(".TXT", new TextDocumentReader());
			}
			if (systemParameterService.isPDFFormatEnabled()) {
				availableReaders.put(".PDF", new PDFDocumentReader());
			}

			for (Map.Entry<String, DocumentReader> theEntry : availableReaders
					.entrySet()) {
				logger.logInfo("Supporting file format " + theEntry.getKey());
			}
		}
	}

	/**
	 * Get the document reader instance for a document.
	 * 
	 * @param fileName
	 *            the source file
	 * @return the reader that can read the source file or null if none is
	 *         available
	 */
	public DocumentReader getDocumentReaderForFile(File fileName) {
		String theFilename = fileName.getName().toUpperCase();
		synchronized (availableReaders) {
			for (Map.Entry<String, DocumentReader> theEntry : availableReaders
					.entrySet()) {
				if (theFilename.endsWith(theEntry.getKey())) {
					return theEntry.getValue();
				}
			}
		}
		return null;
	}
}