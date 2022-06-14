package com.vuthaihung.loseflat.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdmobFirebaseModel {
    @SerializedName("status")
    private boolean status;
    @SerializedName("list")
    private List<String> listAdmob;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getListAdmob() {
        return listAdmob;
    }

    public void setListAdmob(List<String> listAdmob) {
        this.listAdmob = listAdmob;
    }

}
