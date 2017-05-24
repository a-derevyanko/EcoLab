package org.ekolab.server.common;

/**
 * Created by 777Al on 28.03.2017.
 */
public class Authorize {
    public class HasAuthorities {
        private static final String HASAUTHORITY_PREFIX = "hasAuthority('";
        private static final String HASAUTHORITY_POSTFIX = "')";

        public static final String ADMIN = HASAUTHORITY_PREFIX + Authorities.ADMIN + HASAUTHORITY_POSTFIX;
        public static final String TEACHER = HASAUTHORITY_PREFIX + Authorities.TEACHER + HASAUTHORITY_POSTFIX;
        public static final String STUDENT = HASAUTHORITY_PREFIX + Authorities.STUDENT + HASAUTHORITY_POSTFIX;
    }
    public class Authorities {
        public static final String ADMIN = "ADMIN";
        public static final String TEACHER = "TEACHER";
        public static final String STUDENT = "STUDENT";
    }
}
