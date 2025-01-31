package com.github.mingyu.loginmaster.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    private String email;
    private String password;
}
