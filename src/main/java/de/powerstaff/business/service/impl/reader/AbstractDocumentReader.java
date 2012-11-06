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

import java.io.IOException;
import java.util.StringTokenizer;

public abstract class AbstractDocumentReader implements DocumentReader {

    protected String toFlatString(String aString) throws IOException {
        StringBuilder theBuilder = new StringBuilder();
        StringTokenizer theST = new StringTokenizer(aString, "\n\r");
        while (theST.hasMoreTokens()) {
            String theLine = theST.nextToken();
            if (theLine != null) {
                theLine = theLine.trim();
                if (theLine != null && theLine.length() > 0) {
                    theBuilder.append(theLine);
                    theBuilder.append(" ");
                }
            }
        }
        return theBuilder.toString();
    }
}
