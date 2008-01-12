package de.powerstaff.web.backingbean.profile;

import java.util.Vector;

import javax.faces.component.UIComponent;

import de.mogwai.common.web.backingbean.BackingBeanDataModel;
import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.service.ProfileSearchResult;

public class ProfileBackingBeanDataModel extends BackingBeanDataModel {

	private String searchString;

	private transient UIComponent viewRoot;
	
	private CollectionDataModel<ProfileSearchResult> searchResult = new CollectionDataModel<ProfileSearchResult>(new Vector<ProfileSearchResult>());
	
	/**
	 * @return the searchString
	 */
	public String getSearchString() {
		return searchString;
	}

	/**
	 * @param searchString the searchString to set
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	/**
	 * @return the viewRoot
	 */
	public UIComponent getViewRoot() {
		return viewRoot;
	}

	/**
	 * @param viewRoot the viewRoot to set
	 */
	public void setViewRoot(UIComponent viewRoot) {
		this.viewRoot = viewRoot;
	}

	/**
	 * @return the searchResult
	 */
	public CollectionDataModel<ProfileSearchResult> getSearchResult() {
		return searchResult;
	}
	
	public int getSearchResultSize() {
		return searchResult.size();
	}
}