/**
 * Copyright 2002 - 2007 the Mogwai Project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.mogwai.common.web.navigation;

import de.mogwai.common.utils.SourceProvider;

/**
 * Ein Descriptor für ein Element eines Processtrains.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:20 $
 */
public abstract class SourceProviderPageDescriptor extends PageDescriptor implements SourceProvider {

    private MultiPageNavigationDataModel model;

    public SourceProviderPageDescriptor(MultiPageNavigationDataModel aModel, String aPage, String aDescriptionKey) {
        this(aModel, aPage, aDescriptionKey, true);
    }

    public SourceProviderPageDescriptor(MultiPageNavigationDataModel aModel, String aPage, String aDescriptionKey,
            boolean aVisible) {
        super(aPage, aDescriptionKey);
        model = aModel;
    }

    public String provideSource() {

        int theIndex = model.getIndex();
        int theMyIndex = model.getPages().indexOf(this);

        if (!isEnabled()) {

            if (theMyIndex < theIndex) {
                return getProcessedImage();
            } else {
                return getInactiveImage();
            }
        }

        if (theIndex == theMyIndex) {
            return getActiveImage();
        }

        if (theMyIndex < theIndex) {
            return getProcessedImage();
        } else {
            return getUnprocessedImage();
        }
    }

    public abstract String getProcessedImage();

    public abstract String getActiveImage();

    public abstract String getInactiveImage();

    public abstract String getUnprocessedImage();

    @Override
    public boolean isEnabled() {
        return model.isEnabled(this);
    }

    public boolean isActive() {
        return model.isActive(this);
    }

    public boolean isNotActive() {
        return !isActive();
    }
}
