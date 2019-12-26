package com.haylen.pan.bo;

import com.haylen.pan.entity.Owner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author haylen
 * @date 2019-12-26
 */
public class OwnerDetails implements UserDetails {
    private Owner owner;

    public OwnerDetails(Owner owner) {
        this.owner = owner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return owner.getPassword();
    }

    @Override
    public String getUsername() {
        return owner.getUsername();
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
