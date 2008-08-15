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
package de.mogwai.common.web.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamClass;

import org.apache.myfaces.shared_impl.util.ClassUtils;
import org.jboss.serial.io.JBossObjectInputStream;

public class MyJBossInputStream extends JBossObjectInputStream {

    public MyJBossInputStream(InputStream stream) throws IOException {
        super(stream);
    }

    @Override
    protected Class resolveClass(ObjectStreamClass desc) throws ClassNotFoundException, IOException {

        try {
            return ClassUtils.classForName(desc.getName());
        } catch (ClassNotFoundException e) {
            return super.resolveClass(desc);
        }
    }

}