package com.example.hogwartsartifactsonline.hogwartsuser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;

public class MyUserPrincipal implements UserDetails {
    private HogwartsUser user;

    public MyUserPrincipal(HogwartsUser user) {
        this.user = user;
    }

    public HogwartsUser getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //first, split the roles by space, then map each role to a SimpleGrantedAuthority with "ROLE_" prefix
        return Arrays.stream(StringUtils.tokenizeToStringArray(user.getRoles(), " ")).map(
                role -> new SimpleGrantedAuthority("ROLE_" + role)
        ).toList();

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        //return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
