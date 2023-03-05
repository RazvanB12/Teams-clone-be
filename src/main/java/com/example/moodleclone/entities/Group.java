package com.example.moodleclone.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="TeamGroup")
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group implements Comparable<Group> {
    @Id
    @GeneratedValue
    @Column(name="ID",columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="enroll_key")
    private String enrollKey;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner", referencedColumnName = "id")
    private User owner;

    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private Set<UserGroups> userGroups;

    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY, cascade=CascadeType.DETACH)
    private Set<Files> files;

    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY, cascade=CascadeType.DETACH)
    private Set<Assignments> assignments;

    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY, cascade=CascadeType.DETACH)
    private Set<Posts> posts;

    @Override
    public int compareTo(Group o) {
        return getName().compareTo(o.getName());
    }
}
