package com.example.hua.huachuang.utils;


import com.example.hua.huachuang.bean.music.LrcContent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2016/12/10.
 */
public class LrcUtil {
    public static final String FOLDER_NAME= "HuaMusic";
    public static List<LrcContent> mLrcLists; //存放当前歌曲歌词
    /*
    *  解析lrc歌词文本，用List<LrcContent>保存，path是歌词文本路径
    * */
    public static List<LrcContent> ResolveLrc(String path){
        List<LrcContent> lrcContents = new ArrayList<>();
        try {
            File file = new File(path);
            if(!file.exists())
                return null;
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader read = new InputStreamReader(fileInputStream, "utf-8");
            BufferedReader buffer = new BufferedReader(read);
            String line;
            while((line = buffer.readLine()) != null){
                line = line.replace("[", "");
                line = line.replace("]", "@");
                String[] strings = line.split("@");
                if(strings.length > 1){  //有时间并且有内容才解析
                    LrcContent lrcContent = new LrcContent();
                    lrcContent.setTime(DensityUtil.LrcTime2Int(strings[0]));
                    lrcContent.setContent(strings[1]);
                    lrcContents.add(lrcContent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mLrcLists = lrcContents;
        return lrcContents;
    }
}
