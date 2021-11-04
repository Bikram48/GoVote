package com.example.govote.Model;

public class Election {
    private String name;
    private String imageUrl;
    private String imageName;
    private String endDate;
    private String isEnded;
    private String electionType;

    public String getElectionType() {
        return electionType;
    }

    public void setElectionType(String electionType) {
        this.electionType = electionType;
    }

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


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIsEnded() {
        return isEnded;
    }

    public void setIsEnded(String isEnded) {
        this.isEnded = isEnded;
    }

    public Election(String name, String imageUrl, String imageName, String endDate, String isEnded,String electionType) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
        this.endDate = endDate;
        this.isEnded=isEnded;
        this.electionType=electionType;
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
