package de.powerstaff.web.backingbean.freelancer;

import de.mogwai.common.utils.KeyValuePair;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.entity.*;
import de.powerstaff.web.backingbean.PersonEditorBackingBeanDataModel;

import java.util.ArrayList;
import java.util.List;

public class FreelancerBackingBeanDataModel extends
        PersonEditorBackingBeanDataModel<Freelancer> {

    private static final long serialVersionUID = 686254367960000233L;

    private CollectionDataModel<FreelancerProfile> profiles = new CollectionDataModel<FreelancerProfile>();

    private List<KeyValuePair<Integer, String>> status = new ArrayList<KeyValuePair<Integer, String>>();

    private ProjectPosition currentProjectPosition = new ProjectPosition();

    private CollectionDataModel<ProjectPosition> positions = new CollectionDataModel<ProjectPosition>();

    private List<FreelancerToTag> tagsSchwerpunkte = new ArrayList<FreelancerToTag>();
    private List<FreelancerToTag> tagsFunktionen = new ArrayList<FreelancerToTag>();
    private List<FreelancerToTag> tagsEinsatzorte = new ArrayList<FreelancerToTag>();
    private List<FreelancerToTag> tagsBemerkungen = new ArrayList<FreelancerToTag>();

    private Tag newSchwerpunkte;
    private Tag newFunktion;
    private Tag newEinsatzOrt;
    private Tag newBemerkung;

    private Long tagIdSchwerpunkt = null;
    private Long tagIdFunktion = null;
    private Long tagIdEinsatzOrt = null;
    private Long tagIdBemerkung = null;

    public FreelancerBackingBeanDataModel() {
        init();
    }

    public String getStatusAsString(Integer aStatus) {
        for (KeyValuePair<Integer, String> theStatus : status) {
            if (aStatus == null) {
                if (theStatus.getKey() == null) {
                    return theStatus.getValue();
                }
            } else if (aStatus.equals(theStatus.getKey())) {
                return theStatus.getValue();
            }
        }
        return "";
    }

    private void init() {
        status.add(new KeyValuePair<Integer, String>(1, "Angestellter"));
        status.add(new KeyValuePair<Integer, String>(2, "Freiberufler"));
        status.add(new KeyValuePair<Integer, String>(3,
                "Freiberufler + Festanstellung"));
        status.add(new KeyValuePair<Integer, String>(4, "ANÜ"));
        status.add(new KeyValuePair<Integer, String>(5, "Angestellter + ANÜ"));
        status.add(new KeyValuePair<Integer, String>(6, "Freiberufler + ANÜ"));
        status.add(new KeyValuePair<Integer, String>(7,
                "Freiberufler + Festanstellung + ANÜ"));
        status.add(new KeyValuePair<Integer, String>(null, "Undefiniert"));
    }

    @Override
    protected void initialize() {
        setEntity(new Freelancer());
    }

    public CollectionDataModel<FreelancerProfile> getProfiles() {
        return profiles;
    }

    public List getStatus() {
        return status;
    }

    public ProjectPosition getCurrentProjectPosition() {
        return currentProjectPosition;
    }

    public void setCurrentProjectPosition(ProjectPosition currentProjectPosition) {
        this.currentProjectPosition = currentProjectPosition;
    }

    public CollectionDataModel<ProjectPosition> getPositions() {
        return positions;
    }

    public void setPositions(CollectionDataModel<ProjectPosition> positions) {
        this.positions = positions;
    }

    public List<FreelancerToTag> getTagsSchwerpunkte() {
        return tagsSchwerpunkte;
    }

    public List<FreelancerToTag> getTagsFunktionen() {
        return tagsFunktionen;
    }

    public List<FreelancerToTag> getTagsEinsatzorte() {
        return tagsEinsatzorte;
    }

    public List<FreelancerToTag> getTagsBemerkungen() {
        return tagsBemerkungen;
    }

    public Tag getNewSchwerpunkte() {
        return newSchwerpunkte;
    }

    public void setNewSchwerpunkte(Tag newSchwerpunkte) {
        this.newSchwerpunkte = newSchwerpunkte;
    }

    public Tag getNewFunktion() {
        return newFunktion;
    }

    public void setNewFunktion(Tag newFunktion) {
        this.newFunktion = newFunktion;
    }

    public Tag getNewEinsatzOrt() {
        return newEinsatzOrt;
    }

    public void setNewEinsatzOrt(Tag newEinsatzOrt) {
        this.newEinsatzOrt = newEinsatzOrt;
    }

    public Tag getNewBemerkung() {
        return newBemerkung;
    }

    public void setNewBemerkung(Tag newBemerkung) {
        this.newBemerkung = newBemerkung;
    }

    public Long getTagIdSchwerpunkt() {
        return tagIdSchwerpunkt;
    }

    public void setTagIdSchwerpunkt(Long tagIdSchwerpunkt) {
        this.tagIdSchwerpunkt = tagIdSchwerpunkt;
    }

    public Long getTagIdFunktion() {
        return tagIdFunktion;
    }

    public void setTagIdFunktion(Long tagIdFunktion) {
        this.tagIdFunktion = tagIdFunktion;
    }

    public Long getTagIdEinsatzOrt() {
        return tagIdEinsatzOrt;
    }

    public void setTagIdEinsatzOrt(Long tagIdEinsatzOrt) {
        this.tagIdEinsatzOrt = tagIdEinsatzOrt;
    }

    public Long getTagIdBemerkung() {
        return tagIdBemerkung;
    }

    public void setTagIdBemerkung(Long tagIdBemerkung) {
        this.tagIdBemerkung = tagIdBemerkung;
    }
}