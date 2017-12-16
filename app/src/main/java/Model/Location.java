package Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PolaDo on 11/19/2017.
 */

public class Location implements Parcelable {
    @SerializedName("city")
    private String city;

    @SerializedName("country")
    private String country;

    @SerializedName("position")
    private Position position;

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Location createFromParcel(Parcel in) {
            Location instance = new Location();
            instance.city = ((String) in.readValue((String.class.getClassLoader())));
            instance.country = ((String) in.readValue((String.class.getClassLoader())));
            instance.position = ((Position) in.readValue((Position.class.getClassLoader())));
            return instance;
        }

        public Location[] newArray(int size) {
            return (new Location[size]);
        }
    };

    public Location() {
    }

    public Location(String city, String country, Position position) {
        this.city = city;
        this.country = country;
        this.position = position;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(city);
        dest.writeValue(country);
        dest.writeValue(position);
    }
}
