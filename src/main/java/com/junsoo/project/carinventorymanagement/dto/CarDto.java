package com.junsoo.project.carinventorymanagement.dto;

import com.junsoo.project.carinventorymanagement.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    private Long id;
    private Boolean availability;
    private Status status;
}
