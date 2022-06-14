package com.vuthaihung.loseflat.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdmobFirebaseModel {
    @SerializedName("status")
    private String status;
    @SerializedName("list")
    private List<String> listAdmob;

    public AdmobFirebaseModel(String status, List<String> listAdmob) {
        this.status = status;
        this.listAdmob = listAdmob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getListAdmob() {
        return listAdmob;
    }

    public void setListAdmob(List<String> listAdmob) {
        this.listAdmob = listAdmob;
    }

}
