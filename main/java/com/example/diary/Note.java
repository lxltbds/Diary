package com.example.diary;

//创建一个日记的类
public class Note {
//类的属性和表的属性对应
    private int id;
    private byte[] picture;
    private String author;
    private String title;
    private String time;
    private String content;
//类的构造函数
    public Note(int id,String author,String title,String time,String content,byte[] picture) {
        this.author = author;
        this.title = title;
        this.time = time;
        this.content = content;
        this.id = id;
        this.picture = picture;
    }
//属性相应的方法
    public String getAuthor() {
        return this.author;
    }
    public String getTitle() {
        return this.title;
    }
    public String getTime() {
        return this.time;
    }
    public String getContent() {
        return this.content;
    }
    public int getId() { return this.id; }
    public byte[] getPicture() { return this.picture; }
}
