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
package de.mogwai.common.web.backingbean;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;

import de.mogwai.common.business.entity.EnumItem;
import de.mogwai.common.business.entity.EnumItemCategory;
import de.mogwai.common.business.entity.EnumItemLabelComparator;
import de.mogwai.common.business.entity.EnumItemListIndexComparator;
import de.mogwai.common.business.enums.BaseEnumItemCategoryEnum;
import de.mogwai.common.business.service.EnumItemCategoryService;
import de.mogwai.common.business.service.EnumItemService;
import de.mogwai.common.web.model.EnumItemMultiLanguageSelectItem;
import de.mogwai.common.web.utils.EnumItemMultiLanguageSelectItemList;

/**
 * Backing Bean für EnumItems bzw. JSF-SelectItems, welche EnumItems beinhalten.
 * Das Bean wird im <code>application</code> Scope gehalten, da die enumItems
 * für sämtliche Benutzer identisch sind.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:17:09 $
 */
public abstract class EnumItemBackingBean extends BackingBean {

	private EnumItemService enumItemService;

	private EnumItemCategoryService enumItemCategoryService;

	/**
	 * Map mit allen EnumItems. Map-Key ist <code>EnumItem.id</code>.
	 */
	private Map<Long, EnumItem> enumItemMap;

	/**
	 * Map mit der Id des Default-EnumItems pro Kategorie. Falls kein
	 * Default-Enum definiert ist, exisitiert zu dieser Kategorie kein Eintrag.
	 * Key = EnumItemCaregory.id, Value = EnumItem.id des Default-EnumItems
	 */
	private Map<Long, Long> defaultEnumItemMap;

	/**
	 * Map mit JSF-SelectItems (die als Wert ein EnumItem besitzen) pro
	 * Kategorie. Map-Key ist <code>EnumItemCategory.id</code>. Sortierung nach
	 * deutschen Labels.
	 */
	private Map<Long, EnumItemMultiLanguageSelectItemList> selectItemCategoryMapDE;

	/**
	 * Map mit JSF-SelectItems (die als Wert ein EnumItem besitzen) pro
	 * Kategorie. Map-Key ist <code>EnumItemCategory.id</code>. Sortierung nach
	 * französischen Labels.
	 */
	private Map<Long, EnumItemMultiLanguageSelectItemList> selectItemCategoryMapFR;

	/**
	 * Gibt den Wert des Attributs <code>enumItemService</code> zurück.
	 * 
	 * @return Wert des Attributs enumItemService.
	 */
	public EnumItemService getEnumItemService() {
		return enumItemService;
	}

	/**
	 * Setzt den Wert des Attributs <code>enumItemService</code>.
	 * 
	 * @param enumItemService
	 *            Wert für das Attribut enumItemService.
	 */
	public void setEnumItemService(EnumItemService enumItemService) {
		this.enumItemService = enumItemService;
	}

	/**
	 * Gibt den Wert des Attributs <code>enumItemCategoryService</code> zurück.
	 * 
	 * @return Wert des Attributs enumItemCategoryService.
	 */
	public EnumItemCategoryService getEnumItemCategoryService() {
		return enumItemCategoryService;
	}

	/**
	 * Setzt den Wert des Attributs <code>enumItemCategoryService</code>.
	 * 
	 * @param enumItemCategoryService
	 *            Wert für das Attribut enumItemCategoryService.
	 */
	public void setEnumItemCategoryService(
			EnumItemCategoryService enumItemCategoryService) {
		this.enumItemCategoryService = enumItemCategoryService;
	}

	/**
	 * Convenience Methode für den Zugriff auf ein EnumItem per ID.
	 * 
	 * @param id
	 *            EnumItem-ID
	 * @return EnumItem mit dieser ID.
	 */
	public EnumItem getEnumItem(long id) {
		EnumItem enumItem = this.getEnumItemMap().get(Long.valueOf(id));
		if (enumItem == null) {
			String msg = MessageFormat.format(
					"Es existiert kein EnumItem mit ID {0}.", id);
			throw new IllegalArgumentException(msg);
		}
		return enumItem;
	}

	/**
	 * Gibt das Default-EnumItem zu einer bestimmten Kategorie zurück.
	 * 
	 * @param enumItemCategoryEnum
	 *            EnumItemCategory-Enumeration.
	 * @return Default-EnumItem der Kategorie oder null, falls kein Default
	 *         definiert.
	 */
	public EnumItem getDefaultEnumItem(
			BaseEnumItemCategoryEnum enumItemCategoryEnum) {

		Long defaultEnumItemId = this.getDefaultEnumItemMap().get(
				enumItemCategoryEnum.getId());
		if (defaultEnumItemId != null) {
			return this.getEnumItem(defaultEnumItemId);
		}

		return null;
	}

	/**
	 * Zugriff auf SelectItem Liste einer bestimmten Kategorie. Die enthaltenen
	 * EnumItems sind entweder nach listIndex oder lexikografisch in der
	 * aktuellen Sprache sortiert.
	 * 
	 * @param enumItemCategoryEnum
	 *            EnumItem Kategorie.
	 * @return SelectItem Liste der bestimmten Kategorie.
	 */
	public EnumItemMultiLanguageSelectItemList getSelectItemList(
			BaseEnumItemCategoryEnum enumItemCategoryEnum) {

		Locale currentLocale = Locale.GERMAN;
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null) {
			currentLocale = context.getViewRoot().getLocale();
		}

		if (Locale.FRENCH.equals(currentLocale)) {
			if (this.selectItemCategoryMapFR == null) {
				this.selectItemCategoryMapFR = this
						.getSelectItemCategoryMap(currentLocale);
			}
			return new EnumItemMultiLanguageSelectItemList(
					this.selectItemCategoryMapFR.get(enumItemCategoryEnum
							.getId()));
		}

		if (this.selectItemCategoryMapDE == null) {
			this.selectItemCategoryMapDE = this
					.getSelectItemCategoryMap(currentLocale);
		}
		return new EnumItemMultiLanguageSelectItemList(
				this.selectItemCategoryMapDE.get(enumItemCategoryEnum.getId()));
	}

	/**
	 * Löscht den "Cache", so dass die EnumItem-Maps beim nächsten Zugriff
	 * wieder ab Datenbank gelesen werden. Die Methode ist aufzurufen, wenn auf
	 * der Datenbank Mutationen durchgeführt wurden (Stammdatenverwaltung).
	 */
	@Override
	public void init() {

		super.init();

		this.enumItemMap = null;
		this.defaultEnumItemMap = null;
		this.selectItemCategoryMapDE = null;
		this.selectItemCategoryMapFR = null;
	}

	/**
	 * Holt eine Map von kategorisierten SelectItems, die pro Kategorie jeweils
	 * sortiert sind.
	 * 
	 * @param locale
	 *            Locale für die lexikografische Sortierung.
	 * @return Map von kategorisierten SelectItems.
	 */
	private Map<Long, EnumItemMultiLanguageSelectItemList> getSelectItemCategoryMap(
			Locale locale) {

		Map<Long, EnumItemMultiLanguageSelectItemList> selectItemCategoryMap = new HashMap<Long, EnumItemMultiLanguageSelectItemList>();
		List<EnumItemCategory> enumItemCategoryList = this.enumItemCategoryService
				.getEnumItemCategoryList();

		for (Iterator iter = enumItemCategoryList.iterator(); iter.hasNext();) {
			EnumItemCategory enumItemCategory = (EnumItemCategory) iter.next();

			List<EnumItem> enumItemsOfOneCategory = new LinkedList<EnumItem>(
					enumItemCategory.getEnumItems());
			EnumItemMultiLanguageSelectItemList selectItemList = new EnumItemMultiLanguageSelectItemList();

			if (enumItemCategory.getIsAlphabeticalOrder().booleanValue()) {
				// Lexikografische Sortierung
				Collections.sort(enumItemsOfOneCategory,
						new EnumItemLabelComparator(locale));
			} else {
				// Sortierung nach listIndex
				Collections.sort(enumItemsOfOneCategory,
						new EnumItemListIndexComparator());
			}

			for (EnumItem enumItem : enumItemsOfOneCategory) {
				EnumItemMultiLanguageSelectItem selectItem = new EnumItemMultiLanguageSelectItem(
						enumItem);
				selectItemList.add(selectItem);
			}
			selectItemCategoryMap.put(enumItemCategory.getId(), selectItemList);
		}

		return selectItemCategoryMap;
	}

	/**
	 * Gibt den Wert des Attributs <code>enumItemMap</code> zurück.
	 * 
	 * @return Wert des Attributs enumItemMap.
	 */
	private synchronized Map<Long, EnumItem> getEnumItemMap() {

		if (this.enumItemMap == null) {
			this.enumItemMap = this.enumItemService.getEnumItemMap();
		}

		return this.enumItemMap;
	}

	/**
	 * Gibt den Wert des Attributs <code>defaultEnumItemMap</code> zurück.
	 * 
	 * @return Wert des Attributs defaultEnumItemMap.
	 */
	private synchronized Map<Long, Long> getDefaultEnumItemMap() {
		if (this.defaultEnumItemMap == null) {
			this.defaultEnumItemMap = new HashMap<Long, Long>();

			List<EnumItemCategory> enumItemCategoryList = this.enumItemCategoryService
					.getEnumItemCategoryList();

			for (Iterator iter = enumItemCategoryList.iterator(); iter
					.hasNext();) {
				EnumItemCategory enumItemCategory = (EnumItemCategory) iter
						.next();

				EnumItem theItem = enumItemCategory.getDefaultItem();
				if (((theItem != null) && (theItem.getId() != null))) {
					this.defaultEnumItemMap.put(enumItemCategory.getId(),
							theItem.getId());
				}
			}
		}
		return this.defaultEnumItemMap;
	}
}
