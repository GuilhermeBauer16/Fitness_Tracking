package com.github.GuilhermeBauer16.FitnessTracking.model;


import com.github.GuilhermeBauer16.FitnessTracking.enums.UserProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;

    @Column(name = "user_profile")
    @Enumerated(EnumType.STRING)
    private UserProfile userProfile;

    public UserEntity(String id, String name, String email, String password, UserProfile userProfile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userProfile = userProfile;
    }

    public UserEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.singleton(new SimpleGrantedAuthority(userProfile.getUserProfile()));

    }


    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
