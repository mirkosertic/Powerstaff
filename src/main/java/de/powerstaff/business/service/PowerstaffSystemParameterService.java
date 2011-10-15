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

import de.mogwai.common.business.service.Service;

public interface PowerstaffSystemParameterService extends Service {

	String getWrongDataReportDir();

	void setWrongDataReportDir(String aValue);

	String getIndexerNetworkDir();

	void setIndexerNetworkDir(String aValue);

	String getIndexerPath();

	void setIndexerPath(String aValue);

	String getIndexerSourcePath();

	void setIndexerSourcePath(String aValue);

	int getNewsletterBatchCount();

	void setNewsletterBatchCount(int aValue);

	int getNewsletterMaxThreadCount();

	void setNewsletterMaxThreadCount(int aValue);

	boolean isNewsletterSendDelta();

	void setNewsletterSendDelta(boolean aValue);

	String getNewsletterSender();

	void setNewsletterSender(String aValue);

	int getNewsletterSleepIntervall();

	void setNewsletterSleepIntervall(int aValue);

	String getNewsletterSubject();

	void setNewsletterSubject(String aValue);

	String getNewsletterTemplate();

	void setNewsletterTemplate(String aValue);

	String getSmtpHost();

	void setSmtpHost(String aValue);

	int getSmtpPort();

	void setSmtpPort(int aValue);

	String getSmtpPwd();

	void setSmtpPwd(String aValue);

	String getSmtpUser();

	void setSmtpUser(String aValue);

	boolean isNewsletterEnabled();

	void setNewsletterEnabled(boolean aValue);

	boolean isWebSyncEnabled();

	void setWebSyncEnabled(boolean aValue);

	boolean isIndexingEnabled();

	void setIndexingEnabled(boolean aValue);

	int getMaxSearchResult();

	void setMaxSearchResult(int aValue);

	/**
	 * @return True if the DOC Format is enabled for indexing
	 */
	boolean isDOCFormatEnabled();

	void setDOCFormatEnabled(boolean aValue);

	/**
	 * @return True if the DOCX Format is enabled for indexing
	 */
	boolean isDOCXFormatEnabled();

	void setDOCXFormatEnabled(boolean aValue);

	/**
	 * @return True if the TXT Format is enabled for indexing
	 */
	boolean isTXTFormatEnabled();

	void setTXTFormatEnabled(boolean aValue);

	/**
	 * @return True if the PDF Format is enabled for indexing
	 */
	boolean isPDFFormatEnabled();

	void setPDFFormatEnabled(boolean aValue);

	String getStartDateForNotInNewsletter();

	void setStartDateForNotInNewsletter(String aValue);
}
