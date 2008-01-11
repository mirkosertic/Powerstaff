package de.powerstaff.web.utils;

import java.util.Comparator;

import org.apache.commons.collections.comparators.ReverseComparator;

import de.mogwai.common.business.entity.AuditableEntity;
import de.powerstaff.business.entity.Contact;

public final class Comparators {

	public static Comparator CREATIONDATECOMPARATOR = new Comparator() {

		public int compare(Object aObject1, Object aObject2) {
			if ((aObject1 == null) || (aObject2 == null)) {
				return 0;
			}
			
			AuditableEntity theE1 = (AuditableEntity)aObject1;
			AuditableEntity theE2 = (AuditableEntity)aObject2;
			
			return theE1.getCreationDate().compareTo(theE2.getCreationDate());
		}
		
	};
	
	public static Comparator INVERSECREATIONDATECOMPARATOR = new ReverseComparator(CREATIONDATECOMPARATOR);
	
	public static Comparator CONTACTCOMPARATOR = new Comparator() {

		public int compare(Object aObject1, Object aObject2) {
			if ((aObject1 == null) || (aObject2 == null)) {
				return 0;
			}
			
			Contact theE1 = (Contact)aObject1;
			Contact theE2 = (Contact)aObject2;
			
			return theE1.getValue().compareTo(theE2.getValue());
		}
		
	};
	
}
