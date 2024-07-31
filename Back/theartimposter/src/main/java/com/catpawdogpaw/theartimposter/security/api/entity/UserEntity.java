package com.catpawdogpaw.theartimposter.security.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity {

//    @JsonIgnore
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "ID", length = 64, unique = true)
    @NotNull
    @Size(max = 64)
    private String id;

    @JsonIgnore
    @Column(name = "PASSWORD", length = 128)
    @NotNull
    @Size(max = 128)
    private String password;

    @Column(name = "nickname", length = 100)
    @NotNull
    @Size(max = 100)
    private String nickname;

    @Column(name = "socialId", length = 512)
    @NotNull
    @Size(max = 512)
    private String socialId;

    @Column(name = "socialProviderType", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProviderType socialProviderType;

    @Column(name = "PROFILE_IMAGE_URL", length = 512)
    @NotNull
    @Size(max = 512)
    private String profileImageUrl;

    @Column(name = "ROLE_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType roleType;

    @Column(name = "CREATED_AT")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    @NotNull
    private LocalDateTime updatedAt;

    @Column(name="VIC_CNT")
    @NotNull
    private Long vicCnt;

    @Column
    @NotNull
    private Long gameCnt;

    public UserEntity(
            @NotNull @Size(max = 64) String id,
            @NotNull @Size(max = 100) String nickname,
            @NotNull @Size(max = 512) String socialId,
            @NotNull ProviderType providerType,
            @NotNull @Size(max = 512) String profileImageUrl,
            @NotNull RoleType roleType,
            @NotNull LocalDateTime createdAt,
            @NotNull LocalDateTime updatedAt
    ){
        this.id = id;
        this.nickname = nickname;
        this.password = "NO_PASS";
        this.socialId = socialId;
        this.socialProviderType = providerType;
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.roleType = roleType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.vicCnt = 0L;
        this.gameCnt = 0L;
    }
}
