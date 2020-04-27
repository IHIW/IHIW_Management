package org.ihiw.management.service.dto;

import org.ihiw.management.domain.IhiwLab;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

public class LabDTO {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;

    @NotBlank
    //@Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String labCode;

    @Size(max = 50)
    private String title;

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Size(max = 50)
    private String director;

    @Size(max = 50)
    private String department;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 50)
    private String institution;

    @Size(max = 50)
    private String address1;

    @Size(max = 50)
    private String address2;

    @Size(max = 50)
    private String sAddress1;

    @Size(max = 50)
    private String sAddress;

    @Size(max = 50)
    private String city;

    @Size(max = 50)
    private String state;

    @Size(max = 50)
    private String zip;

    @Size(max = 100)
    private String country;

    @Size(max = 50)
    private String phone;

    @Size(max = 50)
    private String fax;

    @Size(max = 50)
    private String url;

    @Size(max = 50)
    private String oldLabCode;

    @Size(max = 50)
    private String sName;

    @Size(max = 50)
    private String sPhone;

    @Size(max = 50)
    private String sEmail;

    @Size(max = 50)
    private String dName;

    @Size(max = 50)
    private String dEmail;

    public String getLabCode() {
        return labCode;
    }

    public void setLabCode(String labCode) {
        this.labCode = labCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getsAddress1() {
        return sAddress1;
    }

    public void setsAddress1(String sAddress1) {
        this.sAddress1 = sAddress1;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOldLabCode() {
        return oldLabCode;
    }

    public void setOldLabCode(String oldLabCode) {
        this.oldLabCode = oldLabCode;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdEmail() {
        return dEmail;
    }

    public void setdEmail(String dEmail) {
        this.dEmail = dEmail;
    }

    public String getdPhone() {
        return dPhone;
    }

    public void setdPhone(String dPhone) {
        this.dPhone = dPhone;
    }

    @Size(max = 50)
    private String dPhone;

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Size(max = 200)
    private ZonedDateTime createdAt;


    public LabDTO() {
        // Empty constructor needed for Jackson.
    }

    public LabDTO(IhiwLab lab) {
        this.id = lab.getId();
        this.labCode = lab.getLabCode();
        this.firstName = lab.getFirstName();
        this.lastName = lab.getLastName();
        this.email = lab.getEmail();
        this.title = lab.getTitle();
        this.institution = lab.getInstitution();
        this.address1 = lab.getAddress1();
        this.address2 = lab.getAddress2();
        this.sAddress1 = lab.getsAddress1();
        this.sAddress = lab.getsAddress();
        this.city = lab.getCity();
        this.state = lab.getState();
        this.zip = lab.getZip();
        this.country=lab.getCountry();
        this.phone = lab.getPhone();
        this.fax = lab.getFax();
        this.url = lab.getUrl();
        this.oldLabCode = lab.getOldLabCode();
        this.dName = lab.getdName();
        this.dEmail= lab.getdEmail();
        this.dPhone = lab.getdPhone();
        this.createdAt = lab.getCreatedAt();
        this.department = lab.getDepartment();
        this.director = lab.getDirector();
        //this.authorities = lab.getAuthorities().stream()
        //    .map(Authority::getName)
        //    .collect(Collectors.toSet());
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
