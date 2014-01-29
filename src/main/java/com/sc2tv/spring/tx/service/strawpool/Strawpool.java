package com.sc2tv.spring.tx.service.strawpool;

public interface Strawpool {
    void startVoteStrawpool(String url, int[] voteFor, int threads);
    void startVoteRupool(String url, int[] voteFor, int threads);
}
