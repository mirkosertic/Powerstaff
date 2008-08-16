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
import java.util.HashMap;
import java.util.Map;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.commons.lang.StringUtils;
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
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerContact;
import de.powerstaff.business.entity.FreelancerHistory;
import de.powerstaff.business.entity.UDFSupport;
import de.powerstaff.business.entity.UserDefinedField;

public class Datenuebernahme {

    private static final Long CT_PHONE = 1L;

    private static final Long CT_FAX = 2L;

    private static final Long CT_MOBIL = 3L;

    private static final Long CT_MAIL = 4L;

    private static final Long CT_WEB = 5L;

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

        File theCVPath = new File("c:\\Temp\\CVPath");
        importMitarbeiter(theFactory, theManager, theConnection, theCVPath);

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

        ContactType theTelContactType = new ContactType();
        theTelContactType.setId(CT_PHONE);
        ContactType theFaxContactType = new ContactType();
        theFaxContactType.setId(CT_FAX);
        ContactType theMobileContactType = new ContactType();
        theMobileContactType.setId(CT_MOBIL);
        ContactType theMailContactType = new ContactType();
        theMailContactType.setId(CT_MAIL);
        ContactType theWebContactType = new ContactType();
        theWebContactType.setId(CT_WEB);

        ResultSet theMitarbeiterResult = theSelectAdresse.executeQuery("select * from mitarbeiter");
        long theCounter = 0;
        while (theMitarbeiterResult.next()) {
            theCounter++;

            long theMitarbeiterID = theMitarbeiterResult.getLong("ID");

            LOGGER.logInfo("Verarbeite " + theCounter + "-> " + theMitarbeiterID);

            // Freiberufler
            final Freelancer theFreelancer = new Freelancer();
            theFreelancer.setCreationDate(getTimestamp(theMitarbeiterResult, "erstdatum"));
            theFreelancer.setCreationUserID(thePersonalMap.get(getString(theMitarbeiterResult, "erstPersID")));
            theFreelancer.setLastModificationDate(getTimestamp(theMitarbeiterResult, "modifdatum"));
            theFreelancer.setLastModificationUserID(thePersonalMap.get(getString(theMitarbeiterResult, "modifPersID")));

            theFreelancer.setName1(getString(theMitarbeiterResult, "name"));
            theFreelancer.setName2(getString(theMitarbeiterResult, "vorname"));
            theFreelancer.setContactforbidden(getBoolean(theMitarbeiterResult, "passiv"));
            theFreelancer.setComments(getConcatenatedString(theMitarbeiterResult, "notiz", "notiz2"));

            theFreelancer.setWorkplace(getConcatenatedString(theMitarbeiterResult, "wunschort", "wunschlandPLZ"));
            theFreelancer.setAvailability(getString(theMitarbeiterResult, "verfvon"));
            theFreelancer.setSallary(getConcatenatedString(theMitarbeiterResult, "satz", "satzInfo"));
            theFreelancer.setCode(getString(theMitarbeiterResult, "cvNr"));
            theFreelancer.setSkills(getString(theMitarbeiterResult, "kurzskill"));
            theFreelancer.setSallary(getConcatenatedString(theMitarbeiterResult, "gulpId", "gulpName"));

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
                    theContact.setType(theTelContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "tel2");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theTelContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "mobiltel");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theMobileContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "fax");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theFaxContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "email1");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theMailContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "email2");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theMailContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }
                theValue = getString(theAdresseResult, "web");
                if (!(theValue == null)) {
                    FreelancerContact theContact = new FreelancerContact();
                    theContact.setType(theWebContactType);
                    theContact.setValue(theValue);
                    theFreelancer.getContacts().add(theContact);
                }

            }
            theAdresseResult.close();

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
                }
            }
            theFreelancer.getUdf().remove("projekt");

            DefaultTransactionDefinition theDefinition = new DefaultTransactionDefinition();
            theDefinition.setName("atx");
            theDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus theTransaction = aManager.getTransaction(theDefinition);
            Session theSession = aFactory.openSession();
            try {
                theSession.save(theFreelancer);
                theSession.flush();
                theSession.close();
                aManager.commit(theTransaction);
            } catch (Exception e) {
                LOGGER.logError("Fehler beim Import", e);
                theTransaction.setRollbackOnly();
                aManager.rollback(theTransaction);
            }
        }
        theMitarbeiterResult.close();

        theSelectAdresse.close();
        theAdresse.close();

    }
}
