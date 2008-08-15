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
package de.mogwai.common.business.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Basisklasse für Data Transfer Objects (DTO). Sie dienen dem Datenaustausch
 * zwischen den verschiedenen Schichten der Anwendung. Im Gegesatz zu Entities
 * werden DTOs nicht persistiert. Ein Beispiel für eine konkrete Ausprägung ist
 * ein DTO, welches sämtlich Suchkriterien einer Suche kapselt.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:13:06 $
 */
public abstract class DataTransferObject implements Serializable {

    private static final long serialVersionUID = 3256446889040622648L;

    /**
     * @see java.lang.Object#toString()
     * @return see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
