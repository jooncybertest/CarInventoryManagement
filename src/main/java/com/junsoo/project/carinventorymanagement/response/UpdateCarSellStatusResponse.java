package com.junsoo.project.carinventorymanagement.response;

import com.junsoo.project.carinventorymanagement.dto.CarSellDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCarSellStatusResponse {
    private boolean success;
    private IsUserAdmin isUserAdmin;
    private List<CarSellDto> updatedCars;
}
