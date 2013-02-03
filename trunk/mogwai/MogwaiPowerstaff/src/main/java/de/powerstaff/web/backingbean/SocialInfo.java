package de.powerstaff.web.backingbean;

public class SocialInfo {

    boolean infoProvided;

    String imageUrl;

    public boolean isInfoProvided() {
        return infoProvided;
    }

    public void setInfoProvided(boolean infoProvided) {
        this.infoProvided = infoProvided;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
