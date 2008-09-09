package de.powerstaff.mobile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import de.mogwai.common.logging.Logger;
import de.powerstaff.business.entity.ContactType;
import de.powerstaff.business.entity.Customer;
import de.powerstaff.business.entity.CustomerContact;
import de.powerstaff.business.entity.CustomerHistory;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerContact;
import de.powerstaff.business.entity.FreelancerHistory;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.business.entity.PartnerContact;
import de.powerstaff.business.entity.PartnerHistory;
import de.powerstaff.business.entity.Project;
import de.powerstaff.business.entity.UDFSupport;
import de.powerstaff.business.entity.UserDefinedField;

public class Datenuebernahme {

    private static final Long CT_PHONE_GES = 1L;

    private static final Long CT_FAX_GES = 2L;

    private static final Long CT_MOBIL_GES = 3L;

    private static final Long CT_MAIL_GES = 4L;

    private static final Long CT_WEB_GES = 5L;

    private static final Long CT_PHONE_PRIV = 6L;

    private static final Long CT_FAX_PRIV = 7L;

    private static final Long CT_MOBIL_PRIV = 8L;

    private static final Long CT_MAIL_PRIV = 9L;

    private static final Long CT_WEB_PRIV = 10L;

    private static final Long CT_GULPID = 11L;

    private static final Long CT_GULPNAME = 12L;

    private static final Logger LOGGER = new Logger(Datenuebernahme.class);

    public static void main(String[] args) throws InstantiationException, ClassNotFoundException, SQLException,
            NotSupportedException, SystemException, RollbackException, HeuristicMixedException,
            HeuristicRollbackException, IOException, IllegalAccessException {

        LOGGER.logInfo("Init JDBC");

        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();

        String theFileName = "C:\\Daten\\Arbeit\\Projekte\\MobileConsulting\\Altdaten\\mcdb_DM.mdb";

        String theJDBCURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};;DBQ=" + theFileName;

        LOGGER.logInfo("JDBC URL ist " + theJDBCURL);

        final Connection theConnection = DriverManager.getConnection(theJDBCURL, "", "");

        LOGGER.logInfo("Init Spring");
        ApplicationContext theContext = new ClassPathXmlApplicationContext("applicationContext-uebernahme.xml");
        final SessionFactory theFactory = (SessionFactory) theContext.getBean("sessionFactory");

        PlatformTransactionManager theManager = (PlatformTransactionManager) theContext.getBean("txManager");

        File theCVPath = new File("C:\\Daten\\Arbeit\\Projekte\\MobileConsulting\\CVPath");

        // importMitarbeiter(theFactory, theManager, theConnection, theCVPath);
        // importPartner(theFactory, theManager, theConnection);        
        importKunden(theFactory, theManager, theConnection);
        importProjekte(theFactory, theManager, theConnection);

        theConnection.close();

        LOGGER.logInfo("Finished");
        System.exit(0);

    }

    private static boolean getBoolean(ResultSet aResultSet, String aFieldName) throws SQLException {
        String theValue = getString(aResultSet, aFieldName);
        return "1".equals(theValue);
    }

    private static Timestamp getTimestamp(ResultSet aResultSet, String aFieldName) throws SQLException {
        return aResultSet.getTimestamp(aFieldName);
    }

    private static Date getDate(ResultSet aResultSet, String aFieldName) throws SQLException {
        return aResultSet.getDate(aFieldName);
    }

    private static Long getLong(ResultSet aResultSet, String aFieldName) throws SQLException {
        long theValue = aResultSet.getLong(aFieldName);
        if (theValue == 0) {
            return null;
        }
        return new Long(theValue);
    }

    private static String getLongString(ResultSet aResultSet, String aFieldName) throws SQLException {
        Long theValue = aResultSet.getLong(aFieldName);
        if (theValue == null) {
            return null;
        }
        if (theValue.longValue() == 0) {
            return null;
        }
        return theValue.toString();
    }

    private static String getString(ResultSet aResultSet, String aFieldName) throws SQLException {
        String theValue = aResultSet.getString(aFieldName);
        if (StringUtils.isEmpty(theValue)) {
            return null;
        }
        return theValue;
    }

    private static String getConcatenatedString(ResultSet aResultSet, String... aFieldName) throws SQLException {
        StringBuilder theResult = new StringBuilder();
        for (String theField : aFieldName) {
            String theValue = getString(aResultSet, theField);
            if (theValue != null) {
                if (theResult.length() > 0) {
                    theResult.append(" ");
                }
                theResult.append(theValue);
            }
        }
        if (theResult.length() == 0) {
            return null;
        }
        return theResult.toString();
    }

    private static void resultSetToUDF(ResultSet aResult, UDFSupport aObject) throws SQLException {
        ResultSetMetaData theMeta = aResult.getMetaData();
        for (int i = 1; i < theMeta.getColumnCount(); i++) {
            String theColumnName = theMeta.getColumnName(i);

            if ("s_GUID".equals(theColumnName)) {
                continue;
            }
            if ("s_Generation".equals(theColumnName)) {
                continue;
            }
            if ("s_Lineage".equals(theColumnName)) {
                continue;
            }

            UserDefinedField theField = aObject.getUdf().get(theColumnName);
            if (theField == null) {
                theField = new UserDefinedField();
                aObject.getUdf().put(theColumnName, theField);
            }

            int theType = theMeta.getColumnType(i);
            switch (theType) {
            case Types.BOOLEAN:
            case Types.BIT:
                theField.setBooleanValue(getBoolean(aResult, theColumnName));
                break;
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.TINYINT:
                theField.setIntValue(aResult.getInt(i));
                break;
            case Types.BIGINT:
            case Types.NUMERIC:
                theField.setLongValue(aResult.getLong(i));
                break;
            case Types.DATE:
            case Types.TIMESTAMP:
            case Types.TIME:
                theField.setDateValue(aResult.getDate(i));
                break;
            case Types.CLOB:
            case Types.LONGVARCHAR:
                theField.setLongStringValue(getString(aResult, theColumnName));
                break;
            default:
                theField.setStringValue(getString(aResult, theColumnName));
            }
        }
    }

    private static void importMitarbeiter(final SessionFactory aFactory, final PlatformTransactionManager aManager,
            Connection aConnection, File aCVPath) throws SQLException, IOException {

        LOGGER.logInfo("Import Mitarbeiter");

        Map<String, String> thePersonalMap = new HashMap<String, String>();
        Statement theFindPersonalStatenebt = aConnection.createStatement();
        ResultSet thePersonalResult = theFindPersonalStatenebt.executeQuery("select * from personal");
        while (thePersonalResult.next()) {
            thePersonalMap.put(thePersonalResult.getString("ID"), thePersonalResult.getString("loginName"));
        }
        thePersonalResult.close();
        theFindPersonalStatenebt.close();

        Statement theSelectAdresse = aConnection.createStatement();
        Statement theAdresse = aConnection.createStatement();
        Statement theReadAgainStatement = aConnection.createStatement();

        // Geschäftlich
        ContactType theTelGesContactType = new ContactType();
        theTelGesContactType.setId(CT_PHONE_GES);
        ContactType theFaxGesContactType = new ContactType();
        theFaxGesContactType.setId(CT_FAX_GES);
        ContactType theMobileGesContactType = new ContactType();
        theMobileGesContactType.setId(CT_MOBIL_GES);
        ContactType theMailGesContactType = new ContactType();
        theMailGesContactType.setId(CT_MAIL_GES);
        ContactType theWebGesContactType = new ContactType();
        theWebGesContactType.setId(CT_WEB_GES);

        ContactType theTelPrivatContactType = new ContactType();
        theTelPrivatContactType.setId(CT_PHONE_PRIV);
        ContactType theFaxPrivatContactType = new ContactType();
        theFaxPrivatContactType.setId(CT_FAX_PRIV);
        ContactType theMobilePrivatContactType = new ContactType();
        theMobilePrivatContactType.setId(CT_MOBIL_PRIV);
        ContactType theMailPrivatContactType = new ContactType();
        theMailPrivatContactType.setId(CT_MAIL_PRIV);
        ContactType theWebPrivatContactType = new ContactType();
        theWebPrivatContactType.setId(CT_WEB_PRIV);

        ContactType theGulpIDContactType = new ContactType();
        theGulpIDContactType.setId(CT_GULPID);
        ContactType theGulpNameContactType = new ContactType();
        theGulpNameContactType.setId(CT_GULPNAME);

        ResultSet theMitarbeiterResult = theSelectAdresse.executeQuery("select * from mitarbeiter");
        long theCounter = 0;
        while (theMitarbeiterResult.next()) {
            theCounter++;

            long theMitarbeiterID = theMitarbeiterResult.getLong("ID");

            LOGGER.logInfo("Verarbeite " + theCounter + "-> " + theMitarbeiterID);

            // Freiberufler
            final Freelancer theFreelancer = new Freelancer();
            theFreelancer.setCreationDate(getTimestamp(theMitarbeiterResult, "cvEing"));
            theFreelancer.setCreationUserID(thePersonalMap.get(getString(theMitarbeiterResult, "erstPersID")));
            theFreelancer.setLastModificationDate(getTimestamp(theMitarbeiterResult, "modifdatum"));
            theFreelancer.setLastModificationUserID(thePersonalMap.get(getString(theMitarbeiterResult, "modifPersID")));

            theFreelancer.setName1(getString(theMitarbeiterResult, "name"));
            theFreelancer.setName2(getString(theMitarbeiterResult, "vorname"));
            theFreelancer.setContactforbidden(getBoolean(theMitarbeiterResult, "passiv"));
            theFreelancer.setComments(getConcatenatedString(theMitarbeiterResult, "notiz", "notiz2", "bewertung"));

            theFreelancer.setWorkplace(getConcatenatedString(theMitarbeiterResult, "wunschort", "wunschlandPLZ"));
            theFreelancer.setAvailabilityAsDate(getDate(theMitarbeiterResult, "verfvon"));
            theFreelancer.setSallaryLong(getLong(theMitarbeiterResult, "satz"));
            theFreelancer.setCode(getString(theMitarbeiterResult, "cvNr"));
            theFreelancer.setSkills(getString(theMitarbeiterResult, "kurzskill"));
            theFreelancer.setGulpID(getLongString(theMitarbeiterResult, "gulpId"));
            theFreelancer.setGeburtsdatum(getString(theMitarbeiterResult, "gebDatum"));

            int theStatus = 0;
            if (getBoolean(theMitarbeiterResult, "freimit")) {
                theStatus += 2;
            }
            if (getBoolean(theMitarbeiterResult, "festmit")) {
                theStatus += 1;
            }
            if (getBoolean(theMitarbeiterResult, "anu")) {
                theStatus += 4;
            }
            if (theStatus > 0) {
                theFreelancer.setStatus(new Integer(theStatus));
            }

            ResultSet theAdresseResult = theAdresse.executeQuery("select * from mitarbeiter_adresse where mitArbID = "
                    + theMitarbeiterID);
            while (theAdresseResult.next()) {

                boolean theMaster = getBoolean(theAdresseResult, "master");
                if (theMaster) {
                    theFreelancer.setCompany(getString(theAdresseResult, "firma"));
                    theFreelancer.setStreet(getString(theAdresseResult, "strasse"));
                    theFreelancer.setCountry(getString(theAdresseResult, "land"));
                    theFreelancer.setPlz(getString(theAdresseResult, "plz"));
                    theFreelancer.setCity(getString(theAdresseResult, "ort"));
                }

                String theValue = getString(theAdresseResult, "tel1");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theMaster ? theTelGesContactType : theTelPrivatContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "tel2");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theMaster ? theTelGesContactType : theTelPrivatContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "mobiltel");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theMaster ? theMobileGesContactType : theMobilePrivatContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "fax");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theMaster ? theFaxGesContactType : theFaxPrivatContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "email1");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theMaster ? theMailGesContactType : theMailPrivatContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "email2");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theMaster ? theMailGesContactType : theMailPrivatContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "web");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theMaster ? theWebGesContactType : theMailPrivatContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }

            }
            theAdresseResult.close();

            String theGulpId = theFreelancer.getGulpID();
            if (!StringUtils.isEmpty(theGulpId)) {
                FreelancerContact theContact = new FreelancerContact();
                theContact.setType(theGulpIDContactType);
                theContact.setValue(theGulpId);
                theFreelancer.getContacts().add(theContact);
            }

            ResultSet theHistoryResult = theAdresse.executeQuery("select * from mitarbeiter_kontakte where mitArbID = "
                    + theMitarbeiterID);
            while (theHistoryResult.next()) {

                String theID = getString(theHistoryResult, "ID");
                if (!"1109917714".equals(theID)) {
                    LOGGER.logInfo("Verarbeitung " + theID);

                    FreelancerHistory theHistory = new FreelancerHistory();

                    theHistory.setCreationDate(getTimestamp(theHistoryResult, "datum"));
                    theHistory.setCreationUserID(getString(theHistoryResult, "person"));
                    theHistory.setDescription(getString(theHistoryResult, "typ") + "\n"
                            + getString(theHistoryResult, "notiz"));

                    theFreelancer.getHistory().add(theHistory);
                }
            }
            theHistoryResult.close();

            ResultSet theReadAgain = theReadAgainStatement.executeQuery("select * from mitarbeiter where ID = "
                    + theMitarbeiterID);
            theReadAgain.next();

            String theGulpName = getString(theReadAgain, "gulpName");
            if (!StringUtils.isEmpty(theGulpName)) {
                FreelancerContact theContact = new FreelancerContact();
                theContact.setType(theGulpNameContactType);
                theContact.setValue(theGulpName);
                theFreelancer.getContacts().add(theContact);
            }

            resultSetToUDF(theReadAgain, theFreelancer);
            theReadAgain.close();

            // CV Generieren
            UserDefinedField theField = theFreelancer.getUdf().get("projekt");

            if (!StringUtils.isEmpty(theFreelancer.getCode())) {
                if (!StringUtils.isEmpty(theField.getLongStringValue())) {
                    File theCV = new File(aCVPath, "Profil " + theFreelancer.getCode() + ".txt");
                    FileWriter theWriter = new FileWriter(theCV);

                    theWriter.write(theField.getLongStringValue());
                    theWriter.close();

                    if (theFreelancer.getCreationDate() != null) {
                        theCV.setLastModified(theFreelancer.getCreationDate().getTime());
                    }
                }
            }

            theFreelancer.getUdf().remove("projekt");

            /*
             * DefaultTransactionDefinition theDefinition = new
             * DefaultTransactionDefinition(); theDefinition.setName("atx");
             * theDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
             * TransactionStatus theTransaction =
             * aManager.getTransaction(theDefinition); Session theSession =
             * aFactory.openSession(); try { theSession.save(theFreelancer);
             * theSession.flush(); theSession.close();
             * aManager.commit(theTransaction); } catch (Exception e) {
             * LOGGER.logError("Fehler beim Import", e);
             * theTransaction.setRollbackOnly();
             * aManager.rollback(theTransaction); }
             */
        }
        theMitarbeiterResult.close();

        theSelectAdresse.close();
        theAdresse.close();

    }

    private static void importKunden(final SessionFactory aFactory, final PlatformTransactionManager aManager,
            Connection aConnection) throws SQLException, IOException {

        LOGGER.logInfo("Import Kunden");

        Map<String, String> thePersonalMap = new HashMap<String, String>();
        Statement theFindPersonalStatenebt = aConnection.createStatement();
        ResultSet thePersonalResult = theFindPersonalStatenebt.executeQuery("select * from personal");
        while (thePersonalResult.next()) {
            thePersonalMap.put(thePersonalResult.getString("ID"), thePersonalResult.getString("loginName"));
        }
        thePersonalResult.close();
        theFindPersonalStatenebt.close();

        Map<Integer, String> theLandMap = new HashMap<Integer, String>();
        Statement theFindLandStatenebt = aConnection.createStatement();
        ResultSet theFindLandResult = theFindLandStatenebt.executeQuery("select * from land");
        while (theFindLandResult.next()) {
            theLandMap.put(theFindLandResult.getInt("ID"), theFindLandResult.getString("landKuerzel"));
        }
        theFindLandResult.close();
        theFindLandStatenebt.close();

        Statement theKundenSelect = aConnection.createStatement();
        Statement theAdresse = aConnection.createStatement();
        Statement theHistoryStatement = aConnection.createStatement();
        Statement theReadAgainStatement = aConnection.createStatement();

        ContactType theTelContactType = new ContactType();
        theTelContactType.setId(CT_PHONE_GES);
        ContactType theFaxContactType = new ContactType();
        theFaxContactType.setId(CT_FAX_GES);
        ContactType theMobileContactType = new ContactType();
        theMobileContactType.setId(CT_MOBIL_GES);
        ContactType theMailContactType = new ContactType();
        theMailContactType.setId(CT_MAIL_GES);
        ContactType theWebContactType = new ContactType();
        theWebContactType.setId(CT_WEB_GES);

        ResultSet theKundenResult = theKundenSelect.executeQuery("select * from kunde");
        long theCounter = 0;
        while (theKundenResult.next()) {
            theCounter++;

            Customer theRootCustomer = new Customer();
            resultSetToUDF(theKundenResult, theRootCustomer);

            String theKundenId = theRootCustomer.getUdf().get("kunde").getStringValue();

            LOGGER.logInfo("Verarbeite " + theCounter + "-> " + theKundenId);

            ResultSet theKontakte = theAdresse.executeQuery("select * from ansprechpartner where kunde = '"
                    + theKundenId + "'");
            while (theKontakte.next()) {

                long theId = theKontakte.getLong("ID");
                Customer theCustomer = new Customer();

                // Wurzeldaten kopieren
                theCustomer.setCompany(theRootCustomer.getUdf().get("firma").getStringValue());
                theCustomer.setStreet(theRootCustomer.getUdf().get("strasse").getStringValue());
                theCustomer.setPlz(theRootCustomer.getUdf().get("plz").getStringValue());
                theCustomer.setCity(theRootCustomer.getUdf().get("ort").getStringValue());

                UserDefinedField theLandField = theRootCustomer.getUdf().get("landID");
                if (theLandField != null) {
                    theCustomer.setCountry(theLandMap.get(theLandField.getIntValue()));
                }

                String theValue = theRootCustomer.getUdf().get("tel1").getStringValue();
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theTelContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }
                theValue = theRootCustomer.getUdf().get("tel2").getStringValue();
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theTelContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }
                theValue = theRootCustomer.getUdf().get("fax").getStringValue();
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theFaxContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }
                theValue = theRootCustomer.getUdf().get("email").getStringValue();
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theMailContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }
                theValue = theRootCustomer.getUdf().get("url").getStringValue();
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theWebContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }

                theCustomer.setName1(getString(theKontakte, "name"));
                theCustomer.setName2(getString(theKontakte, "vorname"));

                theValue = getString(theKontakte, "tel1");
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theTelContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }
                theValue = getString(theKontakte, "tel2");
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theTelContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }
                theValue = getString(theKontakte, "mobiltel");
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theMobileContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }
                theValue = getString(theKontakte, "fax");
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theFaxContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }
                theValue = getString(theKontakte, "email1");
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theMailContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }
                theValue = getString(theKontakte, "email2");
                if (!(theValue == null)) {
                    CustomerContact theContact = new CustomerContact();
                    theContact.setType(theMailContactType);
                    theContact.setValue(theValue);
                    theCustomer.getContacts().add(theContact);
                }

                theCustomer.setCreationDate(getTimestamp(theKontakte, "erstdatum"));
                theCustomer.setCreationUserID(thePersonalMap.get(getString(theKontakte, "erstPersID")));
                theCustomer.setLastModificationDate(getTimestamp(theKontakte, "modifdatum"));
                theCustomer.setLastModificationUserID(thePersonalMap.get(getString(theKontakte, "modifPersID")));

                ResultSet theReadAgain = theReadAgainStatement.executeQuery("select * from ansprechpartner where ID = "
                        + theId);
                theReadAgain.next();
                resultSetToUDF(theReadAgain, theCustomer);
                theReadAgain.close();

                ResultSet theHistoryResult = theHistoryStatement
                        .executeQuery("select * from ansp_kontakte where anspID = " + theId);
                while (theHistoryResult.next()) {

                    String theID = getString(theHistoryResult, "ID");
                    LOGGER.logInfo("Verarbeitung " + theID);

                    CustomerHistory theHistory = new CustomerHistory();

                    theHistory.setCreationDate(getTimestamp(theHistoryResult, "datum"));
                    theHistory.setCreationUserID(getString(theHistoryResult, "person"));
                    theHistory.setDescription(getString(theHistoryResult, "typ") + "\n"
                            + getString(theHistoryResult, "notiz"));

                    theCustomer.getHistory().add(theHistory);
                }
                theHistoryResult.close();

                DefaultTransactionDefinition theDefinition = new DefaultTransactionDefinition();
                theDefinition.setName("atx");
                theDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                TransactionStatus theTransaction = aManager.getTransaction(theDefinition);
                Session theSession = aFactory.openSession();
                try {
                    theSession.save(theCustomer);
                    theSession.flush();
                    theSession.close();
                    aManager.commit(theTransaction);
                } catch (Exception e) {
                    LOGGER.logError("Fehler beim Import", e);
                    theTransaction.setRollbackOnly();
                    aManager.rollback(theTransaction);
                }
            }
        }
        theKundenResult.close();

        theKundenSelect.close();
        theAdresse.close();

    }

    private static void importPartner(final SessionFactory aFactory, final PlatformTransactionManager aManager,
            Connection aConnection) throws SQLException, IOException {

        LOGGER.logInfo("Import Partner");

        Map<String, String> thePersonalMap = new HashMap<String, String>();
        Statement theFindPersonalStatenebt = aConnection.createStatement();
        ResultSet thePersonalResult = theFindPersonalStatenebt.executeQuery("select * from personal");
        while (thePersonalResult.next()) {
            thePersonalMap.put(thePersonalResult.getString("ID"), thePersonalResult.getString("loginName"));
        }
        thePersonalResult.close();
        theFindPersonalStatenebt.close();

        Map<Integer, String> theLandMap = new HashMap<Integer, String>();
        Statement theFindLandStatenebt = aConnection.createStatement();
        ResultSet theFindLandResult = theFindLandStatenebt.executeQuery("select * from land");
        while (theFindLandResult.next()) {
            theLandMap.put(theFindLandResult.getInt("ID"), theFindLandResult.getString("landKuerzel"));
        }
        theFindLandResult.close();
        theFindLandStatenebt.close();

        Statement theKundenSelect = aConnection.createStatement();
        Statement theAdresse = aConnection.createStatement();
        Statement theHistoryStatement = aConnection.createStatement();
        Statement theReadAgainStatement = aConnection.createStatement();

        ContactType theTelContactType = new ContactType();
        theTelContactType.setId(CT_PHONE_GES);
        ContactType theFaxContactType = new ContactType();
        theFaxContactType.setId(CT_FAX_GES);
        ContactType theMobileContactType = new ContactType();
        theMobileContactType.setId(CT_MOBIL_GES);
        ContactType theMailContactType = new ContactType();
        theMailContactType.setId(CT_MAIL_GES);
        ContactType theWebContactType = new ContactType();
        theWebContactType.setId(CT_WEB_GES);

        ResultSet theKundenResult = theKundenSelect.executeQuery("select * from kunde where agentur = 0");
        long theCounter = 0;
        while (theKundenResult.next()) {
            theCounter++;

            Partner theRootCustomer = new Partner();
            resultSetToUDF(theKundenResult, theRootCustomer);

            String theKundenId = theRootCustomer.getUdf().get("kunde").getStringValue();

            LOGGER.logInfo("Verarbeite " + theCounter + "-> " + theKundenId);

            ResultSet theKontakte = theAdresse.executeQuery("select * from ansprechpartner where kunde = '"
                    + theKundenId + "'");
            while (theKontakte.next()) {

                long theId = theKontakte.getLong("ID");
                Partner thePartner = new Partner();

                // Wurzeldaten kopieren
                thePartner.setCompany(theRootCustomer.getUdf().get("firma").getStringValue());
                thePartner.setStreet(theRootCustomer.getUdf().get("strasse").getStringValue());
                thePartner.setPlz(theRootCustomer.getUdf().get("plz").getStringValue());
                thePartner.setCity(theRootCustomer.getUdf().get("ort").getStringValue());

                UserDefinedField theLandField = theRootCustomer.getUdf().get("landID");
                if (theLandField != null) {
                    thePartner.setCountry(theLandMap.get(theLandField.getIntValue()));
                }

                String theValue = theRootCustomer.getUdf().get("tel1").getStringValue();
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theTelContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }
                theValue = theRootCustomer.getUdf().get("tel2").getStringValue();
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theTelContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }
                theValue = theRootCustomer.getUdf().get("fax").getStringValue();
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theFaxContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }
                theValue = theRootCustomer.getUdf().get("email").getStringValue();
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theMailContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }
                theValue = theRootCustomer.getUdf().get("url").getStringValue();
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theWebContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }

                thePartner.setName1(getString(theKontakte, "name"));
                thePartner.setName2(getString(theKontakte, "vorname"));

                theValue = getString(theKontakte, "tel1");
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theTelContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }
                theValue = getString(theKontakte, "tel2");
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theTelContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }
                theValue = getString(theKontakte, "mobiltel");
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theMobileContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }
                theValue = getString(theKontakte, "fax");
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theFaxContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }
                theValue = getString(theKontakte, "email1");
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theMailContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }
                theValue = getString(theKontakte, "email2");
                if (!(theValue == null)) {
                    PartnerContact theContact = new PartnerContact();
                    theContact.setType(theMailContactType);
                    theContact.setValue(theValue);
                    thePartner.getContacts().add(theContact);
                }

                thePartner.setCreationDate(getTimestamp(theKontakte, "erstdatum"));
                thePartner.setCreationUserID(thePersonalMap.get(getString(theKontakte, "erstPersID")));
                thePartner.setLastModificationDate(getTimestamp(theKontakte, "modifdatum"));
                thePartner.setLastModificationUserID(thePersonalMap.get(getString(theKontakte, "modifPersID")));

                ResultSet theReadAgain = theReadAgainStatement.executeQuery("select * from ansprechpartner where ID = "
                        + theId);
                theReadAgain.next();
                resultSetToUDF(theReadAgain, thePartner);
                theReadAgain.close();

                ResultSet theHistoryResult = theHistoryStatement
                        .executeQuery("select * from ansp_kontakte where anspID = " + theId);
                while (theHistoryResult.next()) {

                    String theID = getString(theHistoryResult, "ID");
                    LOGGER.logInfo("Verarbeitung " + theID);

                    PartnerHistory theHistory = new PartnerHistory();

                    theHistory.setCreationDate(getTimestamp(theHistoryResult, "datum"));
                    theHistory.setCreationUserID(getString(theHistoryResult, "person"));
                    theHistory.setDescription(getString(theHistoryResult, "typ") + "\n"
                            + getString(theHistoryResult, "notiz"));

                    thePartner.getHistory().add(theHistory);
                }
                theHistoryResult.close();

                DefaultTransactionDefinition theDefinition = new DefaultTransactionDefinition();
                theDefinition.setName("atx");
                theDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
                TransactionStatus theTransaction = aManager.getTransaction(theDefinition);
                Session theSession = aFactory.openSession();
                try {
                    theSession.save(thePartner);
                    theSession.flush();
                    theSession.close();
                    aManager.commit(theTransaction);
                } catch (Exception e) {
                    LOGGER.logError("Fehler beim Import", e);
                    theTransaction.setRollbackOnly();
                    aManager.rollback(theTransaction);
                }
            }
        }
        theKundenResult.close();

        theKundenSelect.close();
        theAdresse.close();

    }

    private static void importProjekte(final SessionFactory aFactory, final PlatformTransactionManager aManager,
            Connection aConnection) throws SQLException, IOException {

        LOGGER.logInfo("Import Projekte");

        Map<Integer, String> theLandMap = new HashMap<Integer, String>();
        Statement theFindLandStatenebt = aConnection.createStatement();
        ResultSet theFindLandResult = theFindLandStatenebt.executeQuery("select * from land");
        while (theFindLandResult.next()) {
            theLandMap.put(theFindLandResult.getInt("ID"), theFindLandResult.getString("landKuerzel"));
        }
        theFindLandResult.close();
        theFindLandStatenebt.close();

        Map<String, String> thePersonalMap = new HashMap<String, String>();
        Statement theFindPersonalStatenebt = aConnection.createStatement();
        ResultSet thePersonalResult = theFindPersonalStatenebt.executeQuery("select * from personal");
        while (thePersonalResult.next()) {
            thePersonalMap.put(thePersonalResult.getString("ID"), thePersonalResult.getString("loginName"));
        }
        thePersonalResult.close();
        theFindPersonalStatenebt.close();

        ContactType theTelContactType = new ContactType();
        theTelContactType.setId(CT_PHONE_GES);
        ContactType theFaxContactType = new ContactType();
        theFaxContactType.setId(CT_FAX_GES);
        ContactType theMobileContactType = new ContactType();
        theMobileContactType.setId(CT_MOBIL_GES);
        ContactType theMailContactType = new ContactType();
        theMailContactType.setId(CT_MAIL_GES);
        ContactType theWebContactType = new ContactType();
        theWebContactType.setId(CT_WEB_GES);

        Statement theProjectStatement = aConnection.createStatement();
        Statement theReadAgainStatement = aConnection.createStatement();
        ResultSet theProjectResult = theProjectStatement
                .executeQuery("select * from projekt where (prNr like 'MS%') or ((prNr like 'TD%'))");
        
        long theCounter = 0;

        Map<String, Customer> theDummies = new HashMap<String, Customer>();

        while (theProjectResult.next()) {
            theCounter++;

            long theProjectId = theProjectResult.getLong("ID");

            LOGGER.logInfo("Verarbeite " + theCounter + "-> " + theProjectId);

            Project theProject = new Project();
            theProject.setEntryDate(getDate(theProjectResult, "eingDatum"));
            theProject.setProjectNumber(getString(theProjectResult, "prNr"));
            theProject.setWorkplace(getConcatenatedString(theProjectResult, "land", "plz", "ort"));
            theProject.setStartDate(getDate(theProjectResult, "startDatum"));
            theProject.setDuration(getConcatenatedString(theProjectResult, "dauer", "dauerEinheit"));
            theProject.setDescriptionShort(getString(theProjectResult, "prTitel"));
            theProject.setDescriptionLong(getString(theProjectResult, "aufgabe"));
            theProject.setStundensatzVK(getLong(theProjectResult, "satz"));

            String theSkills = getString(theProjectResult, "sonst_must");
            String theSkills2 = getString(theProjectResult, "sonst_nice");
            if (!StringUtils.isEmpty(theSkills2)) {
                if (StringUtils.isEmpty(theSkills)) {
                    theSkills = theSkills2;
                } else {
                    theSkills += "\n" + theSkills2;
                }
            }
            theProject.setSkills(theSkills);

            String theStatus = getString(theProjectResult, "status");
            theProject.setStatus(2);
            if ("1".equals(theStatus)) {
                theProject.setStatus(1);
            }
            if ("2".equals(theStatus)) {
                theProject.setStatus(4);
            }
            if ("3".equals(theStatus)) {
                theProject.setStatus(3);
            }
            if ("4".equals(theStatus)) {
                theProject.setStatus(2);
            }
            if ("988965646".equals(theStatus)) {
                theProject.setStatus(5);
            }

            theProject.setCreationDate(getTimestamp(theProjectResult, "erstdatum"));
            theProject.setCreationUserID(thePersonalMap.get(getString(theProjectResult, "erstPersID")));
            theProject.setLastModificationDate(getTimestamp(theProjectResult, "modifdatum"));
            theProject.setLastModificationUserID(thePersonalMap.get(getString(theProjectResult, "modifPersID")));

            ResultSet theReadAgain = theReadAgainStatement.executeQuery("select * from projekt where ID = "
                    + theProjectId);
            theReadAgain.next();
            resultSetToUDF(theReadAgain, theProject);
            theReadAgain.close();

            String thePLID = getString(theProjectResult, "anspLeitID");
            if (thePLID == null) {
                thePLID = getString(theProjectResult, "ansEinkID");
            }

            DefaultTransactionDefinition theDefinition = new DefaultTransactionDefinition();
            theDefinition.setName("atx");
            theDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus theTransaction = aManager.getTransaction(theDefinition);
            Session theSession = aFactory.openSession();
            try {
                if (thePLID != null) {
                    // Kunden suchen
                    Query theQuery = theSession
                            .createQuery("from Customer item join item.udf u where u.intValue = :id");
                    int theID = Integer.parseInt(thePLID);
                    theQuery.setInteger("id", theID);
                    for (Object theObject : theQuery.list()) {
                        Customer theCustomer = (Customer) theObject;
                        if (theCustomer.getUdf().get("ID").getIntValue() == theID) {
                            theProject.setCustomer(theCustomer);
                        }
                    }
                } else {
                    String theKunde = theProject.getUdf().get("kunde").getStringValue();
                    Customer theDummyCustomer = theDummies.get(theKunde);
                    if (theDummyCustomer == null) {

                        Statement theCustomerStatement = aConnection.createStatement();
                        ResultSet theCustomerResult = theCustomerStatement
                                .executeQuery("select * from kunde where kunde = '" + theKunde + "'");
                        while (theCustomerResult.next()) {

                            theDummyCustomer = new Customer();

                            theDummyCustomer.setName1("Unbekannt");
                            theDummyCustomer.setName2("Unbekannt");
                            theDummyCustomer.setCompany(getString(theCustomerResult, "firma"));
                            theDummyCustomer.setStreet(getString(theCustomerResult, "strasse"));
                            theDummyCustomer.setPlz(getString(theCustomerResult, "plz"));
                            theDummyCustomer.setCity(getString(theCustomerResult, "ort"));

                            String theLandField = getString(theCustomerResult, "landID");
                            if (theLandField != null) {
                                theDummyCustomer.setCountry(theLandMap.get(Integer.parseInt(theLandField)));
                            }

                            String theValue = getString(theCustomerResult, "tel1");
                            if (!(theValue == null)) {
                                CustomerContact theContact = new CustomerContact();
                                theContact.setType(theTelContactType);
                                theContact.setValue(theValue);
                                theDummyCustomer.getContacts().add(theContact);
                            }
                            theValue = getString(theCustomerResult, "tel2");
                            if (!(theValue == null)) {
                                CustomerContact theContact = new CustomerContact();
                                theContact.setType(theTelContactType);
                                theContact.setValue(theValue);
                                theDummyCustomer.getContacts().add(theContact);
                            }
                            theValue = getString(theCustomerResult, "fax");
                            if (!(theValue == null)) {
                                CustomerContact theContact = new CustomerContact();
                                theContact.setType(theFaxContactType);
                                theContact.setValue(theValue);
                                theDummyCustomer.getContacts().add(theContact);
                            }
                            theValue = getString(theCustomerResult, "email");
                            if (!(theValue == null)) {
                                CustomerContact theContact = new CustomerContact();
                                theContact.setType(theMailContactType);
                                theContact.setValue(theValue);
                                theDummyCustomer.getContacts().add(theContact);
                            }
                            theValue = getString(theCustomerResult, "url");
                            if (!(theValue == null)) {
                                CustomerContact theContact = new CustomerContact();
                                theContact.setType(theWebContactType);
                                theContact.setValue(theValue);
                                theDummyCustomer.getContacts().add(theContact);
                            }
                        }
                        theDummies.put(theKunde, theDummyCustomer);
                        theSession.save(theDummyCustomer);
                    }
                    theProject.setCustomer(theDummyCustomer);
                }

                if (theProject.getCustomer() != null) {
                    theSession.save(theProject);
                    theSession.flush();
                    theSession.close();
                    aManager.commit(theTransaction);
                }
            } catch (Exception e) {
                LOGGER.logError("Fehler beim Import", e);
                theTransaction.setRollbackOnly();
                aManager.rollback(theTransaction);
            }
        }
        theProjectResult.close();

        theProjectStatement.close();
    }

}
