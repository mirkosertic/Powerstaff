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

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import de.mogwai.common.dao.DAO;

/**
 * Hibernate Implementation des generischen Data Access Objects.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:25:22 $
 */
public abstract class GenericDaoHibernateImpl extends HibernateDaoSupport implements DAO {

    public Object getById(Class entityClass, Long id) {

        return getHibernateTemplate().get(entityClass, id);
    }

    public void save(Object entity) {

        getHibernateTemplate().saveOrUpdate(entity);
    }

    public void delete(Object entity) {

        getHibernateTemplate().delete(entity);
    }

    /**
     * Wandelt die GUI-Wildcards in SQL Wilcards um.
     * 
     * @param str
     *                unbearbeiteteter String aus dem GUI
     * @return String mit SQL-Wildcards für die Datenbank
     */

    public String wildcardFilter(String str) {
        if (str.indexOf('?') >= 0) {
            str = str.replace('?', '_');
        }
        if (str.indexOf('*') >= 0) {
            str = str.replace('*', '%');
        }
        return str;

    }

}
