package de.powerstaff.business.service;

import java.util.List;

import de.mogwai.common.business.service.Service;
import de.powerstaff.business.entity.ContactType;

public interface AdditionalDataService extends Service {

	List<ContactType> getContactTypes();
}