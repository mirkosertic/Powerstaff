package de.powerstaff.business.service.impl;

import de.mogwai.common.business.service.impl.SystemParameterServiceImpl;
import de.powerstaff.business.service.PowerstaffSystemParameterService;
import de.powerstaff.business.service.enums.SystemParameterEnum;

public class PowerstaffSystemParameterServiceImpl extends
        SystemParameterServiceImpl implements PowerstaffSystemParameterService {

    @Override
    public String getIndexerNetworkDir() {
        return getString(SystemParameterEnum.INDEXER_NETWORK_DIR, null);
    }

    @Override
    public String getIndexerSourcePath() {
        return getString(SystemParameterEnum.INDEXER_SOURCE_PATH, null);
    }

    @Override
    public int getNewsletterBatchCount() {
        return getInt(SystemParameterEnum.NEWSLETTER_BATCH_COUNT, 5);
    }

    @Override
    public int getNewsletterMaxThreadCount() {
        return getInt(SystemParameterEnum.NEWSLETTER_MAX_THREAD_COUNT, 5);
    }

    @Override
    public boolean isNewsletterSendDelta() {
        return getBoolean(SystemParameterEnum.NEWSLETTER_SEND_DELTA, true);
    }

    @Override
    public String getNewsletterSender() {
        return getString(SystemParameterEnum.NEWSLETTER_SENDER, null);
    }

    @Override
    public int getNewsletterSleepIntervall() {
        return getInt(SystemParameterEnum.NEWSLETTER_SLEEP_INTERVAL, 2000);
    }

    @Override
    public String getNewsletterSubject() {
        return getString(SystemParameterEnum.NEWSLETTER_SUBJECT, null);
    }

    @Override
    public String getNewsletterTemplate() {
        return getString(SystemParameterEnum.NEWSLETTER_TEMPLATE, null);
    }

    @Override
    public String getSmtpHost() {
        return getString(SystemParameterEnum.SMTP_HOST, null);
    }

    @Override
    public String getSmtpPwd() {
        return getString(SystemParameterEnum.SMTP_PWD, null);
    }

    @Override
    public String getSmtpUser() {
        return getString(SystemParameterEnum.SMTP_USER, null);
    }

    @Override
    public int getSmtpPort() {
        return getInt(SystemParameterEnum.SMTP_PORT, 25);
    }

    @Override
    public boolean isNewsletterEnabled() {
        return getBoolean(SystemParameterEnum.NEWSLETTER_ENABLED, false);
    }

    @Override
    public boolean isWebSyncEnabled() {
        return getBoolean(SystemParameterEnum.WEBSYNC_ENABLED, true);
    }

    @Override
    public boolean isIndexingEnabled() {
        return getBoolean(SystemParameterEnum.INDEXING_ENABLED, true);
    }

    @Override
    public int getMaxSearchResult() {
        return getInt(SystemParameterEnum.MAX_SEARCH_RESULT, 100);
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
    public String getWrongDataReportDir() {
        return getString(SystemParameterEnum.WRONGDATAREPORTDIR, null);
    }

    @Override
    public String getStartDateForNotInNewsletter() {
        return getString(SystemParameterEnum.STARTDATE_FOR_NOT_IN_NEWSLETTER,
                "01.01.2007");
    }
}