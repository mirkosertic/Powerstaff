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

/**
 * Interface to a document reader.
 * 
 * A document reader can read the text content from a file and give a Reader
 * object to read the content.
 * 
 * @author sertic
 */
public interface DocumentReader {

    /**
     * Get the content reader for a given file.
     * 
     * @param inputFile
     *            the input file
     * @return the file content.
     * 
     * @throws Exception
     */
    ReadResult getContent(File inputFile) throws Exception;

}
