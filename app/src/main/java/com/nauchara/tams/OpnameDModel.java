package com.nauchara.tams;

public class OpnameDModel {
    private int opd_id;
    private int opd_value1;
    private int opd_value2;
    private int opd_value3;
    private String opd_itemname;
    private int opd_value4;
    private String opd_tag;
    private int opd_location;
    private String opd_location_desc;

    public OpnameDModel(int opd_id, int opd_value1, int opd_value2, int opd_value3, String opd_itemname, int opd_value4, String opd_tag, int opd_location, String opd_location_desc) {
        this.opd_id = opd_id;
        this.opd_value1 = opd_value1;
        this.opd_value2 = opd_value2;
        this.opd_value3 = opd_value3;
        this.opd_itemname = opd_itemname;
        this.opd_value4 = opd_value4;
        this.opd_tag = opd_tag;
        this.opd_location = opd_location;
        this.opd_location_desc = opd_location_desc;
    }

    @Override
    public String toString() {
        /*return "OpnameDModel{" +
                "opd_id=" + opd_id +
                ", opd_value1=" + opd_value1 +
                ", opd_value2=" + opd_value2 +
                ", opd_value3=" + opd_value3 +
                ", opd_itemname='" + opd_itemname + '\'' +
                ", opd_value4=" + opd_value4 +
                ", opd_tag='" + opd_tag + '\'' +
                '}';*/
        //return opd_itemname+" - "+opd_location_desc;
        return opd_itemname;
    }

    public String getOpd_location_desc() {
        return opd_location_desc;
    }

    public void setOpd_location_desc(String opd_location_desc) {
        this.opd_location_desc = opd_location_desc;
    }

    public int getOpd_location() {
        return opd_location;
    }

    public void setOpd_location(int opd_location) {
        this.opd_location = opd_location;
    }

    public String getOpd_tag() {
        return opd_tag;
    }

    public void setOpd_tag(String opd_tag) {
        this.opd_tag = opd_tag;
    }

    public int getOpd_id() {
        return opd_id;
    }

    public void setOpd_id(int opd_id) {
        this.opd_id = opd_id;
    }

    public int getOpd_value1() {
        return opd_value1;
    }

    public void setOpd_value1(int opd_value1) {
        this.opd_value1 = opd_value1;
    }

    public int getOpd_value2() {
        return opd_value2;
    }

    public void setOpd_value2(int opd_value2) {
        this.opd_value2 = opd_value2;
    }

    public int getOpd_value3() {
        return opd_value3;
    }

    public void setOpd_value3(int opd_value3) {
        this.opd_value3 = opd_value3;
    }

    public String getOpd_itemname() {
        return opd_itemname;
    }

    public void setOpd_itemname(String opd_itemname) {
        this.opd_itemname = opd_itemname;
    }

    public int getOpd_value4() {
        return opd_value4;
    }

    public void setOpd_value4(int opd_value4) {
        this.opd_value4 = opd_value4;
    }

}
