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
package de.mogwai.common.web.component.renderkit.html;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.shared_impl.renderkit.RendererUtils;

import de.mogwai.common.utils.StringPresentationProvider;
import de.mogwai.common.web.component.layout.GridbagLayoutCellComponent;

/**
 * Base renderer.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:38:59 $
 */
public class BaseRenderer extends Renderer {

	protected boolean rendersChildren;

	protected BaseRenderer() {
		rendersChildren = true;
	}

	@Override
	public boolean getRendersChildren() {
		return rendersChildren;
	}

	@Override
	public Object getConvertedValue(FacesContext aContext,
			UIComponent aComponent, Object aSubmittedValue) {

		if ((aComponent instanceof UIOutput)
				&& (aSubmittedValue instanceof String)) {
			Converter theConverter = ((UIOutput) aComponent).getConverter();
			if (theConverter != null) {
				return theConverter.getAsObject(aContext, aComponent,
						(String) aSubmittedValue);
			}
		}

		return aSubmittedValue;
	}

	/**
	 * Ermittlung der String - Darstellung für einen Wert.
	 * 
	 * Falls die Komponente einen Converter hat, wird der Converter benutzt, um
	 * die String - Darstellung zu generieren.
	 * 
	 * Falls der Wert ein StringPresentationProvider ist, so wird dieses
	 * Interface benutzt, um die String - Darstellung zu generieren.
	 * 
	 * Wenn der Wert null ist, wird ein Leerstring zurückgegeben.
	 * 
	 * In allen anderen Fällen wird die toString() Methode benutzt.
	 * 
	 * @param aContext
	 *            der Context
	 * @param aComponent
	 *            die Komponente
	 * @param aValue
	 *            der Wert
	 * @return die String - Version
	 */
	protected String getStringValue(FacesContext aContext, UIOutput aComponent,
			Object aValue) {

		Converter theConverter = (aComponent).getConverter();
		if (theConverter != null) {
			return theConverter.getAsString(aContext, aComponent, aValue);
		}

		if (aValue instanceof StringPresentationProvider) {
			return ((StringPresentationProvider) aValue)
					.getStringPresentation();
		}

		if (aValue == null) {
			return "";
		}

		return aValue.toString();
	}

	/**
	 * Render a component to its string representation.
	 * 
	 * @param aContext
	 *            der Kontext
	 * @param aComponent
	 *            die Komponente
	 * @return the generated HTML - Data for the component.
	 * @throws IOException
	 *             wird im Falle eines Fehlers geworfen
	 */
	protected String renderComponentToString(FacesContext aContext,
			UIComponent aComponent) throws IOException {

		ResponseWriter theOldWriter = aContext.getResponseWriter();

		StringWriter theWriter = new StringWriter();
		ResponseWriter theTempWriter = theOldWriter.cloneWithWriter(theWriter);

		aContext.setResponseWriter(theTempWriter);

		RendererUtils.renderChild(aContext, aComponent);

		theTempWriter.flush();

		aContext.setResponseWriter(theOldWriter);

		return theWriter.toString();

	}

	protected void setWidthIfInGridBag(ResponseWriter aWriter,
			UIComponent aComponent, HashMap<String, String> aAdditionalStyles)
			throws IOException {

		if (aComponent.getParent() instanceof GridbagLayoutCellComponent) {
			int theWidth = ((GridbagLayoutCellComponent) aComponent.getParent())
					.computeWidth();

			if (aAdditionalStyles == null) {
				aAdditionalStyles = new HashMap<String, String>();
			}

			aAdditionalStyles.put("width", "" + (theWidth) + "px;");
		}

		if ((aAdditionalStyles != null)
				&& (aAdditionalStyles.keySet().size() > 0)) {

			StringBuilder theStyles = new StringBuilder();
			for (Map.Entry<String, String> theEntry : aAdditionalStyles
					.entrySet()) {

				theStyles.append(theEntry.getKey());
				theStyles.append(":");
				theStyles.append(theEntry.getValue());
				theStyles.append(";");
			}

			aWriter.writeAttribute("style", theStyles, null);
		}

	}

	protected HashMap<String, String> getHeightIfInGridGag(
			UIComponent aComponent) {

		if (aComponent.getParent() instanceof GridbagLayoutCellComponent) {
			int theHeight = ((GridbagLayoutCellComponent) aComponent
					.getParent()).computeHeight();

			HashMap<String, String> theResult = new HashMap<String, String>();
			theResult.put("height", theHeight + "px");

			return theResult;
		}

		return null;
	}

	protected void setHeightIfInGridBag(ResponseWriter aWriter,
			UIComponent aComponent) throws IOException {

		if (aComponent.getParent() instanceof GridbagLayoutCellComponent) {
			int theHeight = ((GridbagLayoutCellComponent) aComponent
					.getParent()).computeHeight();

			aWriter.writeAttribute("style", "height:" + (theHeight) + "px;",
					null);
		}
	}

	protected String computeAbsoluteURL(FacesContext aContext,
			String aUrlRelativeToContext) {

		ExternalContext theExternalContext = aContext.getExternalContext();
		HttpServletRequest theRequest = (HttpServletRequest) theExternalContext
				.getRequest();

		String theRequestURI = theRequest.getRequestURI();
		String theContextPath = theRequest.getContextPath();

		String theApplicationURI = theRequestURI.substring(theContextPath
				.length());
		if (theApplicationURI.equals("/")) {
			theApplicationURI = "";
		}

		if (theApplicationURI.startsWith("/")) {
			theApplicationURI = theApplicationURI.substring(1);
		}

		if (aUrlRelativeToContext.startsWith("/")) {
			aUrlRelativeToContext = aUrlRelativeToContext.substring(1);
		}

		String theRealUrl = aUrlRelativeToContext;
		int p = theApplicationURI.indexOf("/");
		while (p >= 0) {
			theRealUrl = "../" + theRealUrl;
			p = theApplicationURI.indexOf("/", p + 1);
		}

		return theRealUrl;
	}

	/**
	 * Remove any html comments from a given string.
	 * 
	 * @param aValue
	 *            the html string
	 * @return the string without comments
	 */
	protected StringBuffer stripCommentsFromString(StringBuffer aValue) {

		int p = aValue.indexOf("<!--");
		while (p >= 0) {
			int p2 = aValue.indexOf("-->", p);

			aValue.delete(p, p2 + 3);

			p = aValue.indexOf("<!--");
		}

		return aValue;
	}

	/**
	 * Ermittlung des Renderers für eine Komponente.
	 * 
	 * @param aContext
	 *            der Kontext
	 * @param aComponent
	 *            die Komponente
	 * @return der Renderer oder null, wenn kein Renderer gefunden wurde
	 */
	protected Renderer getRenderer(FacesContext aContext, UIComponent aComponent) {
		String theRenderType = aComponent.getRendererType();
		if (theRenderType == null) {
			return null;
		}

		String theRenderKitId = aContext.getViewRoot().getRenderKitId();
		RenderKitFactory theRenderKitFactory = (RenderKitFactory) FactoryFinder
				.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
		RenderKit theRenderKit = theRenderKitFactory.getRenderKit(aContext,
				theRenderKitId);
		return theRenderKit.getRenderer(aComponent.getFamily(), theRenderType);
	}

	@Override
	public void encodeChildren(FacesContext aContext, UIComponent aComponent)
			throws IOException {
		RendererUtils.renderChildren(aContext, aComponent);
	}
}
