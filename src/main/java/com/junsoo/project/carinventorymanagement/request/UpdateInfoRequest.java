
package com.junsoo.project.carinventorymanagement.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInfoRequest {
    private Long id;
    private String make;
    private String model;
    private Integer year;
    private String vin;
    private String color;
    private Integer mileage;
    private String licensePlate;
}
