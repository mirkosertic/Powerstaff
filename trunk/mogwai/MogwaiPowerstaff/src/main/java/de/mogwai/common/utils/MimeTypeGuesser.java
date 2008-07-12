/**
 * Copyright 2002 - 2007 the Mogwai Project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.mogwai.common.utils;

import javax.activation.MimetypesFileTypeMap;

/**
 * Hilfs - Klasse zum Umgang mit Mime - Typen.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:10:10 $
 */
public final class MimeTypeGuesser {

    private static MimeTypeGuesser me;

    private MimetypesFileTypeMap mimetypesMap = new MimetypesFileTypeMap();

    private MimeTypeGuesser() {
    }

    /**
     * Ermittlung der Instanz der Klasse.
     * 
     * @return die Singleton - Instanz
     */
    public static MimeTypeGuesser getInstance() {

        if (me == null) {
            me = new MimeTypeGuesser();
        }

        return me;
    }

    /**
     * Ermittlung des Mime - Types.
     * 
     * @param aFileName
     *                der Dateiname
     * @return der Mime - Type. Wenn nichts gefunden wurde, wird eine
     *         RuntimeException geworfen
     */
    public String guessMimeTypeFromFileName(String aFileName) {
        String theType = mimetypesMap.getContentType(aFileName);
        if (theType == null) {
            throw new RuntimeException("Unknown Mime - Type for " + aFileName
                    + ". Please configure META-INF/mime.types correctly!");
        }

        return theType;
    }
}
