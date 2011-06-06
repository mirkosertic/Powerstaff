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

import java.io.File;
import java.io.IOException;

import de.mogwai.common.logging.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.springframework.beans.factory.InitializingBean;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.lucene.analysis.ProfileAnalyzerFactory;
import de.powerstaff.business.service.LuceneService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import sun.util.LocaleServiceProviderPool;

public class LuceneServiceImpl extends LogableService implements LuceneService,
		InitializingBean {

	private PowerstaffSystemParameterService systemParameterService;

	private IndexReader indexReader;

	private IndexSearcher indexSearcher;

	private IndexWriter indexWriter;

	private Directory directory;

	private int indexWriterUsageCount;

	public void setSystemParameterService(
			PowerstaffSystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	@Override
	public IndexReader getIndexReader() throws CorruptIndexException,
			IOException {
		synchronized (this) {
			if (indexReader == null || !indexReader.isCurrent()) {

				if (indexReader == null) {
					logger
							.logInfo("Creating new indexreader as there is no one");
				} else {
                    closeIndexReader();
					logger
							.logInfo("Reopen existing indexreader as there are new segments");
				}

				indexReader = IndexReader.open(directory);

                closeIndexSearcher();
			}
			return indexReader;
		}
	}

	@Override
	public synchronized IndexSearcher getIndexSearcher()
			throws CorruptIndexException, IOException {
		synchronized (this) {
			if (indexSearcher == null) {
				indexSearcher = new IndexSearcher(getIndexReader());
			}
			return indexSearcher;
		}
	}

    private void closeIndexReader() {
        synchronized(this) {
            if (indexReader != null) {
                try {
                    indexReader.close();
                } catch (Exception e) {
                    logger.logError("Error closing IndexReader", e);
                }
                indexReader = null;
            }
        }
    }

    private void closeIndexSearcher() {
        synchronized(this) {
            if (indexSearcher != null) {
                try {
                    indexSearcher.close();
                } catch (Exception e) {
                    logger.logError("Error closing IndexSearcher", e);
                }
                indexSearcher = null;
            }
        }
    }

	@Override
	public IndexWriter getIndexWriter() throws CorruptIndexException,
			LockObtainFailedException, IOException {

		synchronized (this) {
			if (indexWriter == null) {

				indexWriterUsageCount = 1;

				try {

					logger.logInfo("Trying to append to existing index in "
							+ directory);

					// Try to append
					indexWriter = new IndexWriter(directory,
							ProfileAnalyzerFactory.createAnalyzer(), false,
							IndexWriter.MaxFieldLength.UNLIMITED);

				} catch (LockObtainFailedException e) {

					logger
							.logInfo("Index seems to be locked, trying to unlock");

					try {

						IndexWriter.unlock(directory);

						indexWriter = new IndexWriter(directory,
								ProfileAnalyzerFactory.createAnalyzer(), false,
								IndexWriter.MaxFieldLength.UNLIMITED);

						logger
								.logInfo("Index unlocked and writer created for exing index");

					} catch (Exception e1) {

						logger
								.logError(
										"Error on unlocking existing index, will create a new one",
										e1);

						// Create a new index
						indexWriter = new IndexWriter(directory,
								ProfileAnalyzerFactory.createAnalyzer(), true,
								IndexWriter.MaxFieldLength.UNLIMITED);

                        closeIndexReader();
                        closeIndexSearcher();
					}

				} catch (Exception ex) {

					logger.logError(
							"Error appending to index, creating a new one", ex);

					// Create a new index
					indexWriter = new IndexWriter(directory,
							ProfileAnalyzerFactory.createAnalyzer(), true,
							IndexWriter.MaxFieldLength.UNLIMITED);

                    closeIndexReader();
                    closeIndexSearcher();
				}
			} else {

				logger.logInfo("Reusing existing index writer");

				indexWriterUsageCount++;
			}
			return indexWriter;
		}
	}

	@Override
	public void shutdownIndexWriter(boolean aOptimize)
			throws CorruptIndexException, IOException {

		synchronized (this) {
			if (indexWriter != null) {

				if (indexWriterUsageCount < 1) {
					logger.logError("Wrong instance count of index writers!");
				}

				indexWriterUsageCount--;

				if (indexWriterUsageCount <= 0) {

					logger.logInfo("Shutting down index writer");
                    indexWriter.commit();

                    forceNewIndexReader();

					try {
						if (aOptimize) {

							logger.logInfo("Optimizing index");
							indexWriter.optimize();
							logger.logInfo("Optimizing done");
						}
                        indexWriter.close();
                        indexWriter = null;

                        indexWriterUsageCount = 0;

					} finally {
                        forceNewIndexReader();
					}
				} else {

					logger
							.logInfo("Will not shutdown indexwriter as usage count is "
									+ indexWriterUsageCount);
				}
			}
		}
	}

	@Override
	public void createNewIndex() throws CorruptIndexException,
			LockObtainFailedException, IOException {

		synchronized (this) {
			logger.logInfo("Creating new index");

            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (Exception e) {
                    logger.logError("Error closing old index writer", e);
                }
            }

			// Create a new index
			indexWriter = new IndexWriter(directory, ProfileAnalyzerFactory
					.createAnalyzer(), true,
					IndexWriter.MaxFieldLength.UNLIMITED);

            closeIndexReader();
            closeIndexSearcher();

			indexWriterUsageCount = 1;

			shutdownIndexWriter(false);
		}
	}

	public void afterPropertiesSet() throws Exception {
		String theFile = systemParameterService.getIndexerPath();
		directory = FSDirectory.open(new File(theFile));
	}

	@Override
	public void forceNewIndexReader() {
		synchronized (this) {
            logger.logInfo("Forcing new index reader");
			if (indexWriter != null) {
				try {
					indexWriter.commit();
				} catch (Exception e) {
					logger.logError("Cannot commit IndexWriter", e);
				}
			}
            closeIndexReader();
            closeIndexSearcher();
		}
	}
}