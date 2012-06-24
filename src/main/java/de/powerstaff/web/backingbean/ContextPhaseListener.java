package de.powerstaff.web.backingbean;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Phase listener welcher das aktuelle Projekt aus den ContextUtils wieder entfernt.
 */
public class ContextPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        ContextUtils.cleanupCache();
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
