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
public class DiaryAdapter extends ArrayAdapter<Diary> {

    private  int resourceID;
//构造函数
    public DiaryAdapter(Context context, int textViewResourceID, List<Diary> objects) {
        super(context,textViewResourceID,objects);
        resourceID = textViewResourceID;
    }

    @NonNull
    //重写父类的getView函数
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Diary diary = getItem(position);
        View view;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        }else {
            view = convertView;
        }

        TextView diary_time = (TextView) view.findViewById(R.id.diary_time);
        TextView diary_title = (TextView) view.findViewById(R.id.diary_title);
        diary_time.setText(diary.getTime());
        diary_title.setText(diary.getTitle());
        return view;
    }
}
