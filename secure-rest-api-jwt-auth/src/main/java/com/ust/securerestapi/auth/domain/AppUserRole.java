package com.ust.securerestapi.auth.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppUserRole {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    public AppUserRole(String name) {
        this.name = name;
    }

}
