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
package de.powerstaff.business.entity;

import java.sql.Timestamp;

import de.mogwai.common.business.entity.AuditableEntity;

public class ProjectPosition extends AuditableEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3748584707902354798L;

	private Project project;

    private Freelancer freelancer;

    private boolean informed;

    private Timestamp informedDate;

    private boolean presented;

    private Timestamp presentedDate;

    private boolean ordered;

    private Timestamp canceledDate;

    private boolean canceled;

    private String comment;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Timestamp getCanceledDate() {
        return canceledDate;
    }

    public void setCanceledDate(Timestamp canceledDate) {
        this.canceledDate = canceledDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Freelancer getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(Freelancer freelancer) {
        this.freelancer = freelancer;
    }

    public boolean isInformed() {
        return informed;
    }

    public void setInformed(boolean informed) {
        this.informed = informed;
    }

    public Timestamp getInformedDate() {
        return informedDate;
    }

    public void setInformedDate(Timestamp informedDate) {
        this.informedDate = informedDate;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public boolean isPresented() {
        return presented;
    }

    public void setPresented(boolean presented) {
        this.presented = presented;
    }

    public Timestamp getPresentedDate() {
        return presentedDate;
    }

    public void setPresentedDate(Timestamp presentedDate) {
        this.presentedDate = presentedDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
