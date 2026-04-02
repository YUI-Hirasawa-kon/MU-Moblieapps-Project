package com.example.mumoblieappsproject.model;

import com.google.gson.annotations.SerializedName;

public class SchoolFeature {
    @SerializedName("geometry")
    public Geometry geometry;

    @SerializedName("properties")
    public SchoolProperties properties;
}
