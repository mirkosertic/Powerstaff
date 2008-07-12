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
import java.util.Locale;
import java.util.Map;

/**
 * Geschäftsobjekt für einen Eintrag in der Codetabelle (für Comboboxen usw).
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-07-12 14:11:37 $
 */
public class EnumItem extends AuditableEntity {

    private Integer listIndex;

    private String name;

    private Map<String, String> labels = new HashMap<String, String>();

    public EnumItem() {
    }

    public EnumItem(long aId) {
        setId(aId);
    }

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
     * bestehendes Label ändern oder falls noch nicht existent, neues Label zur
     * Map hinzufügen.
     * 
     * @param locale
     *                Language Code als Locale: de oder fr
     * @param text
     *                Bezeichnung in der entsprechenden Sprache
     * 
     */
    public void addOrUpdateLabel(Locale locale, String text) {
        this.labels.put(locale.toString(), text);
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
     * Gibt den Wert des Attributs <code>listIndex</code> zurück.
     * 
     * @return Wert des Attributs listIndex.
     */
    public Integer getListIndex() {
        return listIndex;
    }

    /**
     * Setzt den Wert des Attributs <code>listIndex</code>.
     * 
     * @param listIndex
     *                Wert für das Attribut listIndex.
     */
    public void setListIndex(Integer listIndex) {
        this.listIndex = listIndex;
    }

}
