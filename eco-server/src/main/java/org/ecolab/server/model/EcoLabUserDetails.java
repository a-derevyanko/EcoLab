package org.ecolab.server.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Locale;

public class EcoLabUserDetails extends User  {
    private final Long id;
    private final Locale locale = Locale.getDefault();

    public EcoLabUserDetails(Long id, String username, String password, boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Locale getLocale() {
        return locale;
    }
}
