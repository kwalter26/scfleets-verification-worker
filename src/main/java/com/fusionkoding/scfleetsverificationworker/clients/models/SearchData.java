package com.fusionkoding.scfleetsverificationworker.clients.models;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchData {
    private List<Member> members;
}
