package de.powerstaff.business.service.enums;

import de.mogwai.common.business.enums.BaseEnumItemEnum;

public enum SystemParameterEnum implements BaseEnumItemEnum {

    INDEXER_NETWORK_DIR(1L), INDEXER_SOURCE_PATH(3L), NEWSLETTER_BATCH_COUNT(
            4L), NEWSLETTER_MAX_THREAD_COUNT(5L), NEWSLETTER_SEND_DELTA(6L), NEWSLETTER_SENDER(
            7L), NEWSLETTER_SLEEP_INTERVAL(8L), NEWSLETTER_SUBJECT(9L), NEWSLETTER_TEMPLATE(
            10L), SMTP_HOST(12L), SMTP_PWD(13L), SMTP_USER(14L), SMTP_PORT(16L), NEWSLETTER_ENABLED(
            17L), WEBSYNC_ENABLED(18L), INDEXING_ENABLED(
            20L), MAX_SEARCH_RESULT(27L), PDFFORMATENABLED(
            28L), TXTFORMATENABLED(29L), DOCFORMATENABLED(30L), DOCXFORMATENABLED(
            31L), WRONGDATAREPORTDIR(32L), STARTDATE_FOR_NOT_IN_NEWSLETTER(33L);

    private final Long id;

    SystemParameterEnum(final Long aId) {
        id = aId;
    }

    public Long getId() {
        return id;
    }
}
