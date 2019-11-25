package org.ihiw.management.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Management microservice entities
 */
@ApiModel(description = "Management microservice entities")
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "modified_at")
    private ZonedDateTime modifiedAt;

    @ManyToOne
    @JsonIgnoreProperties("projects")
    private IhiwUser createdBy;

    @ManyToOne
    @JsonIgnoreProperties("projects")
    private IhiwUser modifiedBy;

    @Column(name= "activated")
    private Boolean activated;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "project_lab",
               joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "lab_id", referencedColumnName = "id"))
    private Set<IhiwLab> labs = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "project_leader",
        joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "ihiw_user_id", referencedColumnName = "id"))
    private Set<IhiwUser> leaders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Project description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Project createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public Project modifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Boolean getActivated() {
        return activated;
    }

    public Project activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public IhiwUser getCreatedBy() {
        return createdBy;
    }

    public Project createdBy(IhiwUser ihiwUser) {
        this.createdBy = ihiwUser;
        return this;
    }

    public void setCreatedBy(IhiwUser ihiwUser) {
        this.createdBy = ihiwUser;
    }

    public IhiwUser getModifiedBy() {
        return modifiedBy;
    }

    public Project modifiedBy(IhiwUser ihiwUser) {
        this.modifiedBy = ihiwUser;
        return this;
    }

    public void setModifiedBy(IhiwUser ihiwUser) {
        this.modifiedBy = ihiwUser;
    }

    public Set<IhiwLab> getLabs() {
        return labs;
    }

    public Project labs(Set<IhiwLab> ihiwLabs) {
        this.labs = ihiwLabs;
        return this;
    }

    public Project addLab(IhiwLab ihiwLab) {
        this.labs.add(ihiwLab);
        ihiwLab.getProjects().add(this);
        return this;
    }

    public Project removeLab(IhiwLab ihiwLab) {
        this.labs.remove(ihiwLab);
        ihiwLab.getProjects().remove(this);
        return this;
    }

    public void setLabs(Set<IhiwLab> ihiwLabs) {
        this.labs = ihiwLabs;
    }

    public Set<IhiwUser> getLeaders() {
        return leaders;
    }

    public Project leaders(Set<IhiwUser> leader) {
        this.leaders = leader;
        return this;
    }

    public Project addLeader(IhiwUser leader) {
        this.leaders.add(leader);
        leader.getProjectLeaderships().add(this);
        return this;
    }

    public Project removeLeader(IhiwUser leader) {
        this.leaders.remove(leader);
        leader.getProjectLeaderships().remove(this);
        return this;
    }

    public void setLeaders(Set<IhiwUser> leaders) {
        this.leaders = leaders;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            "}";
    }
}
