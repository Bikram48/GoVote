package com.example.govote.Model;

public class Candidate {
    private String name;
    private String description;
    private String imageUrl;
    private String imageName;
    private String electionCat;
    private boolean isClicked;

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public String getElectionCat() {
        return electionCat;
    }

    public void setElectionCat(String electionCat) {
        this.electionCat = electionCat;
    }

    public Candidate(String name, String description, String imageUrl, String imageName, String electionCat) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
        this.electionCat=electionCat;
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

    public Candidate(){

    }
    public Candidate(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
