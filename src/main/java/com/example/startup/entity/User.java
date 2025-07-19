package com.example.startup.entity;

import com.example.startup.entity.enums.UserRole;
import com.example.startup.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true,nullable = false)
    private String phone;

    @Column(nullable = false,unique = true)
    private Long chatId;

    @Column(nullable = false,unique = true)
    private String key;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Order> orders;

    private String refreshToken;

    private boolean IMBusy;

    @OneToMany(fetch = FetchType.LAZY)
    private List<JobPost> jobs;

    private boolean active = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return key;
    }
}
