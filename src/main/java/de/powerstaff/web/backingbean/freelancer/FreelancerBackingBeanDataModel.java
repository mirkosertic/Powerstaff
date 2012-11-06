package de.powerstaff.web.backingbean.freelancer;

import de.mogwai.common.utils.KeyValuePair;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.business.entity.ProjectPosition;
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

    public FreelancerBackingBeanDataModel() {
        init();
    }

    public FreelancerBackingBeanDataModel(Freelancer aFreelancer) {
        super(aFreelancer);
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

    /**
     * @return the profiles
     */
    public CollectionDataModel<FreelancerProfile> getProfiles() {
        return profiles;
    }

    /**
     * @return the status
     */
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
}
