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
package de.powerstaff.web.backingbean.statistik;

import de.mogwai.common.logging.Logger;
import de.mogwai.common.web.backingbean.WrappingBackingBean;
import de.mogwai.common.web.utils.JSFMessageUtils;
import de.powerstaff.business.dto.KontakthistorieEntry;
import de.powerstaff.business.service.AdditionalDataService;
import de.powerstaff.business.service.StatisticService;
import de.powerstaff.web.backingbean.MessageConstants;
import org.springframework.beans.factory.InitializingBean;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

public class StatistikBackingBean extends
        WrappingBackingBean<StatistikBackingBeanDataModel> implements
        MessageConstants, StateHolder, InitializingBean {

    private static final long serialVersionUID = -3411470666529912902L;

    private static final Logger LOGGER = new Logger(StatistikBackingBean.class);

    private transient StatisticService statisticService;

    private transient AdditionalDataService additionalDataService;

    @Override
    protected StatistikBackingBeanDataModel createDataModel() {
        return new StatistikBackingBeanDataModel();
    }

    @Override
    public void afterPropertiesSet() {
        if (getData() != null) {
            getData().setViewRoot(null);
        } else {
            setData(createDataModel());
        }
        getData().setBenutzerListe(additionalDataService.getUserList());
    }

    public boolean isTransient() {
        return false;
    }

    public void setTransient(boolean aValue) {
    }

    public void restoreState(FacesContext aContext, Object aValue) {
        Object[] theData = (Object[]) aValue;
        setData((StatistikBackingBeanDataModel) theData[0]);
    }

    public Object saveState(FacesContext aContext) {
        ArrayList theData = new ArrayList();
        theData.add(getData());
        return theData.toArray();
    }

    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    public void setAdditionalDataService(
            AdditionalDataService additionalDataService) {
        this.additionalDataService = additionalDataService;
    }

    public void commandSearch() {
        try {

            List<KontakthistorieEntry> theResult = statisticService
                    .kontakthistorie(getData().getDatumVon(), getData()
                            .getDatumBis(), getData().getBenutzer());
            getData().getSearchResult().setWrappedData(theResult);

            if (theResult.size() == 0) {
                JSFMessageUtils.addGlobalErrorMessage(MSG_KEINEDATENGEFUNDEN);
            }

        } catch (Exception e) {
            JSFMessageUtils.addGlobalErrorMessage(MSG_FEHLERBEIDERSUCHE, e
                    .getMessage());
            LOGGER.logError("Fehler bei Suche", e);
        }
    }
}