package com.fusionkoding.scfleetsverificationworker.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PilotInfoDto {
    private String rsiHandle;
    private String rsiProfileImgUrl;
    private String lang;
    private String timeZone;
    private String ueeRecordNumber;
    private String fluency;
    private String enlistDate;
    private String location;
    private String orgSymbol;
}
