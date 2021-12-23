package com.nauchara.tams;

public class LocationModel {
    private int loc_id;
    private int loc_value1;
    private String loc_description;
    private int loc_value2;
    private String loc_room;
    private String loc_building;
    private String loc_floor;

    public LocationModel(int loc_id, int loc_value1, String loc_description, int loc_value2, String loc_room, String loc_building, String loc_floor) {
        this.loc_id = loc_id;
        this.loc_value1 = loc_value1;
        this.loc_description = loc_description;
        this.loc_value2 = loc_value2;
        this.loc_room = loc_room;
        this.loc_building = loc_building;
        this.loc_floor = loc_floor;
    }

    @Override
    public String toString() {
        return "["+loc_floor+"] "+loc_room;
    }

    public int getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(int loc_id) {
        this.loc_id = loc_id;
    }

    public int getLoc_value1() {
        return loc_value1;
    }

    public void setLoc_value1(int loc_value1) {
        this.loc_value1 = loc_value1;
    }

    public String getLoc_description() {
        return loc_description;
    }

    public void setLoc_description(String loc_description) {
        this.loc_description = loc_description;
    }

    public int getLoc_value2() {
        return loc_value2;
    }

    public void setLoc_value2(int loc_value2) {
        this.loc_value2 = loc_value2;
    }

    public String getLoc_room() {
        return loc_room;
    }

    public void setLoc_room(String loc_room) {
        this.loc_room = loc_room;
    }

    public String getLoc_building() {
        return loc_building;
    }

    public void setLoc_building(String loc_building) {
        this.loc_building = loc_building;
    }

    public String getLoc_floor() {
        return loc_floor;
    }

    public void setLoc_floor(String loc_floor) {
        this.loc_floor = loc_floor;
    }

}
