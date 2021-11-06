package com.example.diary;

//创建一个日记的类
public class Diary {
    private int id;
    private String author;
    private String title;
    private String time;
    private String content;

    public Diary(int id,String author,String title,String time,String content) {
        this.author = author;
        this.title = title;
        this.time = time;
        this.content = content;
        this.id = id;
    }

    public String getAuthor() {
        return this.author;
    }
    public int getId() { return this.id; }
    public String getTitle() {
        return this.title;
    }
    public String getTime() {
        return this.time;
    }
    public String getContent() {
        return this.content;
    }
}
