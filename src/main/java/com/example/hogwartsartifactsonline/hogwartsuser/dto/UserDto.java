package com.example.hogwartsartifactsonline.hogwartsuser.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(
        Integer id,
        @NotEmpty(message = "Username cannot be empty")
        String username,
        boolean enabled,
        @NotEmpty(message = "Roles cannot be empty")
        String roles) {

}
