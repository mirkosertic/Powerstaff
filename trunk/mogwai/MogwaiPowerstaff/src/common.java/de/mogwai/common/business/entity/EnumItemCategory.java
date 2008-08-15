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
package de.mogwai.common.business.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Geschäftsobjekt eine Codetabellen Kategorie (für Comboboxen usw).
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-08-15 16:05:45 $
 */
public class EnumItemCategory extends Entity {

    private String name;

    /**
     * Die Sortierung der EnumItems soll alphabetisch erfolgen. Falls false,
     * wird die Sortierung gemäss EnumItem.listIndex vorgenommen.
     */
    private Boolean isAlphabeticalOrder;

    private EnumItem defaultItem;

    private Set<EnumItem> enumItems = new HashSet<EnumItem>();

    private Map<String, String> labels = new HashMap<String, String>();

    private boolean system;

    /**
     * Holt das sprachspezifische Label.
     * 
     * @param locale
     *                Locale mit Sprachcode.
     * @return sprachspezifisches Label oder der Name des EnumItems, falls
     *         keines vorliegt.
     */
    public String getLabel(Locale locale) {

        String label = null;
        if (this.labels != null) {
            label = this.labels.get(locale.getLanguage());
        }
        if (label == null) {
            label = this.name;
        }
        return label;
    }

    /**
     * Gibt den Wert des Attributs <code>labels</code> zurück.
     * 
     * @return Wert des Attributs labels.
     */
    public Map<String, String> getLabels() {
        return labels;
    }

    /**
     * Setzt den Wert des Attributs <code>labels</code>.
     * 
     * @param labels
     *                Wert für das Attribut labels.
     */
    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    /**
     * Gibt den Wert des Attributs <code>name</code> zurück.
     * 
     * @return Wert des Attributs name.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Wert des Attributs <code>name</code>.
     * 
     * @param name
     *                Wert für das Attribut name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Wert des Attributs <code>isAlphabeticalOrder</code> zurück.
     * 
     * @return Wert des Attributs isAlphabeticalOrder.
     */
    public Boolean getIsAlphabeticalOrder() {
        return isAlphabeticalOrder;
    }

    /**
     * Setzt den Wert des Attributs <code>isAlphabeticalOrder</code>.
     * 
     * @param isAlphabeticalOrder
     *                Wert für das Attribut isAlphabeticalOrder.
     */
    public void setIsAlphabeticalOrder(Boolean isAlphabeticalOrder) {
        this.isAlphabeticalOrder = isAlphabeticalOrder;
    }

    /**
     * Gibt den Wert des Attributs <code>enumItems</code> zurück.
     * 
     * @return Wert des Attributs enumItems.
     */
    public Set<EnumItem> getEnumItems() {
        return enumItems;
    }

    /**
     * Setzt den Wert des Attributs <code>enumItems</code>.
     * 
     * @param enumItems
     *                Wert für das Attribut enumItems.
     */
    public void setEnumItems(Set<EnumItem> enumItems) {
        this.enumItems = enumItems;
    }

    /**
     * neues <code>EnumItem</code> zum Set hinzufügen.
     * 
     * @param enumItem
     *                neues EnumItem
     */
    public void addEnumItem(EnumItem enumItem) {
        this.enumItems.add(enumItem);
    }

    /**
     * bestehendes <code>EnumItem</code> aus dem Set löschen.
     * 
     * @param enumItem
     *                bestehendes EnumItem param neDefaultItem wird das neue
     *                default falls das gelöschte item das default war
     */
    public void removeEnumItem(EnumItem enumItem, EnumItem newDefaultItem) {
        this.enumItems.remove(enumItem);

        if (defaultItem != null) {
            if (enumItem.getId().equals(defaultItem.getId())) {
                defaultItem = newDefaultItem;
            }
        }
    }

    /**
     * Gibt den Wert des Attributs <code>defaultItem</code> zurück.
     * 
     * @return Wert des Attributs defaultItem.
     */
    public EnumItem getDefaultItem() {
        return defaultItem;
    }

    /**
     * Setzt den Wert des Attributs <code>defaultItem</code>.
     * 
     * @param defaultItem
     *                Wert für das Attribut defaultItem.
     */
    public void setDefaultItem(EnumItem defaultItem) {
        this.defaultItem = defaultItem;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }
}
