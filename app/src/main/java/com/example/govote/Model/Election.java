package com.example.govote.Model;

public class Election {
    private String name;
    private String imageUrl;
    private String imageName;
    public Election(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Election(String name, String imageUrl, String imageName){
        if(name.trim().equals(""))
            name="No Name";
        this.name = name;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    public Election(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
