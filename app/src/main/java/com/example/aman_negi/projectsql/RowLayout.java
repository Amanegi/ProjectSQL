package com.example.aman_negi.projectsql;

public class RowLayout {
    byte[] rowImage;
    String rowName, rowEmail, rowPhone;

    public RowLayout(byte[] rowImage, String rowName, String rowEmail, String rowPhone) {
        this.rowImage = rowImage;
        this.rowName = rowName;
        this.rowEmail = rowEmail;
        this.rowPhone = rowPhone;
    }

    public byte[] getRowImage() {
        return rowImage;
    }

    public String getRowName() {
        return rowName;
    }

    public String getRowEmail() {
        return rowEmail;
    }

    public String getRowPhone() {
        return rowPhone;
    }
}
