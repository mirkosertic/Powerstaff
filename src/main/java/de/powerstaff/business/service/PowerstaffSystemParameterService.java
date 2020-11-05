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

public interface PowerstaffSystemParameterService {

    String getWrongDataReportDir();

    String getIndexerNetworkDir();

    String getIndexerSourcePath();

    int getNewsletterBatchCount();

    int getNewsletterMaxThreadCount();

    boolean isNewsletterSendDelta();

    String getNewsletterSender();

    int getNewsletterSleepIntervall();

    String getNewsletterSubject();

    String getNewsletterTemplate();

    String getSmtpHost();

    int getSmtpPort();

    String getSmtpPwd();

    String getSmtpUser();

    boolean isNewsletterEnabled();

    boolean isWebSyncEnabled();

    boolean isIndexingEnabled();

    int getMaxSearchResult();

    /**
     * @return True if the DOC Format is enabled for indexing
     */
    boolean isDOCFormatEnabled();

    /**
     * @return True if the DOCX Format is enabled for indexing
     */
    boolean isDOCXFormatEnabled();

    /**
     * @return True if the TXT Format is enabled for indexing
     */
    boolean isTXTFormatEnabled();

    /**
     * @return True if the PDF Format is enabled for indexing
     */
    boolean isPDFFormatEnabled();

    String getStartDateForNotInNewsletter();
}
