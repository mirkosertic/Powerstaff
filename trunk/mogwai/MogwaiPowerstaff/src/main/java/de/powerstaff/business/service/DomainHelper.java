package de.powerstaff.business.service;

import de.powerstaff.business.service.impl.reader.DocumentReaderFactory;
import org.springframework.context.ApplicationContext;

public class DomainHelper {

    private static DomainHelper ME = new DomainHelper();

    public static synchronized DomainHelper getInstance() {
        return ME;
    }

    private ApplicationContext context;

    private DomainHelper() {
    }

    public void registerApplicationContext(ApplicationContext aContext) {
        context = aContext;
    }

    public ProfileSearchService getProfileSearchService() {
        return (ProfileSearchService) context.getBean("profileSearchService");
    }

    public DocumentReaderFactory getDocumentReaderFactory() {
        return (DocumentReaderFactory) context.getBean("readerFactory");
    }
}