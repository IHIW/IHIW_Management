package org.ihiw.management.web.rest.errors;

public class LabDoesNotExistException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LabDoesNotExistException() {
        super(ErrorConstants.DEFAULT_TYPE, "Labcode does not exist!", "userManagement", "labnotexists");
    }
}
