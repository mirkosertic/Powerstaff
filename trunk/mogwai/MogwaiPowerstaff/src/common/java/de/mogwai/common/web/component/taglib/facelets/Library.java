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
package de.mogwai.common.web.component.taglib.facelets;

import com.sun.facelets.tag.jsf.html.AbstractHtmlLibrary;

import de.mogwai.common.web.component.action.CommandButtonComponent;
import de.mogwai.common.web.component.action.CommandLinkComponent;
import de.mogwai.common.web.component.action.PageRefreshComponent;
import de.mogwai.common.web.component.common.DumpResourceComponent;
import de.mogwai.common.web.component.common.FacetComponent;
import de.mogwai.common.web.component.common.ImageComponent;
import de.mogwai.common.web.component.common.LabelComponent;
import de.mogwai.common.web.component.common.LinkComponent;
import de.mogwai.common.web.component.common.ProcessTrainComponent;
import de.mogwai.common.web.component.common.SaveStateComponent;
import de.mogwai.common.web.component.common.ScriptComponent;
import de.mogwai.common.web.component.common.TableComponent;
import de.mogwai.common.web.component.common.VelocityTemplateComponent;
import de.mogwai.common.web.component.input.CheckboxComponent;
import de.mogwai.common.web.component.input.CheckboxListComponent;
import de.mogwai.common.web.component.input.ComboboxComponent;
import de.mogwai.common.web.component.input.DatepickerComponent;
import de.mogwai.common.web.component.input.EMailInputfieldComponent;
import de.mogwai.common.web.component.input.InputfieldComponent;
import de.mogwai.common.web.component.input.NumberInputfieldComponent;
import de.mogwai.common.web.component.input.RadioListComponent;
import de.mogwai.common.web.component.input.RadiobuttonComponent;
import de.mogwai.common.web.component.input.TextareaComponent;
import de.mogwai.common.web.component.layout.GridbagLayoutCellComponent;
import de.mogwai.common.web.component.layout.GridbagLayoutComponent;
import de.mogwai.common.web.component.layout.IncludeComponent;
import de.mogwai.common.web.component.layout.MessagesComponent;
import de.mogwai.common.web.component.layout.ModalComponent;
import de.mogwai.common.web.component.layout.ScrollComponent;
import de.mogwai.common.web.component.layout.TitleComponent;
import de.mogwai.common.web.component.layout.TitledBoxComponent;
import de.mogwai.common.web.component.taglib.facelets.action.CommandButtonHandler;
import de.mogwai.common.web.component.taglib.facelets.action.CommandLinkHandler;
import de.mogwai.common.web.component.taglib.facelets.action.PageRefreshHandler;
import de.mogwai.common.web.component.taglib.facelets.common.DumpResourceHandler;
import de.mogwai.common.web.component.taglib.facelets.common.FacetHandler;
import de.mogwai.common.web.component.taglib.facelets.common.ImageHandler;
import de.mogwai.common.web.component.taglib.facelets.common.LabelHandler;
import de.mogwai.common.web.component.taglib.facelets.common.LinkHandler;
import de.mogwai.common.web.component.taglib.facelets.common.ProcessTrainHandler;
import de.mogwai.common.web.component.taglib.facelets.common.SaveStateHandler;
import de.mogwai.common.web.component.taglib.facelets.common.ScriptHandler;
import de.mogwai.common.web.component.taglib.facelets.common.TableHandler;
import de.mogwai.common.web.component.taglib.facelets.common.VelocityTemplateHandler;
import de.mogwai.common.web.component.taglib.facelets.input.CheckboxHandler;
import de.mogwai.common.web.component.taglib.facelets.input.CheckboxListHandler;
import de.mogwai.common.web.component.taglib.facelets.input.ComboboxHandler;
import de.mogwai.common.web.component.taglib.facelets.input.DatepickerHandler;
import de.mogwai.common.web.component.taglib.facelets.input.EMailInputfieldHandler;
import de.mogwai.common.web.component.taglib.facelets.input.InputfieldHandler;
import de.mogwai.common.web.component.taglib.facelets.input.NumberInputfieldHandler;
import de.mogwai.common.web.component.taglib.facelets.input.RadioListHandler;
import de.mogwai.common.web.component.taglib.facelets.input.RadiobuttonHandler;
import de.mogwai.common.web.component.taglib.facelets.input.TextareaHandler;
import de.mogwai.common.web.component.taglib.facelets.layout.GridbagLayoutCellHandler;
import de.mogwai.common.web.component.taglib.facelets.layout.GridbagLayoutHandler;
import de.mogwai.common.web.component.taglib.facelets.layout.IncludeHandler;
import de.mogwai.common.web.component.taglib.facelets.layout.MessagesHandler;
import de.mogwai.common.web.component.taglib.facelets.layout.ModalHandler;
import de.mogwai.common.web.component.taglib.facelets.layout.ScrollHandler;
import de.mogwai.common.web.component.taglib.facelets.layout.TitleHandler;
import de.mogwai.common.web.component.taglib.facelets.layout.TitledBoxHandler;

public class Library extends AbstractHtmlLibrary {

    public Library() {
        super("http://mogwai.sourceforge.net/layout");

        addComponent("messages", MessagesComponent.COMPONENT_TYPE, MessagesComponent.RENDERER_TYPE,
                MessagesHandler.class);
        addComponent("gridBagLayout", GridbagLayoutComponent.COMPONENT_TYPE, GridbagLayoutComponent.RENDERER_TYPE,
                GridbagLayoutHandler.class);
        addComponent("cell", GridbagLayoutCellComponent.COMPONENT_TYPE, GridbagLayoutCellComponent.RENDERER_TYPE,
                GridbagLayoutCellHandler.class);
        addComponent("modal", ModalComponent.COMPONENT_TYPE, ModalComponent.RENDERER_TYPE, ModalHandler.class);
        addComponent("scroll", ScrollComponent.COMPONENT_TYPE, ScrollComponent.RENDERER_TYPE, ScrollHandler.class);
        addComponent("titledBox", TitledBoxComponent.COMPONENT_TYPE, TitledBoxComponent.RENDERER_TYPE,
                TitledBoxHandler.class);
        addComponent("include", IncludeComponent.COMPONENT_TYPE, IncludeComponent.RENDERER_TYPE, IncludeHandler.class);
        addComponent("velocityTemplate", VelocityTemplateComponent.COMPONENT_TYPE,
                VelocityTemplateComponent.RENDERER_TYPE, VelocityTemplateHandler.class);
        addComponent("link", LinkComponent.COMPONENT_TYPE, LinkComponent.RENDERER_TYPE, LinkHandler.class);
        addComponent("script", ScriptComponent.COMPONENT_TYPE, ScriptComponent.RENDERER_TYPE, ScriptHandler.class);
        addComponent("table", TableComponent.COMPONENT_TYPE, TableComponent.RENDERER_TYPE, TableHandler.class);
        addComponent("dumpResource", DumpResourceComponent.COMPONENT_TYPE, DumpResourceComponent.RENDERER_TYPE,
                DumpResourceHandler.class);
        addComponent("label", LabelComponent.COMPONENT_TYPE, LabelComponent.RENDERER_TYPE, LabelHandler.class);
        addComponent("image", ImageComponent.COMPONENT_TYPE, ImageComponent.RENDERER_TYPE, ImageHandler.class);
        addComponent("processTrain", ProcessTrainComponent.COMPONENT_TYPE, ProcessTrainComponent.RENDERER_TYPE,
                ProcessTrainHandler.class);
        addComponent("facet", FacetComponent.COMPONENT_TYPE, FacetComponent.RENDERER_TYPE, FacetHandler.class);
        addComponent("checkbox", CheckboxComponent.COMPONENT_TYPE, CheckboxComponent.RENDERER_TYPE,
                CheckboxHandler.class);
        addComponent("radio", RadiobuttonComponent.COMPONENT_TYPE, RadiobuttonComponent.RENDERER_TYPE,
                RadiobuttonHandler.class);
        addComponent("inputfield", InputfieldComponent.COMPONENT_TYPE, InputfieldComponent.RENDERER_TYPE,
                InputfieldHandler.class);
        addComponent("emailinputfield", EMailInputfieldComponent.COMPONENT_TYPE,
                EMailInputfieldComponent.RENDERER_TYPE, EMailInputfieldHandler.class);
        addComponent("numberinputfield", NumberInputfieldComponent.COMPONENT_TYPE,
                NumberInputfieldComponent.RENDERER_TYPE, NumberInputfieldHandler.class);
        addComponent("textarea", TextareaComponent.COMPONENT_TYPE, TextareaComponent.RENDERER_TYPE,
                TextareaHandler.class);
        addComponent("datepicker", DatepickerComponent.COMPONENT_TYPE, DatepickerComponent.RENDERER_TYPE,
                DatepickerHandler.class);
        addComponent("combobox", ComboboxComponent.COMPONENT_TYPE, ComboboxComponent.RENDERER_TYPE,
                ComboboxHandler.class);
        addComponent("radiolist", RadioListComponent.COMPONENT_TYPE, RadioListComponent.RENDERER_TYPE,
                RadioListHandler.class);
        addComponent("checkboxlist", CheckboxListComponent.COMPONENT_TYPE, CheckboxListComponent.RENDERER_TYPE,
                CheckboxListHandler.class);
        addComponent("commandButton", CommandButtonComponent.COMPONENT_TYPE, CommandButtonComponent.RENDERER_TYPE,
                CommandButtonHandler.class);
        addComponent("commandLink", CommandLinkComponent.COMPONENT_TYPE, CommandLinkComponent.RENDERER_TYPE,
                CommandLinkHandler.class);
        addComponent("pageRefresh", PageRefreshComponent.COMPONENT_TYPE, PageRefreshComponent.RENDERER_TYPE,
                PageRefreshHandler.class);
        addComponent("title", TitleComponent.COMPONENT_TYPE, TitleComponent.RENDERER_TYPE, TitleHandler.class);
        addComponent("saveState", SaveStateComponent.COMPONENT_TYPE, SaveStateComponent.RENDERER_TYPE,
                SaveStateHandler.class);

    }
}
