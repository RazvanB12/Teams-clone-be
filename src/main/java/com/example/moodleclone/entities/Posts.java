package com.example.moodleclone.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Entity
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Posts implements Comparable<Posts>{
    @Id
    @GeneratedValue
    @Column(name="ID",columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @NotNull
    @Column(length = 1000, name="content")
    private String content;

    @NotNull
    @Column(name="date")
    private Instant date;

    @ManyToOne
    @JoinColumn(name="author", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="team_group", nullable = false)
    private Group group;

    @Override
    public int compareTo(Posts o) {
        return getDate().compareTo(o.getDate());
    }
}
