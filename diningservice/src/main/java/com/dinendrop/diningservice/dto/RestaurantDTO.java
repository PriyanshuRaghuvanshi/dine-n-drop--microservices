package com.dinendrop.diningservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantDTO {
    private String id;
    private String name;
    private String areaCode;
    private String city;
    private LocalTime openAt;
    private LocalTime closeAt;
    private boolean online;
    private boolean diningEnabled;
}
