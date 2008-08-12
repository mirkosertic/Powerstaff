package de.powerstaff.business.service;

import java.util.Collection;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.business.service.Service;

public interface NavigatingService<T extends Entity> extends Service {

    T findByPrimaryKey(Long id);

    void save(T aBo);

    void delete(T aBo);

    T findFirst();

    T findLast();

    T findPrior(T aObject);

    T findNext(T aObject);

    RecordInfo getRecordInfo(T aObject);

    Collection<T> performQBESearch(T aObject);
    
    T findByRecordNumber(Long aNumber);
}
