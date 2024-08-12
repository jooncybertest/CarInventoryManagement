package com.junsoo.project.carinventorymanagement.dto;

import com.junsoo.project.carinventorymanagement.entity.SellStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarSellDto {
    private Long id;
    private SellStatus sellStatus;
}
