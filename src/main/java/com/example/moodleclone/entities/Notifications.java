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
public class Notifications implements Comparable<Notifications>{

    @Id
    @GeneratedValue
    @Column(name="ID",columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @NotNull
    @Column(length = 1000, name="message")
    private String message;

    @NotNull
    @Column(name="seen")
    private boolean seen;

    @ManyToOne
    @JoinColumn(name="user", nullable = false)
    private User user;

    @NotNull
    @Column(name="date")
    private Instant date;

    @Override
    public int compareTo(Notifications o) {
        return o.getDate().compareTo(getDate());
    }
}
