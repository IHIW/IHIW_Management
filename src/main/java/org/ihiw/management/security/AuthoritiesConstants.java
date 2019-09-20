package org.ihiw.management.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String LAB_MEMBER = "LabMember";
    public static final String PI = "PI";
    public static final String PROJECT_LEADER = "ProjectLeader";
    public static final String WORKSHOP_CHAIR = "WorkshopChair";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
