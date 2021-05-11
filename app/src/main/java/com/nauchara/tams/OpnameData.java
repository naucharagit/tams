package com.nauchara.tams;

public class OpnameData {

    private String strID;
    private String strDate;
    private String strLocation;
    private String strOperatorID;
    private String strOperatorName;
    private String strStatus;

    public OpnameData(String strID, String strDate, String strLocation, String strOperatorID, String strOperatorName, String strStatus) {
        this.strID = strID;
        this.strDate = strDate;
        this.strLocation = strLocation;
        this.strOperatorID = strOperatorID;
        this.strOperatorName = strOperatorName;
        this.strStatus = strStatus;
    }

    public String getStrID() {
        return strID;
    }

    public void setStrID(String strID) {
        this.strID = strID;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getStrLocation() {
        return strLocation;
    }

    public void setStrLocation(String strLocation) {
        this.strLocation = strLocation;
    }

    public String getStrOperatorID() {
        return strOperatorID;
    }

    public void setStrOperatorID(String strOperatorID) {
        this.strOperatorID = strOperatorID;
    }

    public String getStrOperatorName() {
        return strOperatorName;
    }

    public void setStrOperatorName(String strOperatorName) {
        this.strOperatorName = strOperatorName;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }
}
