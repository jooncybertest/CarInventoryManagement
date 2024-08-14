
package com.junsoo.project.carinventorymanagement.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReservationRequest {
    private Long carId;
    private String userEmail;
    private Integer mileage;
}
