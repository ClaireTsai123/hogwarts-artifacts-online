package com.example.hogwartsartifactsonline.hogwartsuser;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class HogwartsUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

//    @NotEmpty(message = "Password cannot be empty")
    private String password;

    private boolean enabled;

    @NotEmpty(message = "Roles cannot be empty")
    private String roles;
}
