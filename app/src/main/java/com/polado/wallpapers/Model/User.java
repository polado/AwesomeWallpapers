package com.polado.wallpapers.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by PolaDo on 11/18/2017.
 */

public class User {


    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("name")
    private String name;

    @SerializedName("portfolio_url")
    private String portfolioUrl;

    @SerializedName("bio")
    private String bio;

    @SerializedName("location")
    private String location;

    @SerializedName("total_likes")
    private Integer totalLikes;

    @SerializedName("total_photos")
    private Integer totalPhotos;

    @SerializedName("total_collections")
    private Integer totalCollections;

    @SerializedName("followed_by_user")
    private boolean followedByUser;

    @SerializedName("followers_count")
    private Integer followersCount;

    @SerializedName("following_count")
    private Integer followingCount;

    @SerializedName("profile_image")
    private ProfileImage profileImage;

    @SerializedName("links")
    private Links links;

    public User() {
    }

    public User(String id, String username, String name, String portfolioUrl, String bio, String location, Integer totalLikes, Integer totalPhotos, Integer totalCollections, boolean followedByUser, Integer followersCount, Integer followingCount, ProfileImage profileImage, Links links) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.portfolioUrl = portfolioUrl;
        this.bio = bio;
        this.location = location;
        this.totalLikes = totalLikes;
        this.totalPhotos = totalPhotos;
        this.totalCollections = totalCollections;
        this.followedByUser = followedByUser;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.profileImage = profileImage;
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Integer getTotalPhotos() {
        return totalPhotos;
    }

    public void setTotalPhotos(Integer totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    public Integer getTotalCollections() {
        return totalCollections;
    }

    public void setTotalCollections(Integer totalCollections) {
        this.totalCollections = totalCollections;
    }

    public boolean isFollowedByUser() {
        return followedByUser;
    }

    public void setFollowedByUser(boolean followedByUser) {
        this.followedByUser = followedByUser;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public ProfileImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
