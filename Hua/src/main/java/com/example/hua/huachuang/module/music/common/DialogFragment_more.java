package com.example.hua.huachuang.module.music.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hua.huachuang.R;


/**
 * Created by hua on 2016/12/7.
 * 点击更多时弹出的fragment
 */
public class DialogFragment_more extends DialogFragment implements AdapterView.OnItemClickListener{
    private View view;
    private ListView lv_dialog_list;
    final private String[] dialogList = {"分享","设为铃声","查看歌曲信息","删除"};
    private Fragment ParentFragment;
    private TextView tv_more_title;
    private String title;


    public static DialogFragment_more newInstance(String title) {
        DialogFragment_more dialogFragmentMore = new DialogFragment_more();
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        dialogFragmentMore.setArguments(bundle);
        return dialogFragmentMore;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        setStyle(DialogFragment.STYLE_NO_TITLE,0);
        ParentFragment = getTargetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_more,null);

        lv_dialog_list = (ListView) view.findViewById(R.id.lv_dialog_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),R.layout.dialog_list_item,
                R.id.tv_dialog_list_content,dialogList);
        lv_dialog_list.setAdapter(adapter);
        lv_dialog_list.setOnItemClickListener(this);
        tv_more_title = (TextView) view.findViewById(R.id.tv_more_title);
        tv_more_title.setText(title);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0: //分享
                ParentFragment.onActivityResult(0,0,null);
                break;
            case 1: //设为铃声
                ParentFragment.onActivityResult(0,1,null);
                break;
            case 2: //查看歌曲信息
                ParentFragment.onActivityResult(0,2,null);
                break;
            case 3: //删除
                ParentFragment.onActivityResult(0,3,null);
        }
    }

}
