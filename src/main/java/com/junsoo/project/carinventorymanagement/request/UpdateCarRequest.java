package com.junsoo.project.carinventorymanagement.request;

import com.junsoo.project.carinventorymanagement.entity.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCarRequest {
    private Long id;
    private Boolean availability;
    private Status status;
}
