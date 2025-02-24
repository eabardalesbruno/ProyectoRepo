package com.proriberaapp.ribera.Domain.entities;

import lombok.Data;

@Data
public class NiubizAutorizationBodyEntity {

    private String channel;
    private String captureType;
    private boolean countable;
    private NiubizAuthorizationOrderEntity order;
    private NiubizAuthorizationDataMapEntity dataMap;

}
