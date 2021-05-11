package com.nauchara.tams;

public class OpnameHModel {
    private int oph_id;
    private String oph_title;
    private String oph_date;
    private int oph_status1;
    private int oph_status2;
    private String oph_status3;
    private String oph_name;
    private String oph_status4;

    public OpnameHModel(int oph_id, String oph_title, String oph_date, int oph_status1, int oph_status2, String oph_status3, String oph_name, String oph_status4) {
        this.oph_id = oph_id;
        this.oph_title = oph_title;
        this.oph_date = oph_date;
        this.oph_status1 = oph_status1;
        this.oph_status2 = oph_status2;
        this.oph_status3 = oph_status3;
        this.oph_name = oph_name;
        this.oph_status4 = oph_status4;
    }

    @Override
    public String toString() {
        return "OpnameHModel{" +
                "oph_id=" + oph_id +
                ", oph_title='" + oph_title + '\'' +
                ", oph_date='" + oph_date + '\'' +
                ", oph_status1=" + oph_status1 +
                ", oph_status2=" + oph_status2 +
                ", oph_status3='" + oph_status3 + '\'' +
                ", oph_name='" + oph_name + '\'' +
                ", oph_status4='" + oph_status4 + '\'' +
                '}';
    }

    public int getOph_id() {
        return oph_id;
    }

    public void setOph_id(int oph_id) {
        this.oph_id = oph_id;
    }

    public String getOph_title() {
        return oph_title;
    }

    public void setOph_title(String oph_title) {
        this.oph_title = oph_title;
    }

    public String getOph_date() {
        return oph_date;
    }

    public void setOph_date(String oph_date) {
        this.oph_date = oph_date;
    }

    public int getOph_status1() {
        return oph_status1;
    }

    public void setOph_status1(int oph_status1) {
        this.oph_status1 = oph_status1;
    }

    public int getOph_status2() {
        return oph_status2;
    }

    public void setOph_status2(int oph_status2) {
        this.oph_status2 = oph_status2;
    }

    public String getOph_status3() {
        return oph_status3;
    }

    public void setOph_status3(String oph_status3) {
        this.oph_status3 = oph_status3;
    }

    public String getOph_name() {
        return oph_name;
    }

    public void setOph_name(String oph_name) {
        this.oph_name = oph_name;
    }

    public String getOph_status4() {
        return oph_status4;
    }

    public void setOph_status4(String oph_status4) {
        this.oph_status4 = oph_status4;
    }

}
