/**
 * Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.web.backingbean.partner;

import de.mogwai.common.web.utils.CollectionDataModel;
import de.powerstaff.business.entity.Freelancer;
import de.powerstaff.business.entity.Partner;
import de.powerstaff.web.backingbean.PersonEditorBackingBeanDataModel;

public class PartnerBackingBeanDataModel extends PersonEditorBackingBeanDataModel<Partner> {

    private static final long serialVersionUID = 6489685830340553955L;

    private CollectionDataModel<Freelancer> freelancer;

    private String codeToAdd;

    public PartnerBackingBeanDataModel() {
    }

    @Override
    protected void initialize() {
        setEntity(new Partner());
    }

    @Override
    public void setEntity(Partner aValue) {
        super.setEntity(aValue);
        freelancer = new CollectionDataModel<Freelancer>(aValue.getFreelancer());
        codeToAdd = null;
    }

    /**
     * @return the freelancer
     */
    public CollectionDataModel<Freelancer> getFreelancer() {
        return freelancer;
    }

    /**
     * @param freelancer the freelancer to set
     */
    public void setFreelancer(CollectionDataModel<Freelancer> freelancer) {
        this.freelancer = freelancer;
    }

    /**
     * @return the codeToAdd
     */
    public String getCodeToAdd() {
        return codeToAdd;
    }

    /**
     * @param codeToAdd the codeToAdd to set
     */
    public void setCodeToAdd(String codeToAdd) {
        this.codeToAdd = codeToAdd;
    }
}