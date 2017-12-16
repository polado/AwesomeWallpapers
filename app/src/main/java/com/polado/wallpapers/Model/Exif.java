package com.polado.wallpapers.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PolaDo on 11/18/2017.
 */

public class Exif implements Parcelable {
    @SerializedName("make")
    private String make;

    @SerializedName("model")
    private String model;

    @SerializedName("exposure_time")
    private String exposureTime;

    @SerializedName("aperture")
    private String aperture;

    @SerializedName("focal_length")
    private String focalLength;

    @SerializedName("iso")
    private Integer iso;

    public static final Creator<Exif> CREATOR = new Creator<Exif>() {

        @SuppressWarnings({
                "unchecked"
        })

        public Exif createFromParcel(Parcel in) {
            Exif instance = new Exif();
            instance.make = (String) in.readValue((String.class.getClassLoader()));
            instance.model = (String) in.readValue((String.class.getClassLoader()));
            instance.exposureTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.aperture = (String) in.readValue((String.class.getClassLoader()));
            instance.focalLength = (String) in.readValue((String.class.getClassLoader()));
            instance.iso = (Integer) in.readValue((Integer.class.getClassLoader()));
            return instance;
        }

        public Exif[] newArray(int size) {
            return (new Exif[size]);
        }
    };

    public Exif() {
    }

    public Exif(String make, String model, String exposureTime, String aperture, String focalLength, Integer iso) {
        this.make = make;
        this.model = model;
        this.exposureTime = exposureTime;
        this.aperture = aperture;
        this.focalLength = focalLength;
        this.iso = iso;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(String exposureTime) {
        this.exposureTime = exposureTime;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public Integer getIso() {
        return iso;
    }

    public void setIso(Integer iso) {
        this.iso = iso;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(make);
        dest.writeValue(model);
        dest.writeValue(exposureTime);
        dest.writeValue(aperture);
        dest.writeValue(focalLength);
        dest.writeValue(iso);
    }
}
