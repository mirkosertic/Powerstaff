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
package de.powerstaff.business.service.impl;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSLockFactory;
import org.springframework.beans.factory.InitializingBean;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.lucene.analysis.ProfileAnalyzerFactory;
import de.powerstaff.business.service.LuceneService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;

public class LuceneServiceImpl extends LogableService implements LuceneService , InitializingBean {

    private PowerstaffSystemParameterService systemParameterService;

    private IndexReader indexReader;

    private IndexSearcher indexSearcher;

    private IndexWriter indexWriter;
    
    private Directory directory;

    public PowerstaffSystemParameterService getSystemParameterService() {
        return systemParameterService;
    }

    public void setSystemParameterService(PowerstaffSystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }
    
    @Override
    public synchronized IndexReader getIndexReader() throws CorruptIndexException, IOException {
        if (indexReader == null || !indexReader.isCurrent()) {

            if (indexReader == null) {
                logger.logInfo("Creating new indexreader as there is no one");
            } else {
                logger.logInfo("Reopen existing indexreader as there are new segments");
            }

            indexReader = IndexReader.open(directory);
            indexSearcher = null;
        }
        return indexReader;
    }

    @Override
    public synchronized IndexSearcher getIndexSearcher() throws CorruptIndexException, IOException {
        if (indexSearcher == null) {
            indexSearcher = new IndexSearcher(getIndexReader());
        }
        return indexSearcher;
    }

    @Override
    public synchronized IndexWriter getIndexWriter() throws CorruptIndexException, LockObtainFailedException,
            IOException {
        if (indexWriter == null) {

            try {
                
                logger.logInfo("Trying to append to existing index in " + directory);

                // Try to append
                indexWriter = new IndexWriter(directory, ProfileAnalyzerFactory.createAnalyzer(), false);
                
            } catch (LockObtainFailedException e) {

                logger.logInfo("Index seems to be locked, trying to unlock");
                
                try {
                    
                    IndexWriter.unlock(directory);
                    
                    indexWriter = new IndexWriter(directory, ProfileAnalyzerFactory.createAnalyzer(), false);                    
                    
                    logger.logInfo("Index unlocked and writer created for exing index");
                    
                } catch (Exception e1) {

                    logger.logError("Error on unlocking existing index, will create a new one", e1);

                    // Create a new index
                    indexWriter = new IndexWriter(directory, ProfileAnalyzerFactory.createAnalyzer(), true);

                    indexReader = null;
                    indexSearcher = null;
                }

            } catch (Exception ex) {

                logger.logError("Error appending to index, creating a new one", ex);

                // Create a new index
                indexWriter = new IndexWriter(directory, ProfileAnalyzerFactory.createAnalyzer(), true);

                indexReader = null;
                indexSearcher = null;
            }
        }
        return indexWriter;
    }

    @Override
    public synchronized void shutdownIndexWriter() throws CorruptIndexException, IOException {

        logger.logInfo("Shutting down index writer");

        try {
            indexWriter.optimize();
        } finally {
            indexWriter.close();
            indexWriter = null;
            
            indexReader = null;
            indexSearcher = null;
        }
    }

    @Override
    public synchronized IndexWriter createNewIndex() throws CorruptIndexException, LockObtainFailedException,
            IOException {

        logger.logInfo("Creating new index");

        // Create a new index
        indexWriter = new IndexWriter(directory, ProfileAnalyzerFactory.createAnalyzer(),
                true);

        indexReader = null;
        indexSearcher = null;

        return indexWriter;
    }

    public void afterPropertiesSet() throws Exception {
        String theFile = systemParameterService.getIndexerPath();
        directory = FSDirectory.getDirectory(theFile);
    }
}