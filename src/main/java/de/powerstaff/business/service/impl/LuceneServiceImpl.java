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
import org.apache.lucene.store.LockObtainFailedException;

import de.powerstaff.business.lucene.analysis.ProfileAnalyzerFactory;
import de.powerstaff.business.service.LuceneService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;

public class LuceneServiceImpl implements LuceneService {

	private PowerstaffSystemParameterService systemParameterService;

	private IndexReader indexReader;

	private IndexSearcher indexSearcher;

	private IndexWriter indexWriter;

	public PowerstaffSystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

	public void setSystemParameterService(
			PowerstaffSystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	@Override
	public synchronized IndexReader getIndexReader()
			throws CorruptIndexException, IOException {
		if (indexReader == null || !indexReader.isCurrent()) {
			indexReader = IndexReader.open(systemParameterService
					.getIndexerPath());
			indexSearcher = null;
		}
		return indexReader;
	}

	@Override
	public synchronized IndexSearcher getIndexSearcher()
			throws CorruptIndexException, IOException {
		if (indexSearcher == null) {
			indexSearcher = new IndexSearcher(getIndexReader());
		}
		return indexSearcher;
	}

	@Override
	public IndexWriter getIndexWriter() throws CorruptIndexException,
			LockObtainFailedException, IOException {
		if (indexWriter == null) {
			try {

				// Try to append
				indexWriter = new IndexWriter(systemParameterService
						.getIndexerPath(), ProfileAnalyzerFactory
						.createAnalyzer(), false);

			} catch (Exception ex) {

				// Create a new index
				indexWriter = new IndexWriter(systemParameterService
						.getIndexerPath(), ProfileAnalyzerFactory
						.createAnalyzer(), true);
				
				indexReader = null;
				indexSearcher = null;
			}
		}
		return indexWriter;
	}

	@Override
	public synchronized void shutdownIndexWriter() throws CorruptIndexException, IOException {
		try {
			indexWriter.optimize();
		} finally {
			indexWriter.close();
			indexWriter = null;
		}
	}

	@Override
	public synchronized IndexWriter createNewIndex() throws CorruptIndexException, LockObtainFailedException, IOException {
		// Create a new index
		indexWriter = new IndexWriter(systemParameterService
				.getIndexerPath(), ProfileAnalyzerFactory
				.createAnalyzer(), true);
		
		indexReader = null;
		indexSearcher = null;

		return indexWriter;
	}
}