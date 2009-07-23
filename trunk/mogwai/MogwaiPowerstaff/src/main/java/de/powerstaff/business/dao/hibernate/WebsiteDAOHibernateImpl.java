package de.powerstaff.business.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import de.mogwai.common.dao.hibernate.GenericDaoHibernateImpl;
import de.powerstaff.business.dao.WebsiteDAO;
import de.powerstaff.business.entity.NewsletterMail;
import de.powerstaff.business.entity.WebProject;

public class WebsiteDAOHibernateImpl extends GenericDaoHibernateImpl implements WebsiteDAO {

    @Override
    public void delete(final WebProject aProject) {
        getHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session aSession) throws HibernateException, SQLException {
                aSession.delete(aProject);
                return null;
            }
        });
    }

    @Override
    public WebProject getById(final Long aId) {
        return (WebProject) getHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session aSession) throws HibernateException, SQLException {
                return aSession.get(WebProject.class, aId);
            }
        });
    }

    @Override
    public Collection<NewsletterMail> getConfirmedMails() {
        return new ArrayList<NewsletterMail>();
    }

    @Override
    public Collection<WebProject> getCurrentProjects() {
        return (Collection<WebProject>) getHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session aSession) throws HibernateException, SQLException {
                return aSession.createCriteria(WebProject.class).list();
            }
        });
    }

    @Override
    public void saveOrUpdate(final WebProject aProject) {
        getHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session aSession) throws HibernateException, SQLException {
                aSession.saveOrUpdate(aProject);
                return null;
            }
        });        
    }

    @Override
    public void saveOrUpdate(NewsletterMail mail) {
    }

    @Override
    public void save(final WebProject aProject) {
        getHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session aSession) throws HibernateException, SQLException {
                aSession.saveOrUpdate(aProject);
                return null;
            }
        });        
    }
}