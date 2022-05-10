package org.ihiw.management.domain;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import org.ihiw.management.domain.enumeration.FileType;

/**
 * An Upload.
 */
@Entity
@Table(name = "upload")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Upload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FileType type;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "modified_at")
    private ZonedDateTime modifiedAt;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToMany(mappedBy = "upload", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Validation> validations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("uploads")
    private IhiwUser createdBy;

    @ManyToOne
    private Project project;

	@Transient
    @JsonProperty
    private String rawDownload;

    @ManyToOne
    private Upload parentUpload;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileType getType() {
        return type;
    }

    public Upload type(FileType type) {
        this.type = type;
        return this;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Upload createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public Upload modifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getFileName() {
        return fileName;
    }

    public Upload fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Upload enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public IhiwUser getCreatedBy() {
        return createdBy;
    }

    public Upload createdBy(IhiwUser ihiwUser) {
        this.createdBy = ihiwUser;
        return this;
    }

    public void setCreatedBy(IhiwUser ihiwUser) {
        this.createdBy = ihiwUser;
    }

    public String getRawDownload() {
        return rawDownload;
    }

    public void setRawDownload(String rawDownload) {
        this.rawDownload = rawDownload;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Set<Validation> getValidations() {
        return validations;
    }

    public void setValidations(Set<Validation> validations) {
        this.validations = validations;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Upload getParentUpload() {
        return parentUpload;
    }

    public void setParentUpload(Upload parentUpload) {
        this.parentUpload = parentUpload;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Upload)) {
            return false;
        }
        return id != null && id.equals(((Upload) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Upload{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
