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

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

public class NullJspWriter extends JspWriter {

    public NullJspWriter() {
        super(1024, false);
    }

    @Override
    public void clear() throws IOException {
    }

    @Override
    public void clearBuffer() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public int getRemaining() {
        return 0;
    }

    @Override
    public void newLine() throws IOException {
    }

    @Override
    public void print(boolean arg0) throws IOException {
    }

    @Override
    public void print(char arg0) throws IOException {
    }

    @Override
    public void print(int arg0) throws IOException {
    }

    @Override
    public void print(long arg0) throws IOException {
    }

    @Override
    public void print(float arg0) throws IOException {
    }

    @Override
    public void print(double arg0) throws IOException {
    }

    @Override
    public void print(char[] arg0) throws IOException {
    }

    @Override
    public void print(String arg0) throws IOException {
    }

    @Override
    public void print(Object arg0) throws IOException {
    }

    @Override
    public void println() throws IOException {
    }

    @Override
    public void println(boolean arg0) throws IOException {
    }

    @Override
    public void println(char arg0) throws IOException {
    }

    @Override
    public void println(int arg0) throws IOException {
    }

    @Override
    public void println(long arg0) throws IOException {
    }

    @Override
    public void println(float arg0) throws IOException {
    }

    @Override
    public void println(double arg0) throws IOException {
    }

    @Override
    public void println(char[] arg0) throws IOException {
    }

    @Override
    public void println(String arg0) throws IOException {
    }

    @Override
    public void println(Object arg0) throws IOException {
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
    }
}