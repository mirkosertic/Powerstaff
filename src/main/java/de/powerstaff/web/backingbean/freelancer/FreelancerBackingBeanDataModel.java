package de.powerstaff.web.backingbean.freelancer;

import de.mogwai.common.utils.KeyValuePair;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.entity.FreelancerToTag;
import de.powerstaff.business.entity.ProjectPosition;
import de.powerstaff.business.entity.Tag;
import de.powerstaff.web.backingbean.PersonEditorBackingBeanDataModel;
import de.powerstaff.web.backingbean.TagSelectionState;

import java.util.ArrayList;
import java.util.List;

public class FreelancerBackingBeanDataModel extends
        PersonEditorBackingBeanDataModel<Freelancer> {

    private static final long serialVersionUID = 686254367960000233L;

    private final CollectionDataModel<FreelancerProfile> profiles = new CollectionDataModel<>();

    private final List<KeyValuePair<String, String>> kreditorNr = new ArrayList<>();

    private ProjectPosition currentProjectPosition = new ProjectPosition();

    private CollectionDataModel<ProjectPosition> positions = new CollectionDataModel<>();

    private final List<FreelancerToTag> tagsSchwerpunkte = new ArrayList<>();
    private final List<FreelancerToTag> tagsFunktionen = new ArrayList<>();
    private final List<FreelancerToTag> tagsEinsatzorte = new ArrayList<>();
    private final List<FreelancerToTag> tagsBemerkungen = new ArrayList<>();

    private Tag newSchwerpunkte;
    private Tag newFunktion;
    private Tag newEinsatzOrt;
    private Tag newBemerkung;

    private Long tagIdSchwerpunkt = null;
    private Long tagIdFunktion = null;
    private Long tagIdEinsatzOrt = null;
    private Long tagIdBemerkung = null;

    private List<TagSelectionState> tagSelection = new ArrayList<>();

    public FreelancerBackingBeanDataModel() {
        init();
    }

    private void init() {
        kreditorNr.add(new KeyValuePair<>("NL", "NL"));
        kreditorNr.add(new KeyValuePair<>("NL1", "NL1"));
        kreditorNr.add(new KeyValuePair<>("NL2","NL2"));
        kreditorNr.add(new KeyValuePair<>("X", "X"));
        kreditorNr.add(new KeyValuePair<>("NO", "NO"));
        kreditorNr.add(new KeyValuePair<>("LL", "LL"));
    }

    @Override
    protected void initialize() {
        setEntity(new Freelancer());
    }

    public CollectionDataModel<FreelancerProfile> getProfiles() {
        return profiles;
    }

    public List<KeyValuePair<String, String>> getKreditorNr() {
        return kreditorNr;
    }

    public ProjectPosition getCurrentProjectPosition() {
        return currentProjectPosition;
    }

    public void setCurrentProjectPosition(final ProjectPosition currentProjectPosition) {
        this.currentProjectPosition = currentProjectPosition;
    }

    public CollectionDataModel<ProjectPosition> getPositions() {
        return positions;
    }

    public void setPositions(final CollectionDataModel<ProjectPosition> positions) {
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

    public void setNewSchwerpunkte(final Tag newSchwerpunkte) {
        this.newSchwerpunkte = newSchwerpunkte;
    }

    public Tag getNewFunktion() {
        return newFunktion;
    }

    public void setNewFunktion(final Tag newFunktion) {
        this.newFunktion = newFunktion;
    }

    public Tag getNewEinsatzOrt() {
        return newEinsatzOrt;
    }

    public void setNewEinsatzOrt(final Tag newEinsatzOrt) {
        this.newEinsatzOrt = newEinsatzOrt;
    }

    public Tag getNewBemerkung() {
        return newBemerkung;
    }

    public void setNewBemerkung(final Tag newBemerkung) {
        this.newBemerkung = newBemerkung;
    }

    public Long getTagIdSchwerpunkt() {
        return tagIdSchwerpunkt;
    }

    public void setTagIdSchwerpunkt(final Long tagIdSchwerpunkt) {
        this.tagIdSchwerpunkt = tagIdSchwerpunkt;
    }

    public Long getTagIdFunktion() {
        return tagIdFunktion;
    }

    public void setTagIdFunktion(final Long tagIdFunktion) {
        this.tagIdFunktion = tagIdFunktion;
    }

    public Long getTagIdEinsatzOrt() {
        return tagIdEinsatzOrt;
    }

    public void setTagIdEinsatzOrt(final Long tagIdEinsatzOrt) {
        this.tagIdEinsatzOrt = tagIdEinsatzOrt;
    }

    public Long getTagIdBemerkung() {
        return tagIdBemerkung;
    }

    public void setTagIdBemerkung(final Long tagIdBemerkung) {
        this.tagIdBemerkung = tagIdBemerkung;
    }

    public List<TagSelectionState> getTagSelection() {
        return tagSelection;
    }

    public void setTagSelection(final List<TagSelectionState> tagSelection) {
        this.tagSelection = tagSelection;
    }
}