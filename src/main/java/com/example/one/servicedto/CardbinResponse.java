package com.example.one.servicedto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardbinResponse {
    private int id;
    private int binNumber;
    private String binType;
    private int attributes;
    private String state;
}
