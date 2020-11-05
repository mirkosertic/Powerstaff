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

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.util.*;

public class FSCache implements InitializingBean {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FSCache.class);

    private static final String PROFIL_PREFIX = "profil ";
    private static final int SLEEP_IN_MS = 1000 * 60 * 10; // 10 Minuten

    private PowerstaffSystemParameterService systemParameterService;
    private final Map<String, Set<File>> fileCache = new HashMap<>();
    private final Set<File> directoriesToWatch = new HashSet<>();

    private class DirectoryScannerThread extends Thread {

        private final File directory;
        private File[] lastRunFiles;

        private DirectoryScannerThread(final File aDirectory) {
            directory = aDirectory;
            setName("Directory Scanner for " + aDirectory);
        }

        @Override
        public void run() {
            while (!interrupted()) {

                LOGGER.info("Scanning directory {}", directoriesToWatch);

                if (lastRunFiles != null) {
                    for (final File theFile : lastRunFiles) {
                        if (!theFile.exists()) {
                            LOGGER.info("File was deleted in the meantime : {}", theFile);
                            removeFromCache(theFile);
                        }
                    }
                }

                final File[] theCurrentContent = directory.listFiles();
                for (final File theSingleFile : theCurrentContent) {
                    if (!theSingleFile.isDirectory()) {
                        processFile(theSingleFile);
                    }
                }

                lastRunFiles = theCurrentContent;

                LOGGER.info("Scanning finished of {}", directory);

                try {
                    sleep(SLEEP_IN_MS);
                } catch (final Exception e) {
                    LOGGER.error("Error sleeping ", e);
                }
            }
        }
    }

    public FSCache() {
    }

    public void setSystemParameterService(final PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    protected void initialize() {
        synchronized (PROFIL_PREFIX) {
            fileCache.clear();
            refresh(new File(systemParameterService.getIndexerSourcePath()));
        }
    }

    protected void processFile(final File aFile) {
        String theName = aFile.getName().toLowerCase();
        final int p = theName.lastIndexOf(".");
        if (p > 0) {
            theName = theName.substring(0, p);
        }
        if (theName.startsWith(PROFIL_PREFIX)) {
            final String theCode = theName.substring(PROFIL_PREFIX.length());
            registerFileForCode(aFile, theCode);
        }
    }

    private void refresh(final File aFile) {
        directoriesToWatch.add(aFile);
        for (final File theFile : aFile.listFiles()) {
            if (theFile.isDirectory()) {
                refresh(theFile);
            } else {
                processFile(aFile);
            }
        }
    }

    protected void removeFromCache(final File aFile) {
        synchronized (PROFIL_PREFIX) {
            for (final Map.Entry<String, Set<File>> theEntry : fileCache.entrySet()) {
                theEntry.getValue().remove(aFile);
            }
        }
    }

    private void registerFileForCode(final File aFile, final String aCode) {
        synchronized (PROFIL_PREFIX) {
            final Set<File> theFileSet = fileCache.computeIfAbsent(aCode, k -> new HashSet<>());
            if (!theFileSet.contains(aFile)) {
                LOGGER.info("Adding new file to cache {}", aFile);
                theFileSet.add(aFile);
            }
        }
    }

    public Set<File> getFilesForCode(final String aCode) {
        synchronized (PROFIL_PREFIX) {
            final Set<File> theResult = new HashSet<>();
            final Set<File> theFiles = fileCache.get(aCode);
            if (theFiles != null) {
                theResult.addAll(theFiles);
            }
            return theResult;
        }
    }

    public Collection<? extends String> getKnownCodes() {
        synchronized (PROFIL_PREFIX) {
            return new HashSet<>(fileCache.keySet());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
        for (final File theDirectory : directoriesToWatch) {
            final DirectoryScannerThread theScanner = new DirectoryScannerThread(theDirectory);
            theScanner.start();
        }
    }
}