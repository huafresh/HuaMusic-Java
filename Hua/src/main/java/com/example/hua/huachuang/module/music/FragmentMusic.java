package com.example.hua.huachuang.module.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.base.BaseFragment;
import com.example.hua.huachuang.bean.music.MenuInfo;
import com.example.hua.huachuang.bean.music.RangeInfo;
import com.example.hua.huachuang.databinding.FragmentMusicBinding;
import com.example.hua.huachuang.databinding.IncludeSongMenuTitleBinding;
import com.example.hua.huachuang.databinding.SongMenuItemBinding;
import com.example.hua.huachuang.module.music.common.PopupWindowManager;
import com.example.hua.huachuang.module.music.down.DownActivity;
import com.example.hua.huachuang.module.music.local.LocalActivity;
import com.example.hua.huachuang.module.music.online.PreviewActivity;
import com.example.hua.huachuang.module.music.recently.RecentlyActivity;
import com.example.hua.huachuang.share.ShareList;
import com.example.hua.huachuang.utils.CommonUtil;
import com.example.hua.huachuang.utils.DensityUtil;
import com.example.hua.huachuang.zhy.CommonAdapter;
import com.example.hua.huachuang.zhy.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/2/17.
 */

public class FragmentMusic extends BaseFragment<FragmentMusicBinding>
        implements AdapterView.OnItemClickListener,View.OnClickListener{

    private ListView rangeList;
    private ListView menuList;
    private List<RangeInfo> rangeLists;
    private List<MenuInfo> menuLists;
    private LinearLayout musicContain;
    //歌单项数据绑定
    private SongMenuItemBinding menuItemBinding;
    //歌单标题数据绑定
    private IncludeSongMenuTitleBinding menuTitleBinding;
    //歌单标题
    private TextView menuSum;
    private LinearLayout titleContain;
    private LinearLayout titleMore;
    private ImageView arrow;
    private RotateAnimation rotateOpen;
    private RotateAnimation rotateClose;
    private boolean isMenuOpen;
    //分类适配器
    private CommonAdapter adapterRanger;
    //歌单适配器
    private CommonAdapter adapterMenu;

    private PopupWindowManager popupWindowManager;
    private int[] ids = {R.drawable.new_menu,R.drawable.menu_manager};

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initListDates();
        initList();
        initPopupWindow();
        setListener();
        showContent();
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void initViews() {
        rangeList = fragmentDataBinding.rangeList;
        menuList = fragmentDataBinding.menuList;

        menuTitleBinding = fragmentDataBinding.menuTitle;
        menuSum = menuTitleBinding.songMenuSum;
        titleContain = menuTitleBinding.songMenuTitle;
        titleMore = menuTitleBinding.songMenuTitleMore;
        arrow = menuTitleBinding.arrow;
        //每次打开界面，歌单列表都是摊开的
        isMenuOpen = true;
    }


    private void initList() {
        adapterRanger = new CommonAdapter<RangeInfo>(getActivity(),R.layout.fragment_music_item, rangeLists) {
            @Override
            protected void convert(ViewHolder viewHolder, RangeInfo item, int position) {
                viewHolder.setImageDrawable(R.id.music_local_image,item.getDrawable());
                viewHolder.setText(R.id.music_local_text,item.getTitle());
                viewHolder.setText(R.id.music_local_sum,item.getStringSum());
                Log.i("***convert**",item.getTitle());
                if(item.getTitle().equals("在线音乐")) {
                    viewHolder.setVisible(R.id.music_local_sum,false);
                } else viewHolder.setVisible(R.id.music_local_sum,true);
            }
        };
        rangeList.setAdapter(adapterRanger);
        adapterMenu = new CommonAdapter<MenuInfo>(getActivity(),R.layout.song_menu_item, menuLists) {
            @Override
            protected void convert(ViewHolder viewHolder, MenuInfo item, int position) {
                viewHolder.setImageResource(R.id.song_menu_cover,item.getCoverId());
                viewHolder.setText(R.id.song_menu_name,item.getName());
                viewHolder.setText(R.id.song_menu_sum,item.getStringSum());
            }
        };
        menuList.setAdapter(adapterMenu);
    }

    private void initPopupWindow() {
        popupWindowManager = new PopupWindowManager(getActivity());
        popupWindowManager.setTitle("创建的歌单");
        popupWindowManager.setListDates(new String[]{"创建新歌单","歌单管理"},
                new int[]{R.drawable.new_menu,R.drawable.menu_manager});
        popupWindowManager.setOnItemClick(new PopupWindowManager.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                doItemClick(position);
            }
        });
    }

    private void doItemClick(int position) {
        switch (position) {
            case 0: //创建新歌单
                CommonUtil.toast("new menu");
                break;
            case 1: //歌单管理
                CommonUtil.toast("menu manager");
                break;
        }
    }

    private void initListDates() {
        rangeLists = new ArrayList<>();
        rangeLists.add(new RangeInfo(DensityUtil.changeDrawableColor(getActivity(),R.drawable.music_local),
                getString(R.string.music_local), addBracket(ShareList.localList.size())));
        rangeLists.add(new RangeInfo(DensityUtil.changeDrawableColor(getActivity(),R.drawable.music_recently),
                getString(R.string.music_recently), addBracket(ShareList.recentlyList.size())));
        rangeLists.add(new RangeInfo(DensityUtil.changeDrawableColor(getActivity(),R.drawable.music_down),
                getString(R.string.music_down), addBracket(ShareList.haveDownList.size())));
        rangeLists.add(new RangeInfo(DensityUtil.changeDrawableColor(getActivity(),R.drawable.music_online),
                getString(R.string.music_online), addBracket(ShareList.haveDownList.size())));

        menuLists = new ArrayList<>();
        ShareList.menuInfoList.add(new MenuInfo(R.drawable.song_menu_default,
                getString(R.string.music_song_menu_default),getString(R.string.music_sum_default2)));
        //这里不能直接等，因为menuLists需要经常被清空
        copyList(menuLists,ShareList.menuInfoList);
    }

    private void copyList(List<MenuInfo> list1,List<MenuInfo> list2) {
        for (MenuInfo info : list2) {
            list1.add(info);
        }
    }

    @Override
    public int getContentId() {
        return R.layout.fragment_music;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (position) {
            case 0: //本地音乐
                intent.setAction(LocalActivity.ACTION);
                break;
            case 1: //最近播放
                intent.setAction(RecentlyActivity.ACTION);
                break;
            case 2: //下载管理
                intent.setAction(DownActivity.ACTION);
                break;
            case 3: //在线音乐
                intent.setAction(PreviewActivity.ACTION);
                break;
        }
        startActivity(intent);
    }

    /**
     * 更新界面
     */
    private void update() {
        //本地音乐
        RangeInfo info1 = rangeLists.get(0);
        info1.setStringSum(addBracket(ShareList.localList.size()));
        //播放记录
        RangeInfo info2 = rangeLists.get(1);
        info2.setStringSum(addBracket(ShareList.recentlyList.size()));
        //下载记录
        RangeInfo info3 = rangeLists.get(2);
        info3.setStringSum(addBracket(ShareList.haveDownList.size()));
        //歌单数
        menuSum.setText(addBracket(menuLists.size()));
        adapterRanger.notifyDataSetChanged();
        adapterMenu.notifyDataSetChanged();
    }

    /**
     * 给数字加括号
     */
    private String addBracket(int s) {
        return "("+s+")";
    }

    private void setListener() {
        rangeList.setOnItemClickListener(this);
        titleContain.setOnClickListener(this);
        titleMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_title:
                doMenuTitleClick();
                break;
            case R.id.song_menu_title_more:
                popupWindowManager.show();
                break;
        }
    }

    private void doMenuTitleClick() {
        //放在这里初始化，get方法才不返回0
        if(rotateOpen==null) {
            rotateOpen = new RotateAnimation(-90,0,arrow.getWidth()/2,arrow.getHeight()/2);
            rotateOpen.setDuration(200);
            rotateOpen.setFillAfter(true);
        }
        if (rotateClose==null) {
            rotateClose = new RotateAnimation(0,-90,arrow.getWidth()/2,arrow.getHeight()/2);
            rotateClose.setDuration(200);
            rotateClose.setFillAfter(true);
        }
        if(isMenuOpen) {
            arrow.startAnimation(rotateClose);
            isMenuOpen = false;
            menuLists.clear();
        }
        else {
            arrow.startAnimation(rotateOpen);
            isMenuOpen = true;
            copyList(menuLists,ShareList.menuInfoList);
        }
        adapterMenu.notifyDataSetChanged();
    }

}
