/**
 * Copyright 2002 - 2007 the Mogwai Project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.mogwai.common.dao.inmem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import de.mogwai.common.business.entity.Entity;
import de.mogwai.common.dao.DAO;

/**
 * Generic Inmem DAO.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:12:11 $
 * @param <T>
 *                Der Typ der Entity
 */
public class GenericInmemDao<T extends Entity> implements DAO {

    private long counter = 1;

    private Map<Long, T> cache = new HashMap<Long, T>();

    public void delete(Object aValue) {
        Entity theEntity = (Entity) aValue;
        if (theEntity.getId() != null) {
            cache.remove(theEntity.getId());
        }
    }

    public Object getById(Class aClass, Long aId) {
        return cache.get(aId);
    }

    public List<T> findAll() {
        Vector<T> theResult = new Vector<T>();
        theResult.addAll(cache.values());
        return theResult;
    }

    public void save(Object aValue) {
        Entity theEntity = (Entity) aValue;
        if (theEntity.getId() == null) {
            theEntity.setId(counter++);
            cache.put(theEntity.getId(), (T) theEntity);
        }
    }

}
