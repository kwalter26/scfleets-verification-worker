package com.fusionkoding.scfleetsverificationworker.clients.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RsiResponse<T> {
    String success;
    String code;
    String msg;
    T data;
}
