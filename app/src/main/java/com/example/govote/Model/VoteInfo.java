package com.example.govote.Model;

public class VoteInfo {
    private String position;
    private String candidateName;
    private String voterName;
    private int voteCount;

    public VoteInfo() {
    }

    public VoteInfo(String position, String candidateName, String voterName, int voteCount) {
        this.position = position;
        this.candidateName = candidateName;
        this.voterName = voterName;
        this.voteCount = voteCount;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
