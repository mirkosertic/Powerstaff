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
package de.mogwai.common.web.utils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.faces.model.ListDataModel;

/**
 * DataModel, welches Collections als Datenquelle unterstützt.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:15:56 $
 * @param <T>
 *            Typ, für den die Collection spezialisiert wird
 */
public class CollectionDataModel<T> extends ListDataModel implements
		Collection<T>, Externalizable {

	protected Set<T> wrappedSet;

	public CollectionDataModel() {
		super();
	}

	public CollectionDataModel(Set<T> aSet) {
		super(new Vector<T>(aSet));

		wrappedSet = aSet;
	}

	public CollectionDataModel(List<T> aList) {
		super(aList);
	}

	@SuppressWarnings("unchecked")
	protected Collection<T> getAsCollection() {
		return (Collection<T>) getWrappedData();
	}

	public boolean add(T o) {

		if (wrappedSet != null) {
			wrappedSet.add(o);
		}

		return getAsCollection().add(o);
	}

	@SuppressWarnings("unchecked")
	public boolean addAll(Collection c) {

		if (wrappedSet != null) {
			wrappedSet.addAll(c);
		}

		return getAsCollection().addAll(c);
	}

	public void clear() {

		if (wrappedSet != null) {
			wrappedSet.clear();
		}

		getAsCollection().clear();
	}

	public boolean contains(Object o) {
		return getAsCollection().contains(o);
	}

	public boolean containsAll(Collection c) {
		return getAsCollection().containsAll(c);
	}

	public boolean isEmpty() {
		return getAsCollection().isEmpty();
	}

	public Iterator<T> iterator() {
		return getAsCollection().iterator();
	}

	public boolean remove(Object o) {

		if (wrappedSet != null) {
			wrappedSet.remove(o);
		}

		return getAsCollection().remove(o);
	}

	public boolean removeAll(Collection c) {

		if (wrappedSet != null) {
			wrappedSet.removeAll(c);
		}

		return getAsCollection().removeAll(c);
	}

	public boolean retainAll(Collection c) {

		if (wrappedSet != null) {
			wrappedSet.retainAll(c);
		}

		return getAsCollection().retainAll(c);
	}

	@SuppressWarnings("unchecked")
	public void sort(Comparator aComparator) {
		Collections.sort((List) getWrappedData(), aComparator);
	}

	public int size() {
		return getAsCollection().size();
	}

	public Object[] toArray() {
		return getAsCollection().toArray();
	}

	@SuppressWarnings("unchecked")
	public Object[] toArray(Object[] a) {
		return getAsCollection().toArray(a);
	}

	public void readExternal(ObjectInput aInput) throws IOException,
			ClassNotFoundException {
		setWrappedData(aInput.readObject());
		wrappedSet = (Set<T>) aInput.readObject();
	}

	public void writeExternal(ObjectOutput aOutput) throws IOException {
		aOutput.writeObject(getWrappedData());
		aOutput.writeObject(wrappedSet);
	}
}
