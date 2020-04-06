package org.ihiw.management.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.domain.Project;
import org.ihiw.management.domain.ProjectIhiwLab;
import org.ihiw.management.domain.enumeration.ProjectComponent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class ProjectDTO {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private ProjectComponent component;

    @Size(max = 50)
    @Lob
    private String description;


    private ZonedDateTime createdAt;


    private ZonedDateTime modifiedAt;

    @ManyToOne
    @JsonIgnoreProperties(value = "projects", allowSetters = true)
    private IhiwUser createdBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "projects", allowSetters = true)
    private IhiwUser modifiedBy;

    @Column(name= "activated")
    private Boolean activated;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonManagedReference
    private Set<ProjectIhiwLab> labs = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "project_leader",
        joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "ihiw_user_id", referencedColumnName = "id"))
    private Set<IhiwUser> leaders = new HashSet<>();

    public ProjectDTO() {
        // Empty constructor needed for Jackson.
    }

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.component = project.getComponent();
        this.description = project.getDescription();
        this.activated = project.getActivated();
        this.createdAt = project.getCreatedAt();
        this.createdBy = project.getCreatedBy();
        this.modifiedAt = project.getModifiedAt();
        this.modifiedBy = project.getModifiedBy();
        this.labs = project.getLabs();
        this.leaders = project.getLeaders();

    }


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

    public ProjectDTO name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectComponent getComponent() {
        return component;
    }

    public ProjectDTO component(ProjectComponent component) {
        this.component = component;
        return this;
    }

    public void setComponent(ProjectComponent component) {
        this.component = component;
    }


    public String getDescription() {
        return description;
    }

    public ProjectDTO description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ProjectDTO createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public ProjectDTO modifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Boolean getActivated() {
        return activated;
    }

    public ProjectDTO activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public IhiwUser getCreatedBy() {
        return createdBy;
    }

    public ProjectDTO createdBy(IhiwUser ihiwUser) {
        this.createdBy = ihiwUser;
        return this;
    }

    public void setCreatedBy(IhiwUser ihiwUser) {
        this.createdBy = ihiwUser;
    }

    public IhiwUser getModifiedBy() {
        return modifiedBy;
    }

    public ProjectDTO modifiedBy(IhiwUser ihiwUser) {
        this.modifiedBy = ihiwUser;
        return this;
    }

    public void setModifiedBy(IhiwUser ihiwUser) {
        this.modifiedBy = ihiwUser;
    }

    public Set<ProjectIhiwLab> getLabs() {
        return labs;
    }

    public ProjectDTO labs(Set<ProjectIhiwLab> ihiwLabs) {
        this.labs = ihiwLabs;
        return this;
    }

    public ProjectDTO addLab(ProjectIhiwLab ihiwLab) {
        this.labs.add(ihiwLab);
        ihiwLab.getLab().getProjects().add(ihiwLab);
        return this;
    }

    public ProjectDTO removeLab(ProjectIhiwLab ihiwLab) {
        this.labs.remove(ihiwLab);
        ihiwLab.getLab().getProjects().remove(this);
        return this;
    }

    public void setLabs(Set<ProjectIhiwLab> ihiwLabs) {
        this.labs = ihiwLabs;
    }

    public Set<IhiwUser> getLeaders() {
        return leaders;
    }

    public ProjectDTO leaders(Set<IhiwUser> leader) {
        this.leaders = leader;
        return this;
    }

//    public ProjectDTO addLeader(IhiwUser leader) {
//        this.leaders.add(leader);
//        leader.getProjectLeaderships().add(ProjectDTOToProject(this));
//        return this;
//    }

    public ProjectDTO removeLeader(IhiwUser leader) {
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
        return id != null && id.equals(((ProjectDTO) o).id);
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
            ", component='" + getComponent() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            "}";
    }

}
