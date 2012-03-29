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
package de.powerstaff.business.dto;

import de.powerstaff.business.entity.Project;

public interface SearchRequestSupport {

    String getProfileContent();

    void setProfileContent(String aContent);

    String getPlz();

    void setPlz(String plz);

    Long getStundensatzVon();

    void setStundensatzVon(Long stundensatzVon);

    Long getStundensatzBis();

    void setStundensatzBis(Long stundensatzBis);

    String getSortierung();

    void setSortierung(String sortierung);

    Project getProject();

    void setProject(Project aProject);

    Long getId();

    void setId(Long aId);
}