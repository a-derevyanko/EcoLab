package org.ecolab.server.model;

import com.google.common.net.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Locale;

public class EcoLabUserDetails extends User  {
    private final Long id;
    private final Locale locale;
    private final String ip;

    public EcoLabUserDetails(Long id, String username, String password, boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        this.locale = request.getLocale();

        String ip = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        this.ip = ip;
    }

    public Long getId() {
        return id;
    }

    public Locale getLocale() {
        return locale;
    }
}
