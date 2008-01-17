package de.powerstaff.business.service;

import de.mogwai.common.business.service.Service;

public interface PowerstaffSystemParameterService extends Service {

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

    String getRemoteWebUrl();

    void setRemoteWebUrl(String aValue);

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

    boolean isDeletedDocumentRemovalEnabled();

    void setDeletedDocumentRemovalEnabled(boolean aValue);

    int getProfileMaxSearchResult();

    void setProfileMaxSearchResult(int aValue);

}
