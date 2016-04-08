package com.atulnambudiri.stashforreddit;

/**
 * Created by atuln on 11/15/2015.
 */
public class Comment {
    int indent;
    String body;
    String author;
    int score;

    String title;
    int comments;
    String subreddit;
    String domain;

    public Comment(int indent, String body, String author, int score) {
        this.indent = indent;
        this.body = body;
        this.author = author;
        this.score = score;
    }

    public Comment(String title, int score, int comments, String subreddit, String domain) {
        this.title = title;
        this.score = score;
        this.comments = comments;
        this.subreddit = subreddit;
        this.domain = domain;
    }
}
