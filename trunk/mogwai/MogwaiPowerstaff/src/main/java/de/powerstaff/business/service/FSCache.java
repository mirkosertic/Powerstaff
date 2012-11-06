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

import de.mogwai.common.logging.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.util.*;

public class FSCache implements InitializingBean {

    private static final Logger LOGGER = new Logger(FSCache.class);

    private static final String PROFIL_PREFIX = "profil ";
    private static final int SLEEP_IN_MS = 1000 * 60 * 10; // 10 Minuten

    private PowerstaffSystemParameterService systemParameterService;
    private Map<String, Set<File>> fileCache = new HashMap<String, Set<File>>();
    private Set<File> directoriesToWatch = new HashSet<File>();

    private class DirectoryScannerThread extends Thread {

        private File directory;
        private File[] lastRunFiles;

        private DirectoryScannerThread(File aDirectory) {
            directory = aDirectory;
            setName("Directory Scanner for " + aDirectory);
        }

        @Override
        public void run() {
            while (!interrupted()) {

                LOGGER.logInfo("Scanning directory " + directoriesToWatch);

                if (lastRunFiles != null) {
                    for (File theFile : lastRunFiles) {
                        if (!theFile.exists()) {
                            LOGGER.logInfo("File was deleted in the meantime : " + theFile);
                            removeFromCache(theFile);
                        }
                    }
                }

                File[] theCurrentContent = directory.listFiles();
                for (File theSingleFile : theCurrentContent) {
                    if (!theSingleFile.isDirectory()) {
                        processFile(theSingleFile);
                    }
                }

                lastRunFiles = theCurrentContent;

                LOGGER.logInfo("Scanning finished of " + directory);

                try {
                    sleep(SLEEP_IN_MS);
                } catch (Exception e) {
                    LOGGER.logError("Error sleeping ", e);
                }
            }
        }
    }

    public FSCache() {
    }

    public void setSystemParameterService(PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    protected void initialize() {
        synchronized (PROFIL_PREFIX) {
            fileCache.clear();
            refresh(new File(systemParameterService.getIndexerSourcePath()));
        }
    }

    protected void processFile(File aFile) {
        String theName = aFile.getName().toLowerCase();
        int p = theName.lastIndexOf(".");
        if (p > 0) {
            theName = theName.substring(0, p);
        }
        if (theName.startsWith(PROFIL_PREFIX)) {
            String theCode = theName.substring(PROFIL_PREFIX.length());
            registerFileForCode(aFile, theCode);
        }
    }

    private void refresh(File aFile) {
        directoriesToWatch.add(aFile);
        for (File theFile : aFile.listFiles()) {
            if (theFile.isDirectory()) {
                refresh(theFile);
            } else {
                processFile(aFile);
            }
        }
    }

    protected void removeFromCache(File aFile) {
        synchronized (PROFIL_PREFIX) {
            for (Map.Entry<String, Set<File>> theEntry : fileCache.entrySet()) {
                theEntry.getValue().remove(aFile);
            }
        }
    }

    private void registerFileForCode(File aFile, String aCode) {
        synchronized (PROFIL_PREFIX) {
            Set<File> theFileSet = fileCache.get(aCode);
            if (theFileSet == null) {
                theFileSet = new HashSet<File>();
                fileCache.put(aCode, theFileSet);
            }
            if (!theFileSet.contains(aFile)) {
                LOGGER.logInfo("Adding new file to cache " + aFile);
                theFileSet.add(aFile);
            }
        }
    }

    public Set<File> getFilesForCode(String aCode) {
        synchronized (PROFIL_PREFIX) {
            Set<File> theResult = new HashSet<File>();
            Set<File> theFiles = fileCache.get(aCode);
            if (theFiles != null) {
                theResult.addAll(theFiles);
            }
            return theResult;
        }
    }

    public Collection<? extends String> getKnownCodes() {
        synchronized (PROFIL_PREFIX) {
            return new HashSet<String>(fileCache.keySet());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
        for (File theDirectory : directoriesToWatch) {
            DirectoryScannerThread theScanner = new DirectoryScannerThread(theDirectory);
            theScanner.start();
        }
    }
}