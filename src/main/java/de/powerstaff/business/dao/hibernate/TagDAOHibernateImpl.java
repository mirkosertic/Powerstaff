package de.powerstaff.business.dao.hibernate;

import de.mogwai.common.dao.hibernate.GenericDaoHibernateImpl;
import de.powerstaff.business.dao.TagDAO;
import de.powerstaff.business.entity.Tag;
import de.powerstaff.business.entity.TagType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDAOHibernateImpl extends GenericDaoHibernateImpl implements TagDAO {

    @Override
    public List<Tag> findTagSuggestion(final String aSuggest, final TagType aTagType) {
        return (List<Tag>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session aSession) throws SQLException {

                List<Tag> theResult = new ArrayList<Tag>();

                Criteria theCriteria = aSession.createCriteria(Tag.class);
                theCriteria.add(Restrictions.ilike("name", aSuggest.toLowerCase() + "%"));
                theCriteria.add(Restrictions.eq("type", aTagType));
                theResult.addAll(theCriteria.list());

                return theResult;
            }

        });
    }
}
