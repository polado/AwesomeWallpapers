package Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by PolaDo on 11/16/2017.
 */

public class Photo implements Parcelable {
    @SerializedName("id")
    private String photoID;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("color")
    private String color;

    @SerializedName("downloads")
    private int downloads;

    @SerializedName("likes")
    private int likes;

    @SerializedName("liked_by_user")
    private boolean likedByUser;

    @SerializedName("description")
    private String description;

    @SerializedName("exif")
    private Exif exif;

    @SerializedName("location")
    private Location location;

    @SerializedName("urls")
    private Urls urls;

    @SerializedName("links")
    private Links links;

    @SerializedName("user")
    private User user;

    @SerializedName("categories")
    private ArrayList<Category> categories = new ArrayList<>();

    // objects: current_user_collections

    public final static Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {

        @SuppressWarnings({
                "unchecked"
        })

        @Override
        public Photo createFromParcel(Parcel in) {
            Photo instance = new Photo();

            instance.photoID = (String) in.readValue(String.class.getClassLoader());
            instance.createdAt = (String) in.readValue((String.class.getClassLoader()));
            instance.updatedAt = (String) in.readValue((String.class.getClassLoader()));
            instance.width = (Integer) in.readValue((Integer.class.getClassLoader()));
            instance.height = (Integer) in.readValue((Integer.class.getClassLoader()));
            instance.color = (String) in.readValue((String.class.getClassLoader()));
            instance.downloads = (Integer) in.readValue((Integer.class.getClassLoader()));
            instance.likes = (Integer) in.readValue((Integer.class.getClassLoader()));
            instance.likedByUser = (Boolean) in.readValue((Boolean.class.getClassLoader()));
            instance.description = (String) in.readValue((String.class.getClassLoader()));
            instance.exif = (Exif) in.readValue((Exif.class.getClassLoader()));
            instance.location = ((Location) in.readValue((Location.class.getClassLoader())));
            instance.urls = ((Urls) in.readValue((Urls.class.getClassLoader())));
            in.readList(instance.categories, (Category.class.getClassLoader()));
            instance.links = ((Links) in.readValue((Links.class.getClassLoader())));
            instance.user = ((User) in.readValue((User.class.getClassLoader())));

            return instance;
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public Photo() {
    }

    public Photo(String photoID, String createdAt, String updatedAt, int width, int height, String color, int downloads, int likes, boolean likedByUser, String description, Exif exif, Location location, Urls urls, Links links, User user, ArrayList<Category> categories) {
        this.photoID = photoID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.width = width;
        this.height = height;
        this.color = color;
        this.downloads = downloads;
        this.likes = likes;
        this.likedByUser = likedByUser;
        this.description = description;
        this.exif = exif;
        this.location = location;
        this.urls = urls;
        this.links = links;
        this.user = user;
        this.categories = categories;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isLikedByUser() {
        return likedByUser;
    }

    public void setLikedByUser(boolean likedByUser) {
        this.likedByUser = likedByUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public Exif getExif() {
        return exif;
    }

    public void setExif(Exif exif) {
        this.exif = exif;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(photoID);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
        dest.writeValue(width);
        dest.writeValue(height);
        dest.writeValue(color);
        dest.writeValue(downloads);
        dest.writeValue(likes);
        dest.writeValue(likedByUser);
        dest.writeValue(exif);
        dest.writeValue(location);
        dest.writeValue(urls);
        dest.writeList(categories);
        dest.writeValue(links);
        dest.writeValue(user);
    }
}
