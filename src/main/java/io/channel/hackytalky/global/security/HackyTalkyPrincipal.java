package io.channel.hackytalky.global.security;

import io.channel.hackytalky.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
public class HackyTalkyPrincipal implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    @Getter private String email;

    public HackyTalkyPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities, String email) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.email = email;
    }

    public static HackyTalkyPrincipal buildHackyTalkyPrincipalEntity(String username, String password, String email) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new HackyTalkyPrincipal(
                username,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                email
        );
    }

    public static HackyTalkyPrincipal createHackyTalkyPrincipalByUserEntity(User user) {
        return HackyTalkyPrincipal.buildHackyTalkyPrincipalEntity(
                user.getName(),
                user.getPassword(),
                user.getEmail()
        );
    }

    public enum RoleType {
        USER("ROLE_USER"),
        ADMIN("ROLE_ADMIN");

        @Getter
        private final String name;
        RoleType(String name) { this.name = name; }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
