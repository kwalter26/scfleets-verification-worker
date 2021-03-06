package com.fusionkoding.scfleetsverificationworker.clients.models;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentState {
    private List<MessageBlock> blocks;
    private Map<String,String> entityMap;
}
