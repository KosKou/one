package com.example.one.webdto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCardbinWebRequest {
    private int binNumber;
    private String binType;
}
