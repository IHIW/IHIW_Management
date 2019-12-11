package org.ihiw.management.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.ihiw.management.domain.enumeration.ProjectSubscriptionStatus;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "project_lab")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectIhiwLab implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    @JsonBackReference
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private IhiwLab lab;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectSubscriptionStatus status;

    public ProjectIhiwLab() {
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public IhiwLab getLab() {
        return lab;
    }

    public void setLab(IhiwLab lab) {
        this.lab = lab;
    }

    public ProjectSubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectSubscriptionStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectIhiwLab that = (ProjectIhiwLab) o;

        if (project != null ? !project.equals(that.project) : that.project != null) return false;
        if (lab != null ? !lab.equals(that.lab) : that.lab != null) return false;
        return status == that.status;

    }

    @Override
    public int hashCode() {
        int result = project != null ? project.hashCode() : 0;
        result = 31 * result + (lab != null ? lab.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
