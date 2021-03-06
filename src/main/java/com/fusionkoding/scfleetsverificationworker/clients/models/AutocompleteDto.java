package com.fusionkoding.scfleetsverificationworker.clients.models;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutocompleteDto {
    @Builder.Default
    private String community_id = null;
    private String text;
    @Builder.Default
    private boolean ignore_self = true;
}
