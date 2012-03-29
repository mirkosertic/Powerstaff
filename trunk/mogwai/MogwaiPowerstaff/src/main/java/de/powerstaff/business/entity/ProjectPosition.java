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

import de.mogwai.common.business.entity.AuditableEntity;

public class ProjectPosition extends AuditableEntity implements Comparable<ProjectPosition> {

    private static final long serialVersionUID = 3748584707902354798L;

    private long freelancerId;

    private ProjectPositionStatus status;

    private String conditions;

    private String comment;

    private Project project;

    public long getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(long freelancerId) {
        this.freelancerId = freelancerId;
    }

    public ProjectPositionStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectPositionStatus status) {
        this.status = status;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int compareTo(ProjectPosition o) {
        return getCreationDate().compareTo(o.getCreationDate());


    }
}