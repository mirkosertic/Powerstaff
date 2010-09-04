package de.powerstaff.business.service.impl;

import de.mogwai.common.business.service.impl.SystemParameterServiceImpl;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.enums.SystemParameterEnum;

public class PowerstaffSystemParameterServiceImpl extends
		SystemParameterServiceImpl implements PowerstaffSystemParameterService {

	public String getIndexerNetworkDir() {
		return getString(SystemParameterEnum.INDEXER_NETWORK_DIR, null);
	}

	public void setIndexerNetworkDir(String aValue) {
		setString(SystemParameterEnum.INDEXER_NETWORK_DIR, aValue);
	}

	public String getIndexerPath() {
		return getString(SystemParameterEnum.INDEXER_PATH, null);
	}

	public void setIndexerPath(String aValue) {
		setString(SystemParameterEnum.INDEXER_PATH, aValue);
	}

	public String getIndexerSourcePath() {
		return getString(SystemParameterEnum.INDEXER_SOURCE_PATH, null);
	}

	public void setIndexerSourcePath(String aValue) {
		setString(SystemParameterEnum.INDEXER_SOURCE_PATH, aValue);
	}

	public int getNewsletterBatchCount() {
		return getInt(SystemParameterEnum.NEWSLETTER_BATCH_COUNT, 5);
	}

	public void setNewsletterBatchCount(int aValue) {
		setInt(SystemParameterEnum.NEWSLETTER_BATCH_COUNT, aValue);
	}

	public int getNewsletterMaxThreadCount() {
		return getInt(SystemParameterEnum.NEWSLETTER_MAX_THREAD_COUNT, 5);
	}

	public void setNewsletterMaxThreadCount(int aValue) {
		setInt(SystemParameterEnum.NEWSLETTER_MAX_THREAD_COUNT, aValue);
	}

	public boolean isNewsletterSendDelta() {
		return getBoolean(SystemParameterEnum.NEWSLETTER_SEND_DELTA, true);
	}

	public void setNewsletterSendDelta(boolean aValue) {
		setBoolean(SystemParameterEnum.NEWSLETTER_SEND_DELTA, aValue);
	}

	public String getNewsletterSender() {
		return getString(SystemParameterEnum.NEWSLETTER_SENDER, null);
	}

	public void setNewsletterSender(String aValue) {
		setString(SystemParameterEnum.NEWSLETTER_SENDER, aValue);
	}

	public int getNewsletterSleepIntervall() {
		return getInt(SystemParameterEnum.NEWSLETTER_SLEEP_INTERVAL, 2000);
	}

	public void setNewsletterSleepIntervall(int aValue) {
		setInt(SystemParameterEnum.NEWSLETTER_SLEEP_INTERVAL, aValue);
	}

	public String getNewsletterSubject() {
		return getString(SystemParameterEnum.NEWSLETTER_SUBJECT, null);
	}

	public void setNewsletterSubject(String aValue) {
		setString(SystemParameterEnum.NEWSLETTER_SUBJECT, aValue);
	}

	public String getNewsletterTemplate() {
		return getString(SystemParameterEnum.NEWSLETTER_TEMPLATE, null);
	}

	public void setNewsletterTemplate(String aValue) {
		setString(SystemParameterEnum.NEWSLETTER_TEMPLATE, aValue);
	}

	public String getSmtpHost() {
		return getString(SystemParameterEnum.SMTP_HOST, null);
	}

	public void setSmtpHost(String aValue) {
		setString(SystemParameterEnum.SMTP_HOST, aValue);
	}

	public String getSmtpPwd() {
		return getString(SystemParameterEnum.SMTP_PWD, null);
	}

	public void setSmtpPwd(String aValue) {
		setString(SystemParameterEnum.SMTP_PWD, aValue);
	}

	public String getSmtpUser() {
		return getString(SystemParameterEnum.SMTP_USER, null);
	}

	public void setSmtpUser(String aValue) {
		setString(SystemParameterEnum.SMTP_USER, aValue);
	}

	public int getSmtpPort() {
		return getInt(SystemParameterEnum.SMTP_PORT, 25);
	}

	public void setSmtpPort(int aValue) {
		setInt(SystemParameterEnum.SMTP_PORT, aValue);
	}

	public boolean isNewsletterEnabled() {
		return getBoolean(SystemParameterEnum.NEWSLETTER_ENABLED, false);
	}

	public void setNewsletterEnabled(boolean aValue) {
		setBoolean(SystemParameterEnum.NEWSLETTER_ENABLED, aValue);
	}

	public boolean isWebSyncEnabled() {
		return getBoolean(SystemParameterEnum.WEBSYNC_ENABLED, true);
	}

	public void setWebSyncEnabled(boolean aValue) {
		setBoolean(SystemParameterEnum.WEBSYNC_ENABLED, aValue);
	}

	public boolean isDeletedDocumentRemovalEnabled() {
		return getBoolean(SystemParameterEnum.DELETEDDOCUMENT_REMOVAL, true);
	}

	public boolean isIndexingEnabled() {
		return getBoolean(SystemParameterEnum.INDEXING_ENABLED, true);
	}

	public void setDeletedDocumentRemovalEnabled(boolean aValue) {
		setBoolean(SystemParameterEnum.DELETEDDOCUMENT_REMOVAL, aValue);
	}

	public void setIndexingEnabled(boolean aValue) {
		setBoolean(SystemParameterEnum.INDEXING_ENABLED, aValue);
	}

	public int getMaxSearchResult() {
		return getInt(SystemParameterEnum.MAX_SEARCH_RESULT, 100);
	}

	public void setMaxSearchResult(int aValue) {
		setInt(SystemParameterEnum.MAX_SEARCH_RESULT, aValue);
	}

	@Override
	public boolean isDOCFormatEnabled() {
		return getBoolean(SystemParameterEnum.DOCFORMATENABLED, true);
	}

	@Override
	public boolean isDOCXFormatEnabled() {
		return getBoolean(SystemParameterEnum.DOCXFORMATENABLED, false);
	}

	@Override
	public boolean isPDFFormatEnabled() {
		return getBoolean(SystemParameterEnum.PDFFORMATENABLED, false);
	}

	@Override
	public boolean isTXTFormatEnabled() {
		return getBoolean(SystemParameterEnum.TXTFORMATENABLED, false);
	}

	@Override
	public void setDOCFormatEnabled(boolean aValue) {
		setBoolean(SystemParameterEnum.DOCFORMATENABLED, aValue);
	}

	@Override
	public void setDOCXFormatEnabled(boolean aValue) {
		setBoolean(SystemParameterEnum.DOCXFORMATENABLED, aValue);
	}

	@Override
	public void setPDFFormatEnabled(boolean aValue) {
		setBoolean(SystemParameterEnum.PDFFORMATENABLED, aValue);
	}

	@Override
	public void setTXTFormatEnabled(boolean aValue) {
		setBoolean(SystemParameterEnum.TXTFORMATENABLED, aValue);
	}

	@Override
	public String getWrongDataReportDir() {
		return getString(SystemParameterEnum.WRONGDATAREPORTDIR, null);
	}

	@Override
	public void setWrongDataReportDir(String aValue) {
		setString(SystemParameterEnum.WRONGDATAREPORTDIR, aValue);
	}

	@Override
	public String getStartDateForNotInNewsletter() {
		return getString(SystemParameterEnum.STARTDATE_FOR_NOT_IN_NEWSLETTER,
				"01.01.2007");
	}

	@Override
	public void setStartDateForNotInNewsletter(String aValue) {
		setString(SystemParameterEnum.STARTDATE_FOR_NOT_IN_NEWSLETTER, aValue);
	}
}