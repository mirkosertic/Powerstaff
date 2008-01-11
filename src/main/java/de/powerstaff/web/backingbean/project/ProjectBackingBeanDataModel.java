package de.powerstaff.web.backingbean.project;

import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.entity.Project;
import de.powerstaff.web.backingbean.NavigatingBackingBeanDataModel;

public class ProjectBackingBeanDataModel extends
		NavigatingBackingBeanDataModel<Project> {

	private CollectionDataModel<Project> searchResult = new CollectionDataModel<Project>();

	public ProjectBackingBeanDataModel() {
	}

	public ProjectBackingBeanDataModel(Project aProject) {
		super(aProject);
	}

	@Override
	protected void initialize() {
		setEntity(new Project());
	}

	@Override
	public void setEntity(Project aValue) {
		super.setEntity(aValue);
	}

	/**
	 * @return the searchResult
	 */
	public CollectionDataModel<Project> getSearchResult() {
		return searchResult;
	}

}
