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
package de.mogwai.common.web.utils;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.utils.MimeTypeGuesser;

/**
 * Utility - Klasse für den Umgang mit Downloads.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:09:16 $
 */
public final class DownloadUtils {

    private static DownloadUtils me;

    private static final Logger LOGGER = new Logger(DownloadUtils.class);

    /**
     * Ermittlung der Singleton - Instanz der DownloadUtils.
     * 
     * @return die Singleton - Instanz
     */
    public static DownloadUtils getInstance() {

        if (me == null) {
            me = new DownloadUtils();
        }

        return me;
    }

    /**
     * Senden einer Datei an den Browser.
     * 
     * @param aContext
     *                der aktuelle FacesContext
     * @param aFileName
     *                der Name der Datei ( Wird benutzt, um den ContentType zu
     *                ermitteln )
     * @param aContent
     *                der Inhalt
     * @param aAttachment
     *                true, wenn die Datei als Attachment verschickt werden
     *                soll, sonst false
     */
    public void sentFileToBrowser(FacesContext aContext, String aFileName, String aContent, boolean aAttachment) {

        try {
            HttpServletResponse theResponse = (HttpServletResponse) aContext.getExternalContext().getResponse();
            theResponse.setCharacterEncoding("UTF-8");

            MimeTypeGuesser theMimeTypeGuesser = MimeTypeGuesser.getInstance();
            String theContentType = theMimeTypeGuesser.guessMimeTypeFromFileName(aFileName);

            LOGGER.logDebug("Content type is " + theContentType);

            String theContentDisposition = aAttachment ? "attachment" : "inline";

            theResponse.setContentType(theContentType);
            theResponse.setHeader("Content-disposition", theContentDisposition + "; filename=\"" + aFileName + "\"");

            // Wichtig für den Download von Dateien über SSL
            theResponse.setHeader("Pragma", "private");

            PrintWriter theWriter = new PrintWriter(new OutputStreamWriter(theResponse.getOutputStream(), "UTF-8"));
            theWriter.print(aContent);
            theWriter.flush();

            theWriter.close();

            aContext.responseComplete();

        } catch (Exception e) {
            throw new FacesException("Fehler beim Senden der Datei ", e);
        }
    }

    /**
     * Senden einer Datei an den Browser.
     * 
     * @param aContext
     *                der aktuelle FacesContext
     * @param aFileName
     *                der Name der Datei ( Wird benutzt, um den ContentType zu
     *                ermitteln )
     * @param aContent
     *                der Inhalt
     * @param aAttachment
     *                true, wenn die Datei als Attachment verschickt werden
     *                soll, sonst false
     */
    public void sentFileToBrowser(FacesContext aContext, String aFileName, byte[] aContent, boolean aAttachment) {

        try {
            HttpServletResponse theResponse = (HttpServletResponse) aContext.getExternalContext().getResponse();

            MimeTypeGuesser theMimeTypeGuesser = MimeTypeGuesser.getInstance();
            String theContentType = theMimeTypeGuesser.guessMimeTypeFromFileName(aFileName);

            LOGGER.logDebug("Content type is " + theContentType);

            String theContentDisposition = aAttachment ? "attachment" : "inline";
            int contentLength = aContent.length;

            LOGGER.logDebug("Content length is " + contentLength);

            theResponse.setContentType(theContentType);
            theResponse.setContentLength(contentLength);
            theResponse.setHeader("Content-disposition", theContentDisposition + "; filename=\"" + aFileName + "\"");

            // Wichtig für den Download von Dateien über SSL
            theResponse.setHeader("Pragma", "private");

            OutputStream theOutput = theResponse.getOutputStream();
            theOutput.write(aContent);
            theOutput.close();

            aContext.responseComplete();

        } catch (Exception e) {
            throw new FacesException("Fehler beim Senden der Datei ", e);
        }
    }
}