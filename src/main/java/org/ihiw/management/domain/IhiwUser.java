package org.ihiw.management.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A IhiwUser.
 */
@Entity
@Table(name = "ihiw_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IhiwUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Upload> uploads = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("ihiwUsers")
    private IhiwLab lab;

    @ManyToMany(mappedBy = "leaders")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Project> projectLeaderships = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public IhiwUser phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public IhiwUser user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Set<Upload> getUploads() {
        return uploads;
    }

    public IhiwUser uploads(Set<Upload> uploads) {
        this.uploads = uploads;
        return this;
    }

    public IhiwUser addUpload(Upload upload) {
        this.uploads.add(upload);
        upload.setCreatedBy(this);
        return this;
    }

    public IhiwUser removeUpload(Upload upload) {
        this.uploads.remove(upload);
        upload.setCreatedBy(null);
        return this;
    }

    public void setUploads(Set<Upload> uploads) {
        this.uploads = uploads;
    }

    public IhiwLab getLab() {
        return lab;
    }

    public IhiwUser lab(IhiwLab ihiwLab) {
        this.lab = ihiwLab;
        return this;
    }

    public void setLab(IhiwLab ihiwLab) {
        this.lab = ihiwLab;
    }

    public Set<Project> getProjectLeaderships() {
        return projectLeaderships;
    }

    public IhiwUser projectLeaderships(Set<Project> projectLeaderships) {
        this.projectLeaderships = projectLeaderships;
        return this;
    }

    public IhiwUser addProjectLeadership(Project project) {
        this.projectLeaderships.add(project);
        project.getLeaders().add(this);
        return this;
    }

    public IhiwUser removeProjectLeadership(Project project) {
        this.projectLeaderships.remove(project);
        project.getLeaders().remove(this);
        return this;
    }

    public void setProjectLeaderships(Set<Project> projectLeaderships) {
        this.projectLeaderships = projectLeaderships;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IhiwUser)) {
            return false;
        }
        return id != null && id.equals(((IhiwUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "IhiwUser{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
