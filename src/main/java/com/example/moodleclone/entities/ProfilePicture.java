package com.example.moodleclone.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePicture {

    @Id
    @Column(name="ID",columnDefinition = "char(36)")
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @NotNull
    @Column(name = "content", unique = false, nullable = false, columnDefinition = "LONGBLOB")
    private byte[] content;

    @NotNull
    @Column(name="type")
    private String type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user",referencedColumnName = "id")
    private User user;
}
