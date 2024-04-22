package com.example.santa.domain.user.entity;

import com.example.santa.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Embedded
    private Password password;

    private String name;

    private String nickname;

    private String phoneNumber;

    private String image;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String email, Password password, String name, String nickname, String phoneNumber, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public User update(String name, String nickname, String phoneNumber, String image) {
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.image = image;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // User 가 가지고 있는 권한 목록을 SimpleGrantedAuthority 로 변환
        String roleName = "ROLE_";
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleName + this.role.name()));
        return authorities;
    }

    public Password getPasswordForChange() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return password.getEncodePassword();
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
