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
import java.util.UUID;

@Entity
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Grade {
    @Id
    @Column(name="ID",columnDefinition = "char(36)")
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="solution",referencedColumnName = "id")
    private Solution solution;

    @NotNull
    @Column(name="value")
    private int value;

    @NotNull
    @Column(name="date")
    private Instant date;
}
