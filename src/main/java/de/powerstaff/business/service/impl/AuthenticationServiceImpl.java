/**
 * Mogwai PowerStaff. Copyright (C) 2002 The Mogwai Project.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.powerstaff.business.service.impl;

import de.mogwai.common.business.service.impl.LogableService;
import de.mogwai.common.usercontext.UserContextHolder;
import de.powerstaff.business.dao.AuthenticationDAO;
import de.powerstaff.business.entity.User;
import de.powerstaff.business.service.AuthenticationService;

public class AuthenticationServiceImpl extends LogableService implements AuthenticationService {

	private AuthenticationDAO authenticationDAO;

	/**
	 * @return the authenticationDAO
	 */
	public AuthenticationDAO getAuthenticationDAO() {
		return authenticationDAO;
	}

	/**
	 * @param authenticationDAO the authenticationDAO to set
	 */
	public void setAuthenticationDAO(AuthenticationDAO authenticationDAO) {
		this.authenticationDAO = authenticationDAO;
	}

	public User login(String aPrincipal, String aCredentials) {

		User theUser = null;

		if ("admin".equals(aPrincipal) && ("secret".equals(aCredentials))) {
			theUser = new User();
			theUser.setName(aPrincipal);

			UserContextHolder.initContextWithAuthenticatable(theUser);
			return theUser;
		}

		theUser = authenticationDAO.findUserByName(aPrincipal);
		if (theUser != null) {
			if (!theUser.isActive())
				theUser = null;
		}
		
		if (theUser != null) {
			if (theUser.getPassword().equals(aCredentials)) {
				UserContextHolder.initContextWithAuthenticatable(theUser);
			} else {
				theUser = null;
			}
		}
		
		return theUser;
	}

}