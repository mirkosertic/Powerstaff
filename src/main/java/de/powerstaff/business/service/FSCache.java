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
package de.powerstaff.business.service;

import java.io.File;
import java.util.*;

public class FSCache {

    private static final int TTL_IN_SECONDS = 60;
    private static final String PROFIL_PREFIX = "profil ";

    private PowerstaffSystemParameterService systemParameterService;
    private long lastupdate;
    private Map<String, Set<File>> fileCache = new HashMap<String, Set<File>>();

    public void setSystemParameterService(PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public boolean needsRefresh() {
        synchronized (PROFIL_PREFIX) {
            return lastupdate < System.currentTimeMillis() - (1000 * TTL_IN_SECONDS);
        }
    }

    public void refresh() {
        synchronized (PROFIL_PREFIX) {
            fileCache.clear();
            refresh(new File(systemParameterService.getIndexerSourcePath()));
        }

    }

    private void refresh(File aFile) {
        for (File theFile : aFile.listFiles()) {
            if (theFile.isDirectory()) {
                refresh(theFile);
            } else {
                String theName = theFile.getName().toLowerCase();
                int p = theName.lastIndexOf(".");
                if (p > 0) {
                    theName = theName.substring(0, p);
                }
                if (theName.startsWith(PROFIL_PREFIX)) {
                    String theCode = theName.substring(PROFIL_PREFIX.length());
                    registerFileForCode(theFile, theCode);
                }
            }
        }
        lastupdate = System.currentTimeMillis();
    }

    private void registerFileForCode(File aFile, String aCode) {
        Set<File> theFileSet = fileCache.get(aCode);
        if (theFileSet == null) {
            theFileSet = new HashSet<File>();
            fileCache.put(aCode, theFileSet);
        }
        theFileSet.add(aFile);
    }

    public Set<File> getFilesForCode(String aCode) {
        synchronized (PROFIL_PREFIX) {
            return fileCache.get(aCode);
        }
    }

    public Collection<? extends String> getKnownCodes() {
        synchronized (PROFIL_PREFIX) {
            return new HashSet<String>(fileCache.keySet());
        }
    }
}