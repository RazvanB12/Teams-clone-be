package com.example.moodleclone.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assignments implements Comparable<Assignments>{
    @Id
    @GeneratedValue
    @Column(name="ID",columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @NotNull
    @Column(name="title")
    private String title;

    @NotNull
    @Column(length = 1000, name="description")
    private String description;

    @NotNull
    @Column(name="date")
    private Instant date;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="team_group", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "assignment",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Solution> solution;

    @Override
    public int compareTo(Assignments o) {
        return getDate().compareTo(o.getDate());
    }
}
