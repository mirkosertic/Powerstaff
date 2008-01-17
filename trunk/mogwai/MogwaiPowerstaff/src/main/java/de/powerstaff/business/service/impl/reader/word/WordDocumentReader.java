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
package de.powerstaff.business.service.impl.reader.word;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import de.powerstaff.business.service.impl.reader.DocumentReader;
import de.powerstaff.business.service.impl.reader.ReadResult;

/**
 * DocumentReader for microsoft word documents.
 * 
 * @author sertic
 */
public class WordDocumentReader implements DocumentReader {

    public ReadResult getContent(File inputFile) throws Exception {

        File tempFile = File.createTempFile("EXTRACT_", ",txt");
        String command = "cmd /c c:\\antiword\\antiword  \"" + inputFile + "\" > \"" + tempFile + "\"";

        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        BufferedReader br = new BufferedReader(new FileReader(tempFile));
        while (br.ready()) {

            String line = br.readLine();
            if (line != null) {
                line = line.trim();
            }
            if (line != null) {

                pw.print(line.replace('|', ' ') + " ");
            }

        }
        br.close();
        pw.flush();
        pw.close();

        tempFile.delete();

        try {
            process.destroy();
        } catch (Exception e) {
            // Nothing will happen here            
        }

        return new ReadResult(sw.toString());
    }

}
