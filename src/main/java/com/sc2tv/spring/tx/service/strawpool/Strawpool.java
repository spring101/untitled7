package com.sc2tv.spring.tx.service.strawpool;

public interface Strawpool {
    void startVoteStrawpool(String url, int[] voteFor, int threads);
    void startVoteRupool(String url, int[] voteFor, int threads, boolean money);
    void scanAll(final int[] options, final int threads, final boolean money);
    void scanSingle(String channel, final int threads, final int[] voteFor, final boolean money);
}
