package com.junsoo.project.carinventorymanagement.request;

import com.junsoo.project.carinventorymanagement.entity.SellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSellStatusRequest {
    private Long id;
    private SellStatus sellStatus;
}

