package org.ekolab.server.common;

/**
 * Created by 777Al on 28.03.2017.
 */
public class Authorize {
    public class HasAuthorities {
        public static final String ADMIN = "hasAuthority('ADMIN')";
        public static final String TEACHER = "hasAuthority('TEACHER')";
        public static final String STUDENT = "hasAuthority('STUDENT')";
    }
}
