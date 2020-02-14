package com.example.one.webdto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCardbinWebRequest {
    private int binNumber;
    private String binType;
}
