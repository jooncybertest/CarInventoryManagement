
package com.junsoo.project.carinventorymanagement.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCarInfoRequest {
    private String make;
    private String model;
    private Integer year;
    private String vin;
    private Integer mileage;
    private String licensePlate;
}
