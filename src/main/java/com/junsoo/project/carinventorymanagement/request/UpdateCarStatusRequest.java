package com.junsoo.project.carinventorymanagement.request;

import com.junsoo.project.carinventorymanagement.entity.SellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCarStatusRequest {
    private Long id;
    private Boolean availability;
    private SellStatus sellStatus;
}
