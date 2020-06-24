package org.ihiw.management.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.org.apache.xpath.internal.objects.XString;
//import jdk.vm.ci.meta.Local;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.ihiw.management.domain.enumeration.FileType;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "newentry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class NewEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Upload upload;

    @Column(name = "newentryname")
    private String name;

    @Column(name = "newentrycreated")
    private ZonedDateTime created_at;

    @Column(name = "newentrymodified")
    private ZonedDateTime modified_at;

    @Column(name = "newentrytype")
    private FileType type;

    public NewEntry() {
    }

    public NewEntry(Upload upload){
        name = upload.getFileName();
        created_at = upload.getCreatedAt();
        modified_at = upload.getModifiedAt();
        type = upload.getType();
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

    public String getName() {
        return name;
    }

    public void setName(String mystring){this.name = mystring;}

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }


    public ZonedDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(ZonedDateTime created_at) {
        this.created_at = created_at;
    }

    public ZonedDateTime getModified_at() {
        return modified_at;
    }

    public void setModified_at(ZonedDateTime modified_at) {
        this.modified_at = modified_at;
    }
}
