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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspWriter;

/**
 * ServletResponseWrapper used by the JSP 'include' action.
 * 
 * This wrapper response object is passed to RequestDispatcher.include(), so
 * that the output of the included resource is appended to that of the including
 * page.
 * 
 * @author Pierre Delisle
 */

public class ServletResponseWrapperInclude extends HttpServletResponseWrapper {

    private PrintWriter printWriter;

    private ByteArrayOutputStream byteStream;

    private ServletOutputStream servletStream;

    private JspWriter jspWriter;

    private boolean useWriter = false;

    private boolean useStream = false;

    public ServletResponseWrapperInclude(ServletResponse aResponse, JspWriter aJspWriter) {
        super((HttpServletResponse) aResponse);
        this.printWriter = new PrintWriter(aJspWriter);
        this.jspWriter = aJspWriter;
    }

    /**
     * Returns a wrapper around the JspWriter of the including page.
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        if (useStream) {
            throw new IllegalStateException();
        }
        useWriter = true;
        return printWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (useWriter) {
            throw new IllegalStateException();
        }
        if (!useStream) {
            byteStream = new ByteArrayOutputStream(getBufferSize());
            servletStream = new ServletOutputStream() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.io.OutputStream#flush()
                 */
                @Override
                public void flush() throws IOException {
                    // TODO Auto-generated method stub
                    super.flush();
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.io.OutputStream#write(byte[], int, int)
                 */
                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    // TODO Auto-generated method stub
                    byteStream.write(b, off, len);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.io.OutputStream#write(byte[])
                 */
                @Override
                public void write(byte[] b) throws IOException {
                    // TODO Auto-generated method stub
                    byteStream.write(b);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.io.OutputStream#write(int)
                 */
                @Override
                public void write(int b) throws IOException {
                    byteStream.write(b);
                }

            };
            useStream = true;
        }
        return servletStream;
    }

    /**
     * Clears the output buffer of the JspWriter associated with the including
     * page.
     */
    @Override
    public void resetBuffer() {
        try {
            jspWriter.clearBuffer();
            useWriter = false;
            useStream = false;
            byteStream = null;
        } catch (IOException ioe) {
            // ignore this
        }
    }

    @Override
    public void reset() {
        resetBuffer();
    }

    @Override
    public void flushBuffer() throws IOException {
        if (useStream) {
            // TODO - detect encoding ?
            jspWriter.write(byteStream.toString(getCharacterEncoding()));
            byteStream = new ByteArrayOutputStream(getBufferSize());
        }
    }

    // Override ignored methods.
    @Override
    public void setBufferSize(int aValue) {
    }

    @Override
    public void setContentLength(int aValue) {
    }

    @Override
    public void setContentType(String aValue) {
    }

    @Override
    public void setStatus(int aValue) {
    }

    @Override
    public void setLocale(Locale aValue) {
    }

    @Override
    public void sendRedirect(String aValue) throws IOException {
        throw new IllegalStateException();
    }
}
