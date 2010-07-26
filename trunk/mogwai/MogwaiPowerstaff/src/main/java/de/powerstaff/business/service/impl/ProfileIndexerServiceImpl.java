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
import java.io.StringReader;
import java.util.UUID;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.mogwai.common.business.service.impl.LogableService;
import de.powerstaff.business.service.LuceneService;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.ProfileIndexerService;
import de.powerstaff.business.service.ServiceLoggerService;
import de.powerstaff.business.service.impl.reader.DocumentReader;
import de.powerstaff.business.service.impl.reader.DocumentReaderFactory;
import de.powerstaff.business.service.impl.reader.ReadResult;

/**
 * @author Mirko Sertic
 */
public class ProfileIndexerServiceImpl extends LogableService implements
		ProfileIndexerService {

	private static final String SERVICE_ID = "ProfileIndexer";

	private DocumentReaderFactory readerFactory;

	private ServiceLoggerService serviceLogger;

	private PowerstaffSystemParameterService systemParameterService;

	private LuceneService luceneService;

	private boolean running;

	public void setReaderFactory(DocumentReaderFactory readerFactory) {
		this.readerFactory = readerFactory;
	}

	public void setSystemParameterService(
			PowerstaffSystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public void setLuceneService(LuceneService luceneService) {
		this.luceneService = luceneService;
	}

	private void processDeletedOrUpdatedFiles() {

		logger.logInfo("Processing deleted or updated files");

		try {

			IndexReader reader = luceneService.getIndexReader();
			IndexWriter writer = luceneService.getIndexWriter();

			for (int i = 0; i < reader.maxDoc(); i++) {

				Document document = reader.document(i);
				File file = new File(document.get(PATH));
				if (!file.exists()
						|| !("" + file.lastModified()).equals(document
								.get(MODIFIED))) {

					logger.logInfo("Deleting file " + file.toString()
							+ " from index as it was modified");

					writer.deleteDocuments(createQueryForFile(file));
				}
			}

		} catch (Exception e) {

			logger.logError("Error on execution", e);

		}

		logger.logInfo("Done with deleted or updated files");
	}

	private Query createQueryForFile(File file) throws ParseException {
		Analyzer theAnalyzer = new KeywordAnalyzer();
		QueryParser theParser = new QueryParser(PATH, theAnalyzer);

		Query theQuery = theParser.parse("\""
				+ file.toString().replace("\\", "\\\\") + "\"");
		return theQuery;
	}

	/**
	 * Run the indexer.
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void runIndexer() {

		// Läuft der indexer bereits ?
		if (this.running) {
			return;
		}

		if (!systemParameterService.isIndexingEnabled()) {
			logger.logInfo("Indexing disabled");
			return;
		}

		readerFactory.initialize();

		serviceLogger.logStart(SERVICE_ID, "");

		// Jetzt läuft er
		this.running = true;

		long theStartTime = System.currentTimeMillis();

		logger.logInfo("Running indexing");

		try {

			if (systemParameterService.isDeletedDocumentRemovalEnabled()) {
				processDeletedOrUpdatedFiles();
			} else {
				logger.logInfo("Deleted document removal is disabled");
			}

			File sourcePath = new File(systemParameterService
					.getIndexerSourcePath());
			if ((sourcePath.exists()) && (sourcePath.isDirectory())) {

				IndexWriter writer = luceneService.getIndexWriter();

				indexDocs(writer, sourcePath, sourcePath.toString(), 0);

			} else {
				logger.logDebug("Source path " + sourcePath
						+ " does not exists");
			}
		} catch (Exception ex) {

			logger.logError("Error on indexing", ex);

		} finally {

			try {
				luceneService.shutdownIndexWriter();
			} catch (Exception e) {
				logger.logError("Error on shutdown of index writer", e);
			}

			theStartTime = System.currentTimeMillis() - theStartTime;

			logger.logInfo("Indexing finished");

			serviceLogger.logEnd(SERVICE_ID, "Dauer = " + theStartTime + "ms");

			this.running = false;
		}
	}

	private void processFile(IndexWriter aWriter, File aFile,
			String aBaseFileName) {

		try {

			// First, try to detect if the file exists
			Searcher theSearcher = luceneService.getIndexSearcher();
			Query theQuery = createQueryForFile(aFile);
			Hits theHits = theSearcher.search(theQuery);
			if (theHits.length() > 0) {

				logger.logDebug("Ignoring file " + aFile
						+ " as it seems to be duplicate");

				return;
			}

			DocumentReader theDocumentReader = this.readerFactory
					.getDocumentReaderForFile(aFile);
			if (theDocumentReader != null) {

				// Try to get the coding
				String theCode = aFile.getName().substring("Profil ".length())
						.trim();
				int p = theCode.lastIndexOf(".");
				if (p > 0) {
					theCode = theCode.substring(0, p);
				}

				String theStrippedPath = aFile.toString().substring(
						aBaseFileName.length() + 1);

				theCode = theCode.trim();

				try {

					logger.logInfo("Adding file " + aFile + " to index");

					Document doc = new Document();
					ReadResult theResult = theDocumentReader.getContent(aFile);

					doc.add(new Field(PATH, aFile.getPath(), Field.Store.YES,
							Field.Index.UN_TOKENIZED));
					doc.add(new Field(CODE, theCode, Field.Store.YES,
							Field.Index.UN_TOKENIZED));
					doc.add(new Field(UNIQUE_ID, UUID.randomUUID().toString(),
							Field.Store.YES, Field.Index.UN_TOKENIZED));
					doc.add(new Field(STRIPPEDPATH, theStrippedPath,
							Field.Store.YES, Field.Index.UN_TOKENIZED));
					doc.add(new Field(MODIFIED, "" + aFile.lastModified(),
							Field.Store.YES, Field.Index.NO));
					doc.add(new Field(INDEXINGTIME, ""
							+ System.currentTimeMillis(), Field.Store.YES,
							Field.Index.NO));
					doc.add(new Field(ORIG_CONTENT, theResult.getContent(),
							Field.Store.YES, Field.Index.UN_TOKENIZED));
					doc.add(new Field(CONTENT, new StringReader(theResult
							.getContent())));

					aWriter.addDocument(doc);

				} catch (Exception ex) {

					logger.logInfo("Error on indexing file " + aFile + " : "
							+ ex.getMessage());
					logger.logDebug("Error on indexing file " + aFile, ex);
				}
			} else {

				logger.logDebug("Cannot index file " + aFile
						+ " as there is no DocumentReader.");

			}

		} catch (Exception e) {
			logger.logError("General error on indexing on file " + aFile, e);
		}

	}

	private int indexDocs(IndexWriter aWriter, File aFile, String aBaseFile,
			int aCounter) throws IOException {

		// do not try to index files that cannot be read
		if (aFile.canRead()) {

			if (aFile.isDirectory()) {

				logger.logDebug("Scanning directory " + aFile);

				String[] files = aFile.list();
				// an IO error could occur
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						aCounter += indexDocs(aWriter,
								new File(aFile, files[i]), aBaseFile, 0);
					}
				}
			} else {

				// Ok, here we go

				// Only try to index filex with a given valid profile name !!
				String fileName = aFile.toString();
				logger.logDebug("Found file " + fileName);

				int p = fileName.lastIndexOf(File.separator);
				if (p >= 0) {
					fileName = fileName.substring(p + 1);
				}

				// Is it valid ?
				if (fileName.startsWith("Profil ")) {

					aCounter = aCounter + 1;

					// Ok, the profile is valid
					// It can be indexed
					processFile(aWriter, aFile, aBaseFile);

				} else {

					// it is not a valid file
					// Log this information

					logger.logDebug("File is not a valid profile");
				}
			}
		}
		return aCounter;
	}

	public ServiceLoggerService getServiceLogger() {
		return serviceLogger;
	}

	public void setServiceLogger(ServiceLoggerService serviceLogger) {
		this.serviceLogger = serviceLogger;
	}

	public void rebuildIndex() {
		File sourcePath = new File(systemParameterService
				.getIndexerSourcePath());
		if ((sourcePath.exists()) && (sourcePath.isDirectory())) {

			try {

				luceneService.createNewIndex();
				luceneService.shutdownIndexWriter();

			} catch (Exception e) {
				logger.logError("Unable to rebuild index", e);
			}
		}
	}
}