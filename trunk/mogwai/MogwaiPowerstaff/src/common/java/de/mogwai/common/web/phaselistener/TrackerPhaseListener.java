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
package de.mogwai.common.web.phaselistener;

import de.mogwai.common.logging.Logger;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletRequest;

public class TrackerPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 4017505684182951105L;

    private static final Logger LOGGER = new Logger(TrackerPhaseListener.class);

    private ThreadLocal<Long> start = new ThreadLocal<Long>();

    public void afterPhase(PhaseEvent aEvent) {
        long theDuration = System.currentTimeMillis() - start.get();

        ServletRequest theRequest = (ServletRequest) aEvent.getFacesContext().getExternalContext().getRequest();
        LOGGER.logDebug(theRequest.getRemoteAddr() + " " + theRequest.getRemoteHost() + " Duration for Phase "
                + aEvent.getPhaseId() + " was " + theDuration);

    }

    public void beforePhase(PhaseEvent event) {
        start.set(System.currentTimeMillis());
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
