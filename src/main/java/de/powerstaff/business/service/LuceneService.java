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

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.LockObtainFailedException;

import de.mogwai.common.business.service.Service;

public interface LuceneService extends Service {

	IndexReader getIndexReader() throws CorruptIndexException, IOException ;
	
	IndexSearcher getIndexSearcher() throws CorruptIndexException, IOException ;
	
	IndexWriter getIndexWriter() throws CorruptIndexException, LockObtainFailedException, IOException ;
	
	IndexWriter createNewIndex() throws CorruptIndexException, LockObtainFailedException, IOException ; 
	
	void shutdownIndexWriter() throws CorruptIndexException, IOException;
}