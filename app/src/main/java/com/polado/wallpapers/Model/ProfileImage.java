package com.polado.wallpapers.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PolaDo on 11/18/2017.
 */

public class ProfileImage implements Parcelable {

    public final static Parcelable.Creator<ProfileImage> CREATOR = new Creator<ProfileImage>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProfileImage createFromParcel(Parcel in) {
            ProfileImage instance = new ProfileImage();
            instance.small = ((String) in.readValue((String.class.getClassLoader())));
            instance.medium = ((String) in.readValue((String.class.getClassLoader())));
            instance.large = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public ProfileImage[] newArray(int size) {
            return (new ProfileImage[size]);
        }

    };
    @SerializedName("small")
    private String small;
    @SerializedName("medium")
    private String medium;
    @SerializedName("large")
    private String large;

    public ProfileImage() {
    }

    public ProfileImage(String small, String medium, String large) {
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(small);
        dest.writeValue(medium);
        dest.writeValue(large);
    }

    public int describeContents() {
        return 0;
    }
}
