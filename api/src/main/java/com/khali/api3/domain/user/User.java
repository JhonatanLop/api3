package com.khali.api3.domain.user;

import jakarta.persistence.*;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity(name="users")
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String registration;
    private String name;

    @Enumerated(EnumType.STRING)

    private UserType userType;

    @Column(unique=true)
    private String email;

    @Column(unique=true)
    private String password;

    private Boolean active;

    @Column(name = "insert_date")
    private Timestamp insertDate;

    @Column(name = "expire_date")
    private Timestamp expiredDate;
}
