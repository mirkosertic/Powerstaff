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
package de.mogwai.common.web.component.renderkit.html.layout;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.renderkit.html.HtmlMessagesRenderer;

import de.mogwai.common.web.component.input.ModalComponentUtils;

/**
 * Renderer for Messages.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:05:20 $
 */
public class MessagesRenderer extends HtmlMessagesRenderer {

    @Override
    public void encodeEnd(FacesContext aContext, UIComponent aComponent) throws IOException {
        if (ModalComponentUtils.isInModalDialogOrOnSimplePage(aContext, aComponent)) {
            super.encodeEnd(aContext, aComponent);
        }
    }

    @Override
    public void encodeBegin(FacesContext aContext, UIComponent aComponent) throws IOException {
        if (ModalComponentUtils.isInModalDialogOrOnSimplePage(aContext, aComponent)) {
            super.encodeBegin(aContext, aComponent);
        }
    }

    @Override
    public void encodeChildren(FacesContext aContext, UIComponent aComponent) throws IOException {
        if (ModalComponentUtils.isInModalDialogOrOnSimplePage(aContext, aComponent)) {
            super.encodeChildren(aContext, aComponent);
        }
    }

}
