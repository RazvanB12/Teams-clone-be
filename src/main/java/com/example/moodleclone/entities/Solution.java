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
import java.util.Date;
import java.util.UUID;

@Entity
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solution implements Comparable<Solution>{
    @Id
    @GeneratedValue
    @Column(name="ID",columnDefinition = "char(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @NotNull
    @Column(name="date")
    private Instant date;

    @NotNull
    @Column(name = "content", unique = false, nullable = false, columnDefinition = "LONGBLOB")
    private byte[] content;

    @ManyToOne
    @JoinColumn(name="student", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="assignment", nullable = false)
    private Assignments assignment;

    @NotNull
    @Column(name="type")
    private String type;

    @OneToOne(mappedBy = "solution",cascade = {CascadeType.ALL})
    private Grade grade;


    @Override
    public int compareTo(Solution o) {
        return getUser().getName().compareTo(o.getUser().getName());
    }
}
