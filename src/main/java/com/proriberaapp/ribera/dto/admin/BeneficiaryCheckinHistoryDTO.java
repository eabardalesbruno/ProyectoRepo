package com.proriberaapp.ribera.dto.admin;

import java.util.List;

public class BeneficiaryCheckinHistoryDTO {
    private String idBeneficiary;
    private int visitas;
    private List<Long> checkinDates;

    public BeneficiaryCheckinHistoryDTO(String idBeneficiary, int visitas, List<Long> checkinDates) {
        this.idBeneficiary = idBeneficiary;
        this.visitas = visitas;
        this.checkinDates = checkinDates;
    }

    public String getIdBeneficiary() {
        return idBeneficiary;
    }

    public void setIdBeneficiary(String idBeneficiary) {
        this.idBeneficiary = idBeneficiary;
    }

    public int getVisitas() {
        return visitas;
    }

    public void setVisitas(int visitas) {
        this.visitas = visitas;
    }

    public List<Long> getCheckinDates() {
        return checkinDates;
    }

    public void setCheckinDates(List<Long> checkinDates) {
        this.checkinDates = checkinDates;
    }
}
