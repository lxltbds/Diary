package com.example.diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

//自定义日记类的适配器
public class NoteAdapter extends ArrayAdapter<Note> {

    private  int resourceID;
//构造函数
    public NoteAdapter(Context context, int textViewResourceID, List<Note> objects) {
        super(context,textViewResourceID,objects);
        resourceID = textViewResourceID;
    }

    @NonNull
//重写父类的getView函数
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Note note = getItem(position);
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        }else {
            view = convertView;
        }
//显示时间和标题
        TextView diary_time = (TextView) view.findViewById(R.id.diary_time);
        TextView diary_title = (TextView) view.findViewById(R.id.diary_title);
        diary_time.setText(note.getTime());
        diary_title.setText(note.getTitle());
        return view;
    }
}
