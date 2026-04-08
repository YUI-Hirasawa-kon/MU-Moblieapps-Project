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

    @SerializedName("district_en")
    public String districtEn;

    @SerializedName("school_address_tc")
    public String schoolAddressTc;

    @SerializedName("school_address_en")
    public String schoolAddressEn;

    @SerializedName("school_tel")
    public String schoolTel;

    @SerializedName("school_email")
    public String schoolEmail;

    @SerializedName("school_website")
    public String schoolWebsite;

    @SerializedName("school_type_tc")
    public String schoolTypeTc;

    @SerializedName("school_type_en")
    public String schoolTypeEn;

// ========= Module 1: Header Supplement =========
    @SerializedName("school_motto_tc")
    public String schoolMottoTc;

    // ========= Module 2: Background and Religion =========
    @SerializedName("school_mission_tc")
    public String schoolMissionTc;

    @SerializedName("religion_tc")
    public String religionTc;

    // ========= Module 3: Academics and Admissions =========
    @SerializedName("oa_s1_admission_tc")
    public String s1AdmissionTc;     // s1 Admission Criteria

    @SerializedName("language_policy_tc")
    public String languagePolicyTc;  // Teaching Language Policy

    @SerializedName("current_year_no_of_class_s1")
    public String classS1;           // Number of classes at each level
    @SerializedName("current_year_no_of_class_s2")
    public String classS2;
    @SerializedName("current_year_no_of_class_s3")
    public String classS3;
    @SerializedName("current_year_no_of_class_s4")
    public String classS4;
    @SerializedName("current_year_no_of_class_s5")
    public String classS5;
    @SerializedName("current_year_no_of_class_s6")
    public String classS6;

    // ========= Module 4: Faculty and Staff Distribution =========

    @SerializedName("tsi_percent_of_bachelor")
    public String tsi_percent_of_bachelor; // percentage of teachers with bachelor's degrees

    @SerializedName("tsi_percent_of_master_doctorate_or_above")
    public String tsi_percent_of_master_doctorate_or_above; // percentage of teachers with master's degrees or above

    // percentage of teachers with teach year
    @SerializedName("tsi_percent_of_exp_0_4")
    public String exp0to4;

    @SerializedName("tsi_percent_of_exp_5_9")
    public String exp5to9;

    @SerializedName("tsi_percent_of_exp_10_or_above")
    public String exp10Plus;

    // ========= Module 5: Campus Facilities =========
    @SerializedName("fac_school_facilities_en")
    public String schoolFacilitiesEn;
    @SerializedName("fac_school_facilities_tc")
    public String schoolFacilitiesTc;

    //multi language part add

    @SerializedName("school_motto_en")
    public String schoolMottoEn;


    @SerializedName("school_mission_en")
    public String schoolMissionEn;

    @SerializedName("religion_en")
    public String religionEn;

    @SerializedName("language_policy_en")
    public String languagePolicyEn;
    @SerializedName("oa_s1_admission_en")
    public String s1AdmissionEn;


}