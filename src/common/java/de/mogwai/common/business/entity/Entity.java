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

import org.hibernate.search.annotations.DocumentId;

import java.io.Serializable;

/**
 * Basisklasse für Geschäftobjekte (Entities).
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:16:30 $
 */
public abstract class Entity implements Serializable {

	private static final long serialVersionUID = 3256446889040622647L;

    @DocumentId
	private Long id;

	private Long version;

	/**
	 * Ermitteln der Id der Entität.
	 * 
	 * @return die Id der Entität
	 */
	public final Long getId() {
		return id;
	}

	/**
	 * Setzen der Id der Entität.
	 * 
	 * @param id
	 *            die Id
	 */
	public final void setId(Long id) {
		this.id = id;
	}

	/**
	 * Setzen der Id der Entität.
	 * 
	 * @param id
	 *            die Id
	 */
	public final void setId(long id) {
		this.id = id;
	}

	/**
	 * Gibt den Wert des Attributs <code>version</code> zurück.
	 * 
	 * @return Wert des Attributs version.
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * Setzt den Wert des Attributs <code>version</code>.
	 * 
	 * @param version
	 *            Wert für das Attribut version.
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * Objekte sind dann gleich, wenn entweder die Referenz identisch ist oder
	 * beide persistenten Objekte die selbe ID haben und der selben Klasse
	 * angehören.
	 * 
	 * Kann bei Bedarf durch ein fachliches Equal überschrieben werden, dann
	 * muss jedoch auch hashCode überschrieben werden !!!!!!!
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param o
	 *            see java.lang.Object#equals(java.lang.Object)
	 * @return see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {

		if (o == null) {
			return false;
		}

		// Wenn die Referenzen gleich sind, sind auch die Objekte gleich
		if (o == this) {
			return true;
		}

		// Wenn das Objekt persistent ist ( hat eine Id ), und beide Objekte der
		// selben Klasse sind,
		// dann sind diese Objekte gleich, wenn sie die selbe Id haben
		if ((id != null) && (o.getClass().equals(getClass()))) {
			return id.equals(((Entity) o).getId());
		}

		return false;
	}

	/**
	 * Siehe Bug 538.
	 * 
	 * Per Default ist der Hashcode immer 0. Dieses kann jedoch von einer
	 * Unterklasse überschrieben werden, sobald ein immutable natürlicher
	 * Schlüssel verfügbar ist.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 0;
	}
}
