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
package de.mogwai.common.web.component.layout;

import de.mogwai.common.web.component.input.ModalComponentUtils;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * Modal component.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:29:19 $
 */
public class ModalComponent extends ModalComponentBase {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ModalComponent.class);

    private boolean switched = false;

    public ModalComponent() {
        setRendered(false);
    }

    @Override
    public void setRendered(boolean aStatus) {
        switched = true;
        super.setRendered(aStatus);
    }

    /**
     * @return the switched
     */
    public boolean isSwitched() {
        return switched;
    }

    public boolean isReallyRendered() {
        return super.isRendered();
    }

    @Override
    public boolean isRendered() {
        return true;
    }

    @Override
    public void processDecodes(FacesContext aContext) {

        // Marker setzen
        ModalComponentUtils.setInModalComponent(this, aContext);

        super.processDecodes(aContext);

        ModalComponentUtils.removeInModalComponent(this, aContext);
    }

    @Override
    public void processUpdates(FacesContext aContext) {
        // Marker setzen
        ModalComponentUtils.setInModalComponent(this, aContext);

        super.processUpdates(aContext);

        ModalComponentUtils.removeInModalComponent(this, aContext);
    }

    @Override
    public void processValidators(FacesContext aContext) {
        // Marker setzen
        ModalComponentUtils.setInModalComponent(this, aContext);

        super.processValidators(aContext);

        ModalComponentUtils.removeInModalComponent(this, aContext);
    }

    @Override
    public void encodeBegin(FacesContext aContext) throws IOException {

        if (!isRendered()) {

            // Wenn diese Komponente nicht gerendert wird, so wird
            // Die Include - Komponente, welche eingebettet ist, auf null
            // Zurückgesetzt, damit beim nächsten mal auf jeden Fall
            // ein neuer Komponentenbaum erzeugt wird
            updateIncludeComponents(this);

        }

        // Marker setzen
        ModalComponentUtils.setInModalComponent(this, aContext);

        Renderer renderer = getRenderer(aContext);
        if (renderer != null) {
            renderer.encodeBegin(aContext, this);
        }
    }

    protected void updateIncludeComponents(UIComponent aComponent) {
        if (aComponent instanceof IncludeComponent) {
            IncludeComponent theIncludeComponent = (IncludeComponent) aComponent;

            LOGGER.debug("Resetting LastIncludePage for Component {}", aComponent.getId());
            theIncludeComponent.setLastIncludedPage(null);
            theIncludeComponent.getChildren().clear();

            return;
        }

        for (int i = 0; i < aComponent.getChildCount(); i++) {
            updateIncludeComponents((UIComponent) aComponent.getChildren().get(i));
        }
    }

    @Override
    public void encodeChildren(FacesContext context) throws IOException {

        Renderer renderer = getRenderer(context);
        if (renderer != null) {
            renderer.encodeChildren(context, this);
        }
    }

    @Override
    public void encodeEnd(FacesContext aContext) throws IOException {

        Renderer renderer = getRenderer(aContext);
        if (renderer != null) {
            renderer.encodeEnd(aContext, this);
        }
        ModalComponentUtils.removeInModalComponent(this, aContext);
    }
}
