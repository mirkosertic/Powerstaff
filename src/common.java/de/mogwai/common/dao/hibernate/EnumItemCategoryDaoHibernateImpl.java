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

import de.mogwai.common.business.entity.EnumItemCategory;
import de.mogwai.common.business.enums.BaseEnumItemCategoryEnum;
import de.mogwai.common.dao.EnumItemCategoryDao;

/**
 * Hibernate Implementation des EnumItemCategory Data Access Objects.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:12:36 $
 */
public class EnumItemCategoryDaoHibernateImpl extends GenericDaoHibernateImpl implements EnumItemCategoryDao {

    /**
     * @see de.mogwai.common.dao.EnumItemCategoryDao#getAll()
     */
    public List<EnumItemCategory> getAll() {

        return getHibernateTemplate().find("from EnumItemCategory order by id");
    }

    /**
     * @see de.mogwai.common.dao.EnumItemCategoryDao#getUserCategories()
     */
    public List<EnumItemCategory> getUserCategories() {

        return getHibernateTemplate().find("from EnumItemCategory item where item.system = false order by id");

    }

    /**
     * @see de.mogwai.common.dao.EnumItemCategoryDao#getByEnum(de.mogwai.common.business.enums.BaseEnumItemCategoryEnum)
     */
    public EnumItemCategory getByEnum(BaseEnumItemCategoryEnum aEnum) {
        return (EnumItemCategory) getById(EnumItemCategory.class, aEnum.getId());
    }
}
