package com.proriberaapp.ribera.Api.controllers.admin.dto.views;

import com.proriberaapp.ribera.Api.controllers.admin.dto.BedroomReturn;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;

import java.util.List;

@Data
@Builder
public class ViewRoomReturn {
    @Column("roomid")
    private Integer roomId;
    @Column("roomtypename")
    private String roomTypeName;
    private String information;
    @Column("roomtypedescription")
    private String roomTypeDescription;
    @Column("stateroomname")
    private String stateRoomName;
    @Column("listbedroomreturn")
    private List<BedroomReturn> listBedroomReturn;

}
