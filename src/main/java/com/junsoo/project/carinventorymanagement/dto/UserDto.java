package com.junsoo.project.carinventorymanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String email;
    private String role;
    private boolean valid;
}
