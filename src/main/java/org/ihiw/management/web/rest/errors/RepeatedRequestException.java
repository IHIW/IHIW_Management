package org.ihiw.management.web.rest.errors;

public class RepeatedRequestException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public RepeatedRequestException() {
        super(ErrorConstants.DEFAULT_TYPE, "IP is blacklisted for repeated requests!", "request", "repeatedrequest");
    }
}
