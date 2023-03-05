package com.example.moodleclone.entities;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Comparable<User>{

    @Id
    @GeneratedValue
    @Column(name="ID",columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @NotNull
    @Column(name="username",unique = true)
    private String username;

    @NotNull
    @Column(name="email",unique = true)
    private String email;

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="password")
    private String password;

    @NotNull
    @Column(name="active")
    private Boolean active;

    @NotNull
    @Column(name="ROLE")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

    @OneToOne(mappedBy = "user",cascade = {CascadeType.ALL})
    private ProfilePicture profilePicture;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Set<Notifications> notifications;

    @OneToMany(mappedBy = "receiver",fetch = FetchType.LAZY, cascade=CascadeType.DETACH)
    private Set<Messages> receivedMessages;

    @OneToMany(mappedBy = "sender",fetch = FetchType.LAZY, cascade=CascadeType.DETACH)
    private Set<Messages> sendMessages;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Set<UserGroups> userGroups;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Set<Files> files;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Set<Solution> assignmentsGrades;

    @NotNull
    @OneToMany(mappedBy = "owner",fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Set<Group> myGroups;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Set<Posts> posts;

    @Override
    public int compareTo(User o) {
        return getName().compareTo(o.getName());
    }
}
