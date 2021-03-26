package com.fusionkoding.scfleetsverificationworker.clients.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDataRsiResponse {
    String success;
    String code;
    String msg;
    SearchData data;
}
