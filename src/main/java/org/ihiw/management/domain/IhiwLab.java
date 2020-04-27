package org.ihiw.management.domain;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A IhiwLab.
 */
@Entity
@Table(name = "ihiw_lab")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IhiwLab implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "lab_code", nullable = false)
    private String labCode;

    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "director")
    private String director;

    @Column(name = "department")
    private String department;

    @Column(name = "institution")
    private String institution;

    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "s_address_1")
    private String sAddress1;

    @Column(name = "s_address")
    private String sAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    private String zip;

    @Column(name = "country")
    private String country;

    @Column(name = "phone")
    private String phone;

    @Column(name = "fax")
    private String fax;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "url")
    private String url;

    @Column(name = "old_lab_code")
    private String oldLabCode;

    @Column(name = "s_name")
    private String sName;

    @Column(name = "s_phone")
    private String sPhone;

    @Column(name = "s_email")
    private String sEmail;

    @Column(name = "d_name")
    private String dName;

    @Column(name = "d_email")
    private String dEmail;

    @Column(name = "d_phone")
    private String dPhone;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonBackReference
    private Set<ProjectIhiwLab> projects = new HashSet<>();

    @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//    @JsonBackReference
    private Set<IhiwUser> ihiwUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabCode() {
        return labCode;
    }

    public IhiwLab labCode(String labCode) {
        this.labCode = labCode;
        return this;
    }

    public void setLabCode(String labCode) {
        this.labCode = labCode;
    }

    public String getTitle() {
        return title;
    }

    public IhiwLab title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public IhiwLab firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public IhiwLab lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDirector() {
        return director;
    }

    public IhiwLab director(String director) {
        this.director = director;
        return this;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDepartment() {
        return department;
    }

    public IhiwLab department(String department) {
        this.department = department;
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInstitution() {
        return institution;
    }

    public IhiwLab institution(String institution) {
        this.institution = institution;
        return this;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getAddress1() {
        return address1;
    }

    public IhiwLab address1(String address1) {
        this.address1 = address1;
        return this;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public IhiwLab address2(String address2) {
        this.address2 = address2;
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getsAddress1() {
        return sAddress1;
    }

    public IhiwLab sAddress1(String sAddress1) {
        this.sAddress1 = sAddress1;
        return this;
    }

    public void setsAddress1(String sAddress1) {
        this.sAddress1 = sAddress1;
    }

    public String getsAddress() {
        return sAddress;
    }

    public IhiwLab sAddress(String sAddress) {
        this.sAddress = sAddress;
        return this;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getCity() {
        return city;
    }

    public IhiwLab city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public IhiwLab state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public IhiwLab zip(String zip) {
        this.zip = zip;
        return this;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public IhiwLab country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public IhiwLab phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public IhiwLab fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public IhiwLab email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public IhiwLab url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOldLabCode() {
        return oldLabCode;
    }

    public IhiwLab oldLabCode(String oldLabCode) {
        this.oldLabCode = oldLabCode;
        return this;
    }

    public void setOldLabCode(String oldLabCode) {
        this.oldLabCode = oldLabCode;
    }

    public String getsName() {
        return sName;
    }

    public IhiwLab sName(String sName) {
        this.sName = sName;
        return this;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsPhone() {
        return sPhone;
    }

    public IhiwLab sPhone(String sPhone) {
        this.sPhone = sPhone;
        return this;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getsEmail() {
        return sEmail;
    }

    public IhiwLab sEmail(String sEmail) {
        this.sEmail = sEmail;
        return this;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getdName() {
        return dName;
    }

    public IhiwLab dName(String dName) {
        this.dName = dName;
        return this;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdEmail() {
        return dEmail;
    }

    public IhiwLab dEmail(String dEmail) {
        this.dEmail = dEmail;
        return this;
    }

    public void setdEmail(String dEmail) {
        this.dEmail = dEmail;
    }

    public String getdPhone() {
        return dPhone;
    }

    public IhiwLab dPhone(String dPhone) {
        this.dPhone = dPhone;
        return this;
    }

    public void setdPhone(String dPhone) {
        this.dPhone = dPhone;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public IhiwLab createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Set<IhiwUser> getIhiwUsers() {
        return ihiwUsers;
    }

    public void setIhiwUsers(Set<IhiwUser> ihiwUsers) {
        this.ihiwUsers = ihiwUsers;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<ProjectIhiwLab> getProjects() {
        return projects;
    }

    public IhiwLab projects(Set<ProjectIhiwLab> projects) {
        this.projects = projects;
        return this;
    }

    public IhiwLab addProject(ProjectIhiwLab project) {
        this.projects.add(project);
        project.getProject().getLabs().add(project);
        return this;
    }

    public IhiwLab removeProject(ProjectIhiwLab project) {
        this.projects.remove(project);
        project.getProject().getLabs().remove(project);
        return this;
    }

    public void setProjects(Set<ProjectIhiwLab> projects) {
        this.projects = projects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IhiwLab)) {
            return false;
        }
        return id != null && id.equals(((IhiwLab) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "IhiwLab{" +
            "id=" + getId() +
            ", labCode='" + getLabCode() + "'" +
            ", title='" + getTitle() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", director='" + getDirector() + "'" +
            ", department='" + getDepartment() + "'" +
            ", institution='" + getInstitution() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", sAddress1='" + getsAddress1() + "'" +
            ", sAddress='" + getsAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zip='" + getZip() + "'" +
            ", country='" + getCountry() + "'" +
            ", phone='" + getPhone() + "'" +
            ", fax='" + getFax() + "'" +
            ", email='" + getEmail() + "'" +
            ", url='" + getUrl() + "'" +
            ", oldLabCode='" + getOldLabCode() + "'" +
            ", sName='" + getsName() + "'" +
            ", sPhone='" + getsPhone() + "'" +
            ", sEmail='" + getsEmail() + "'" +
            ", dName='" + getdName() + "'" +
            ", dEmail='" + getdEmail() + "'" +
            ", dPhone='" + getdPhone() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
