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

import java.util.List;

import de.mogwai.common.business.entity.EnumItem;
import de.mogwai.common.business.enums.BaseEnumItemEnum;
import de.mogwai.common.dao.EnumItemDao;

/**
 * Hibernate Implementation des EnumItem Data Access Objects.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:22 $
 */
public class EnumItemDaoHibernateImpl extends GenericDaoHibernateImpl implements EnumItemDao {

    /**
     * @see de.mogwai.common.dao.EnumItemDao#getAll()
     */
    public List getAll() {

        return getHibernateTemplate().find("from EnumItem order by id");
    }

    /**
     * @see de.mogwai.common.dao.EnumItemDao#getById(long)
     */
    public EnumItem getById(long aId) {
        return (EnumItem) getHibernateTemplate().get(EnumItem.class, aId);
    }

    /**
     * @see de.mogwai.common.dao.EnumItemDao#getByEnum(de.mogwai.common.business.enums.BaseEnumItemEnum)
     */
    public EnumItem getByEnum(BaseEnumItemEnum aEnum) {
        return (EnumItem) getById(EnumItem.class, aEnum.getId());
    }

}
