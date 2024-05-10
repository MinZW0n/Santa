package com.example.santa.domain.user.entity;

import com.example.santa.domain.common.BaseEntity;
import com.example.santa.domain.meeting.entity.Meeting;
import com.example.santa.domain.meeting.entity.Participant;
import com.example.santa.domain.preferredcategory.entity.PreferredCategory;
import com.example.santa.domain.rank.entity.Ranking;
import com.example.santa.domain.report.entity.Report;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import com.example.santa.domain.usermountain.entity.UserMountain;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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

    private String image = "/images/defaultProfile.png";

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;
    //누적 높이 저장
    private double accumulatedHeight;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;

    @OneToMany(mappedBy = "leader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meeting> meetings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ranking ranking;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMountain> userMountains;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChallenge> userChallenges;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferredCategory> preferredCategories;

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reporters;

    @OneToMany(mappedBy = "reportedParticipant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    @Builder
    public User(String email, Password password, String name, String nickname, String phoneNumber, String image, Role role, SocialType socialType, String socialId, double accumulatedHeight) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.accumulatedHeight = accumulatedHeight;
    }

    public User update(String nickname, String phoneNumber, String name, String image) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.image = image;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // User 가 가지고 있는 권한 목록을 SimpleGrantedAuthority 로 변환
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.name()));
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
