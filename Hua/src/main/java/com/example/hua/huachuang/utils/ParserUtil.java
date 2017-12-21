package com.example.hua.huachuang.utils;

import com.example.hua.huachuang.bean.music.LrcContent;
import com.example.hua.huachuang.bean.music.Weather;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2016/12/19.
 * 解析网络数据
 */
public class ParserUtil {
    /**
     * XML节点名称
     */
    final public static String PIC_SMALL = "pic_small";
    final public static String PIC_BIG = "pic_big";
    final public static String LRC_LINK = "lrclink";
    final public static String TITLE = "title";
    final public static String ARTIST = "artist_name";
    final public static String ALBUM_TITLE = "album_title";
    final public static String PREVIEW_IMAGE_RUL = "pic_s260";
    final public static String HEAD_IMAGE_URL = "pic_s640";
    final public static String HEAD_IMAGE__BAC_URL = "pic_s444";
    final public static String HEAD_NAME = "name";
    final public static String HEAD_UPDATE_TIME = "update_date";
    final public static String HEAD_COMMENT = "comment";
    final public static String SONG_ID = "song_id";

    public static final String CURRENT_CITY = "currentCity";
    public static final String PIC_DAY = "dayPictureUrl";
    public static final String WEATHER = "weather";
    public static final String WIND = "wind";
    public static final String TEMPERATURE = "temperature";
    public static final String DATE = "date";


    /**
     * 解析jason数据，得到音乐文件uri
     */
    public static String ResolveJason(String jasonString) {
        String file_link = "";
        try {
            JSONObject object = new JSONObject(jasonString).getJSONObject("bitrate");
            file_link = object.getString("file_link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return file_link;
    }

    public static void ResolverWeatherXML(XmlPullParser pullParser, Weather weather) {
        try {
            int eventType = pullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String nodeName = pullParser.getName();
                        switch (nodeName) {
                            case CURRENT_CITY:
                                pullParser.next();
                                if(weather.getCityName() == null)
                                    weather.setCityName(pullParser.getText());
                                break;
                            case PIC_DAY:
                                pullParser.next();
                                if(weather.getImageUrl() == null)
                                    weather.setImageUrl(pullParser.getText());
                                break;
                            case WEATHER:
                                pullParser.next();
                                if(weather.getWeather() == null)
                                    weather.setWeather(pullParser.getText());
                                break;
                            case WIND:
                                pullParser.next();
                                if(weather.getWind() == null)
                                    weather.setWind(pullParser.getText());
                                break;
                            case DATE:
                                pullParser.next();
                                parserTemperature(pullParser.getText(),weather);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                eventType = pullParser.next();
            }
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void parserTemperature(String date, Weather weather) {
        String[] results = date.split("：");
        if(results.length > 1)
            weather.setTemperature(results[1].substring(0,results[1].length()-1));
    }

    /**
     * 解析歌词字符串，得到List<LrcContent>，解析失败返回null
     */
    public static List<LrcContent> ResolveLrc(String s){
        List<LrcContent> lists = null;
        s = s.replace("[", "").replace("]", "@");
        String[] strings = s.split("@");
        if(strings.length > 1){  //有时间并且有内容才解析
            LrcContent lrcContent = new LrcContent();
            lists = new ArrayList<>();
            lrcContent.setTime(DensityUtil.LrcTime2Int(strings[0]));
            lrcContent.setContent(strings[1]);
            lists.add(lrcContent);
        }
        return lists;
    }


}
