package com.example.mumoblieappsproject.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SchoolFeatureCollection {
    @SerializedName("features")
    public List<SchoolFeature> features;
}
