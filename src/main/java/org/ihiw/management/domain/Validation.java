package org.ihiw.management.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "validation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Validation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Upload upload;

    @Column(name = "valid")
    private Boolean valid;

    @Column(name = "validation_feedback")
    private String validationFeedback;

    @Column(name = "validator")
    private String validator;

    public Validation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Upload getUpload() {
        return upload;
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getValidationFeedback() {
        return validationFeedback;
    }

    public void setValidationFeedback(String validationFeedback) {
        this.validationFeedback = validationFeedback;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }
}
