package com.example.govote.Model;

public class VoteResult implements Comparable<VoteResult>{
    private int voteCount;
    private String election;
    private String candidate;

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public VoteResult(int voteCount, String election, String candidate) {
        this.voteCount = voteCount;
        this.election = election;
        this.candidate=candidate;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getElection() {
        return election;
    }

    public void setElection(String election) {
        this.election = election;
    }

    @Override
    public int compareTo(VoteResult voteResult) {
        if (this.getVoteCount() > voteResult.getVoteCount()) {
            return 1;
        } else if (this.getVoteCount() < voteResult.getVoteCount()) {
            return -1;
        }
        return 0;
    }
}
