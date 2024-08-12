package com.junsoo.project.carinventorymanagement.request;

import com.junsoo.project.carinventorymanagement.entity.RentStatus;
import com.junsoo.project.carinventorymanagement.entity.SellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCarSellStatusRequest {
    private Long id;
    private Boolean availability;
    private SellStatus sellStatus;
    private RentStatus rentStatus;
}
