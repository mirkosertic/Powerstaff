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
package de.mogwai.common.dao.hibernate;

import de.mogwai.common.dao.DAO;
import de.powerstaff.business.service.OptimisticLockException;
import de.powerstaff.business.service.ReferenceExistsException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate Implementation des generischen Data Access Objects.
 *
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:25:22 $
 */
public abstract class GenericDaoHibernateImpl extends HibernateDaoSupport
        implements DAO {

    public Object getById(Class entityClass, Long id) {

        return getHibernateTemplate().get(entityClass, id);
    }

    public void save(Object entity) throws OptimisticLockException {

        try {
            getHibernateTemplate().saveOrUpdate(entity);
            getHibernateTemplate().flush();
        } catch (HibernateOptimisticLockingFailureException e) {
            throw new OptimisticLockException(e);
        }
    }

    public void delete(Object entity) throws ReferenceExistsException, OptimisticLockException {

        try {
            getHibernateTemplate().delete(entity);
            getHibernateTemplate().flush();
        } catch (HibernateOptimisticLockingFailureException e) {
            throw new OptimisticLockException(e);
        }
    }
}
