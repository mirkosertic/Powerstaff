/*
  Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.

  This library is free software; you can redistribute it and/or modify it under
  the terms of the GNU Lesser General Public License as published by the Free
  Software Foundation; either version 2.1 of the License, or (at your option)
  any later version.

  This library is distributed in the hope that it will be useful, but WITHOUT
  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
  FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
  details.

  You should have received a copy of the GNU Lesser General Public License
  along with this library; if not, write to the Free Software Foundation, Inc.,
  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.business.service;

public interface ProfileIndexerService extends ProfileIndexerServicePublicInterface {

    String NUM_PROFILES = "NUM_PROFILES";

    String PROFILE_PATH_PREFIX = "PROFILE_PATH_";

    String PROFILE_MODIFICATION_PREFIX = "PROFILE_MODIFICATION_";

    String PROFILE_CHECKSUM_PREFIX = "PROFILE_CHECKSUM_";

    String CONTENT = "CONTENT";

    String ORIG_CONTENT = "ORIGCONTENT";

    String CODE = "code";

    String UNIQUE_ID = "id";

    String NAME1 = "name1";

    String NAME2 = "name2";

    String PLZ = "plz";

    String VERFUEGBARKEIT = "availabilityAsDate";

    String STUNDENSATZ = "sallaryLong";

    String KONTAKTSPERRE = "contactforbidden";

    String LETZTERKONTAKT = "lastContactDate";

    String TAGS = "tags";
}