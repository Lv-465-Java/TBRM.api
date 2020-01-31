package com.softserve.rms.security;


import com.softserve.rms.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class that implements UserDetails interface.
 *
 * @author Artur Sydor
 */
public class UserPrincipal implements UserDetails {
    /**
     * Represents User entity.
     */
    private User user;

    /**
     * Constructor for
     *
     * @param user object of type user
     */
    public UserPrincipal(User user) {
        this.user = user;
    }

    /**
     * Get role from User object
     * and create new object of type GrantedAuthority.
     *
     * @return list of user`s authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return authorities;
    }

    /**
     * Get password form User object.
     *
     * @return user`s password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Get email from User object.
     *
     * @return user`s email
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Check if account is not expired.
     *
     * @return true if account is not expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return user.isEnabled();
    }

    /**
     * Check if account is not locked.
     *
     * @return true if account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return user.isEnabled();
    }

    /**
     * Check if credentials is not expired.
     *
     * @return true if credentials is not expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return user.isEnabled();
    }

    /**
     * Check if user is enabled.
     *
     * @return true if user is enabled
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}