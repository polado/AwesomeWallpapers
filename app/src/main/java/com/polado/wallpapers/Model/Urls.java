package com.polado.wallpapers.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PolaDo on 11/18/2017.
 */

public class Urls implements Parcelable {

    public final static Parcelable.Creator<Urls> CREATOR = new Parcelable.Creator<Urls>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Urls createFromParcel(Parcel in) {
            Urls instance = new Urls();
            instance.raw = ((String) in.readValue((String.class.getClassLoader())));
            instance.full = ((String) in.readValue((String.class.getClassLoader())));
            instance.regular = ((String) in.readValue((String.class.getClassLoader())));
            instance.small = ((String) in.readValue((String.class.getClassLoader())));
            instance.thumb = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Urls[] newArray(int size) {
            return (new Urls[size]);
        }

    };
    @SerializedName("raw")
    private String raw;
    @SerializedName("full")
    private String full;
    @SerializedName("regular")
    private String regular;
    @SerializedName("small")
    private String small;
    @SerializedName("thumb")
    private String thumb;

    public Urls(String raw, String full, String regular, String small, String thumb) {
        this.raw = raw;
        this.full = full;
        this.regular = regular;
        this.small = small;
        this.thumb = thumb;
    }

    public Urls() {
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(raw);
        dest.writeValue(full);
        dest.writeValue(regular);
        dest.writeValue(small);
        dest.writeValue(thumb);
    }

    public int describeContents() {
        return 0;
    }
}
