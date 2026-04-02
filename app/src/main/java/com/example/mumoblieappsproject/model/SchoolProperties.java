package com.example.mumoblieappsproject.model;

import com.google.gson.annotations.SerializedName;

public class SchoolProperties {
    @SerializedName("id")
    public String id;

    @SerializedName("school_name_tc")
    public String schoolNameTc;

    @SerializedName("school_name_en")
    public String schoolNameEn;

    @SerializedName("district_tc")
    public String districtTc;

    @SerializedName("school_address_tc")
    public String schoolAddressTc;
    @SerializedName("school_tel")
    public String schoolTel;

    @SerializedName("school_type_tc")
    public String schoolTypeTc;

    @SerializedName("student_gender_tc")
    public String studentGenderTc;

    @SerializedName("tsi_percent_of_bachelor")
    public String percentOfBachelor;
}
