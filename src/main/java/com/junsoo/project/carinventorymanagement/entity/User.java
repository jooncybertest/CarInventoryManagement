package com.junsoo.project.carinventorymanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
@Entity
@Table(name = "users")
@Getter
public class User {
    @Id
    @Column(unique = true, length = 100, nullable = false)
    private String email;
}
