package com.nauchara.tams;

public class OpnameScanData {
    private int idscan;
    private String tagscan;
    private String namescan;
    private String roomscan;

    public OpnameScanData(int idscan, String tagscan, String namescan, String roomscan) {
        this.idscan = idscan;
        this.tagscan = tagscan;
        this.namescan = namescan;
        this.roomscan = roomscan;
    }

    @Override
    public String toString() {
        return "OpnameScanData{" +
                "idscan=" + idscan +
                ", tagscan='" + tagscan + '\'' +
                ", namescan='" + namescan + '\'' +
                ", roomscan='" + roomscan + '\'' +
                '}';
    }

    public int getIdscan() {
        return idscan;
    }

    public void setIdscan(int idscan) {
        this.idscan = idscan;
    }

    public String getTagscan() {
        return tagscan;
    }

    public void setTagscan(String tagscan) {
        this.tagscan = tagscan;
    }

    public String getNamescan() {
        return namescan;
    }

    public void setNamescan(String namescan) {
        this.namescan = namescan;
    }

    public String getRoomscan() {
        return roomscan;
    }

    public void setRoomscan(String roomscan) {
        this.roomscan = roomscan;
    }
}


