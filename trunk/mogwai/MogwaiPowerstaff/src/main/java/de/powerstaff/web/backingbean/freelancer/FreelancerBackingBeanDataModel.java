package de.powerstaff.web.backingbean.freelancer;

import java.util.List;
import java.util.Vector;

import de.mogwai.common.utils.KeyValuePair;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.FreelancerProfile;
import de.powerstaff.web.backingbean.PersonEditorBackingBeanDataModel;

public class FreelancerBackingBeanDataModel extends PersonEditorBackingBeanDataModel<Freelancer> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 686254367960000233L;

	private CollectionDataModel<FreelancerProfile> profiles = new CollectionDataModel<FreelancerProfile>();

    private List status = new Vector();

    public FreelancerBackingBeanDataModel() {
        status.add(new KeyValuePair<Integer, String>(1, "Angestellter"));
        status.add(new KeyValuePair<Integer, String>(2, "Freiberufler"));
        status.add(new KeyValuePair<Integer, String>(3, "Freiberufler + Festanstellung"));
        status.add(new KeyValuePair<Integer, String>(4, "ANÜ"));
        status.add(new KeyValuePair<Integer, String>(5, "Angestellter + ANÜ"));
        status.add(new KeyValuePair<Integer, String>(6, "Freiberufler + ANÜ"));
        status.add(new KeyValuePair<Integer, String>(7, "Freiberufler + Festanstellung + ANÜ"));
        status.add(new KeyValuePair<Integer, String>(null, "Undefiniert"));
    }

    public FreelancerBackingBeanDataModel(Freelancer aFreelancer) {
        super(aFreelancer);
        status.add(new KeyValuePair<Integer, String>(1, "Angestellter"));
        status.add(new KeyValuePair<Integer, String>(2, "Freiberufler"));
        status.add(new KeyValuePair<Integer, String>(3, "Freiberufler + Festanstellung"));
        status.add(new KeyValuePair<Integer, String>(4, "ANÜ"));
        status.add(new KeyValuePair<Integer, String>(5, "Angestellter + ANÜ"));
        status.add(new KeyValuePair<Integer, String>(6, "Freiberufler + ANÜ"));
        status.add(new KeyValuePair<Integer, String>(7, "Freiberufler + Festanstellung + ANÜ"));
        status.add(new KeyValuePair<Integer, String>(null, "Undefiniert"));
    }

    @Override
    protected void initialize() {
        setEntity(new Freelancer());
    }

    @Override
    public void setEntity(Freelancer aValue) {
        super.setEntity(aValue);
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
}
