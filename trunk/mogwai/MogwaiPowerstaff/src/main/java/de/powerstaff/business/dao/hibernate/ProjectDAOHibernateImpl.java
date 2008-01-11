/**
 * Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.business.dao.hibernate;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.powerstaff.business.dao.ProjectDAO;
import de.powerstaff.business.entity.Project;

public class ProjectDAOHibernateImpl extends NavigatingDAOHibernateImpl<Project> implements ProjectDAO {

	@Override
	protected Project createNew() {
		return new Project();
	}

	@Override
	protected Class getEntityClass() {
		return Project.class;
	}

	public List<Project> performQBESearch(Project aObject) {
		
		String[] theSearchProperties = new String[] { "date", "projectNumber", "workplace", "start", "duration", "descriptionShort", "descriptionLong" };

		String[] theOrderByProperties = new String[] { "projectNumber" };
		
		return performQBESearch(aObject, theSearchProperties, theOrderByProperties, MATCH_LIKE);
	}

	public Project findByPrimaryKey(Long aProjectID) {
		return (Project)getHibernateTemplate().get(Project.class, aProjectID);
	}

	public List<Project> getActiveProjects() {
		return (List<Project>) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session aSession) throws HibernateException, SQLException {
				
				List<Project> theResult = new Vector<Project>();
				
				Criteria theCriteria = aSession.createCriteria(Project.class);
				theCriteria.add(Expression.eq("visibleOnWebSite", Boolean.TRUE));
				theResult.addAll(theCriteria.list());
				
				return theResult;
			}
			
		});	}
}
