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
package de.mogwai.common.web.backingbean.example;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import de.mogwai.common.web.backingbean.BackingBean;
import de.mogwai.common.web.navigation.ModalPageDescriptor;

public class ModalOverlayBackingBean extends BackingBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5226026699926668679L;

	private String wert1;

    private String wert2;

    private String wert3;

    private String wert4;

    private boolean wert5;

    private String wert6;

    private String wert7;

    private List<String> werte2 = new Vector<String>();

    private List<String> werte = new Vector<String>();

    private Date datum;

    private Integer number;

    private ModalPageDescriptor messageBox = new ModalPageDescriptor("/modal/modaledit.jsp", null);

    public ModalOverlayBackingBean() {
        for (int i = 0; i < 10; i++) {
            werte.add("Wert " + i);
        }
    }

    public void startModal() {

        launchMessageDialog(messageBox, ModalOverlayBackingBean.class, "", "");
    }

    public void stopModal() {
        shutdownModalDialog();
    }

    private int counter = 1;

    public void submit() {

        wert1 = "" + counter++;
    }

    /**
     * Gibt den Wert des Attributs <code>wert1</code> zurück.
     * 
     * @return Wert des Attributs wert1.
     */
    public String getWert1() {
        return wert1;
    }

    /**
     * Setzt den Wert des Attributs <code>wert1</code>.
     * 
     * @param wert1
     *                Wert für das Attribut wert1.
     */
    public void setWert1(String wert1) {
        this.wert1 = wert1;
    }

    /**
     * Gibt den Wert des Attributs <code>wert2</code> zurück.
     * 
     * @return Wert des Attributs wert2.
     */
    public String getWert2() {
        return wert2;
    }

    /**
     * Setzt den Wert des Attributs <code>wert2</code>.
     * 
     * @param wert2
     *                Wert für das Attribut wert2.
     */
    public void setWert2(String wert2) {
        this.wert2 = wert2;
    }

    /**
     * Gibt den Wert des Attributs <code>werte</code> zurück.
     * 
     * @return Wert des Attributs werte.
     */
    public List<String> getWerte() {
        return werte;
    }

    /**
     * Setzt den Wert des Attributs <code>werte</code>.
     * 
     * @param werte
     *                Wert für das Attribut werte.
     */
    public void setWerte(List<String> werte) {
        this.werte = werte;
    }

    public String getInfoText() {
        return wert1 + " " + wert2;
    }

    /**
     * Gibt den Wert des Attributs <code>wert3</code> zurück.
     * 
     * @return Wert des Attributs wert3.
     */
    public String getWert3() {
        return wert3;
    }

    /**
     * Setzt den Wert des Attributs <code>wert3</code>.
     * 
     * @param wert3
     *                Wert für das Attribut wert3.
     */
    public void setWert3(String wert3) {
        this.wert3 = wert3;
    }

    /**
     * Gibt den Wert des Attributs <code>wert4</code> zurück.
     * 
     * @return Wert des Attributs wert4.
     */
    public String getWert4() {
        return wert4;
    }

    /**
     * Setzt den Wert des Attributs <code>wert4</code>.
     * 
     * @param wert4
     *                Wert für das Attribut wert4.
     */
    public void setWert4(String wert4) {
        this.wert4 = wert4;
    }

    /**
     * Gibt den Wert des Attributs <code>wert5</code> zurück.
     * 
     * @return Wert des Attributs wert5.
     */
    public boolean isWert5() {
        return wert5;
    }

    /**
     * Setzt den Wert des Attributs <code>wert5</code>.
     * 
     * @param wert5
     *                Wert für das Attribut wert5.
     */
    public void setWert5(boolean wert5) {
        this.wert5 = wert5;
    }

    /**
     * Gibt den Wert des Attributs <code>wert6</code> zurück.
     * 
     * @return Wert des Attributs wert6.
     */
    public String getWert6() {
        return wert6;
    }

    /**
     * Setzt den Wert des Attributs <code>wert6</code>.
     * 
     * @param wert6
     *                Wert für das Attribut wert6.
     */
    public void setWert6(String wert6) {
        this.wert6 = wert6;
    }

    /**
     * Gibt den Wert des Attributs <code>werte2</code> zurück.
     * 
     * @return Wert des Attributs werte2.
     */
    public List<String> getWerte2() {
        return werte2;
    }

    /**
     * Setzt den Wert des Attributs <code>werte2</code>.
     * 
     * @param werte2
     *                Wert für das Attribut werte2.
     */
    public void setWerte2(List<String> werte2) {
        this.werte2 = werte2;
    }

    /**
     * Gibt den Wert des Attributs <code>datum</code> zurück.
     * 
     * @return Wert des Attributs datum.
     */
    public Date getDatum() {
        return datum;
    }

    /**
     * Setzt den Wert des Attributs <code>datum</code>.
     * 
     * @param datum
     *                Wert für das Attribut datum.
     */
    public void setDatum(Date datum) {
        this.datum = datum;
    }

    /**
     * Gibt den Wert des Attributs <code>number</code> zurück.
     * 
     * @return Wert des Attributs number.
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Setzt den Wert des Attributs <code>number</code>.
     * 
     * @param number
     *                Wert für das Attribut number.
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * Gibt den Wert des Attributs <code>wert7</code> zurück.
     * 
     * @return Wert des Attributs wert7.
     */
    public String getWert7() {
        return wert7;
    }

    /**
     * Setzt den Wert des Attributs <code>wert7</code>.
     * 
     * @param wert7
     *                Wert für das Attribut wert7.
     */
    public void setWert7(String wert7) {
        this.wert7 = wert7;
    }
}
