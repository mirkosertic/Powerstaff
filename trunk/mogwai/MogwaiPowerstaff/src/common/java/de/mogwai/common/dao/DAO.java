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
package de.mogwai.common.dao;

import de.powerstaff.business.service.OptimisticLockException;
import de.powerstaff.business.service.ReferenceExistsException;

/**
 * Data Access Object Interface.
 *
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:32:33 $
 */
public interface DAO {

    /**
     * Liest eine Geschäftsobjekt per ID.
     *
     * @param entityClass Typ des Geschäftsobjektes.
     * @param id          ID des zu lesenden Geschäftsobjektes.
     * @return Geschäftsobjekt oder null, falls kein Geschäftsobjekt mit dieser
     *         ID vorhanden ist.
     */
    Object getById(Class entityClass, Long id);

    /**
     * Speichert ein Geschäftsobjekt.
     *
     * @param entity zu speicherndes Geschäftsobjekt.
     */
    void save(Object entity) throws OptimisticLockException;

    /**
     * Löscht ein Geschäftsobjekt.
     *
     * @param entity zu löschendes Geschäftsobjekt.
     * @throws ReferenceExistsException wird geworfen, falls abhängige Daten existieren sollten.
     * @throws OptimisticLockException wird geworfen, falls der Datensatz zwischenzeitlich geändert wurde.
     */
    void delete(Object entity) throws ReferenceExistsException, OptimisticLockException;
}