package com.example.one.webdto.request;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddAttributeWebRequest {
    private String key;
    private String value;
}
