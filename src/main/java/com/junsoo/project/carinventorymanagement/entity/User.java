package com.junsoo.project.carinventorymanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @Column(unique = true, length = 100, nullable = false)
    private String email;
}
