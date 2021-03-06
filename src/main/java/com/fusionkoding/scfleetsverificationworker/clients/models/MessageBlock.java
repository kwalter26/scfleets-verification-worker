package com.fusionkoding.scfleetsverificationworker.clients.models;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageBlock {
    private String key;
    private String text;
    private String type;
    private int depth;
    private List<Object> inlineStyleRanges;
    private List<Object> entityRanges;
    private Map<String,String> data;
}
