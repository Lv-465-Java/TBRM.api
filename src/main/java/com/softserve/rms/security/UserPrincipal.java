package com.softserve.rms.security;


import com.softserve.rms.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

/**
 * Class that implements UserDetails interface.
 *
 * @author Artur Sydor
 */
public class UserPrincipal implements OAuth2User, UserDetails{
    /**
     * Represents User entity.
     */
    private User user;
    private Map<String, Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor with parameters.
     *
     * @param user object of type user
     */
    public UserPrincipal(User user) {
        this.user = user;
    }

    public UserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public UserPrincipal(User user,  Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
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

    /**
     * Overridden method Equals.
     *
     * @param o object for comparing
     * @return true if object are equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPrincipal that = (UserPrincipal) o;

        return Objects.equals(user, that.user);
    }

    /**
     * Overridden method HashCode.
     *
     * @return hashCode of object user
     */
    @Override
    public int hashCode() {
        return user != null ? user.hashCode() : 0;
    }

    /**
     * get id value from user
     * @return string {@link String}
     */
    @Override
    public String getName() {
        return String.valueOf(user.getId());
    }

    /**
     * method set attributes for user principal
     * @param attributes {@link Map}
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * method create user principal with authorities
     * @param user {@link User}
     * @return userPrincipal {@link UserPrincipal}
     */
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user,
                authorities
        );
    }

    /**
     * method create user principal with attributes
     * @param user {@link User}
     * @param attributes {@link Map}
     * @return userPrincipal {@link UserPrincipal}
     */
    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);

        return userPrincipal;
    }
}