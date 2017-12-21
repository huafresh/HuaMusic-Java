package com.example.hua.huachuang.module.music.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.bean.music.Music;
import com.example.hua.huachuang.utils.DensityUtil;

/**
 * Created by hua on 2016/12/7.
 * 显示歌曲详细信息
 */
public class DialogFragment_detail extends DialogFragment {
    private View view;
    private TextView tv_detail_title;
    private TextView tv_detail_artist;
    private TextView tv_detail_album;
    private TextView tv_detail_time;
    private TextView tv_detail_name;
    private TextView tv_detail_size;
    private TextView tv_detail_path;
    //要显示详细信息的歌曲
    private Music music;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,0);
        Bundle bundle = getArguments();
        music = (Music) bundle.getSerializable("music");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_detail_info,container);
        initDetailFragment();
        return view;
    }

    private void initDetailFragment(){
        tv_detail_title = (TextView) view.findViewById(R.id.tv_detail_title);
        tv_detail_artist = (TextView) view.findViewById(R.id.tv_detail_artist);
        tv_detail_album = (TextView) view.findViewById(R.id.tv_detail_album);
        tv_detail_time = (TextView) view.findViewById(R.id.tv_detail_time);
        tv_detail_name = (TextView) view.findViewById(R.id.tv_detail_name);
        tv_detail_size = (TextView) view.findViewById(R.id.tv_detail_size);
        tv_detail_path = (TextView) view.findViewById(R.id.tv_detail_path);

        String name = getText(R.string.detail_author)+ music.getAuthor();
        String album = getText(R.string.detail_album)+ music.getAlbumName();
        String time = getText(R.string.detail_duration)+ DensityUtil.valueTimeToString(music.getDuration());
        String fileName = getText(R.string.detail_file_name)+ music.getFileName();
        String size = getText(R.string.detail_size)+ DensityUtil.valueToSize(music.getFileSize());
        String path = getText(R.string.detail_path)+ music.getPath();

        tv_detail_title.setText(music.getSongName());
        tv_detail_artist.setText(name);
        tv_detail_album.setText(album);
        tv_detail_time.setText(time);
        tv_detail_name.setText(fileName);
        tv_detail_size.setText(size);
        tv_detail_path.setText(path);
    }
}
