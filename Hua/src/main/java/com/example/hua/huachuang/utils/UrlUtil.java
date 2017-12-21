package com.example.hua.huachuang.utils;

/**
 * Created by hua on 2016/12/27.
 * 生成url
 */
public class UrlUtil {
    final public static String DEFAULT_ENCODE = "utf-8";
    //用于从百度申请榜单数据
    final public static String SCHEME = "http:";
    final public static String AUTHORITY = "tingapi.ting.baidu.com";
    final public static String PATH = "/v1/restserver/ting";
    //用于百度天气接口
    private static final String appKey = "YwbLfjfg66OvO2jwFcBGxl6ElCHF6pMy";
    private static final String sha1 = "90:ED:F0:C6:B4:58:AA:CD:5E:5A:D8:C3:76:5A:35:73:FB:18:6C:0E";
    private static final String packageName = "com.example.hua.myponymusic";

    public static String MakeBillBoardUrl(String dataType,int type, int sum, int offeset) {
        String query = "format="+dataType+"&calback=&from=webapp_music&method=baidu.ting.billboard.billList"
                +"&type="+type+"&size="+sum+"&offset="+offeset;
        return SCHEME+"//"+AUTHORITY+PATH+"?"+query;
    }

    public static String MakeMp3DownLoadUrl(String songId) {
        String query = "format=xml&calback=&from=webapp_music&method=baidu.ting.song.play&songid="+songId;
        return SCHEME+"//"+AUTHORITY+PATH+"?"+query;
    }

    public static String MakeWeatherUrl(String cityName) {
        String urlString = "http://api.map.baidu.com/telematics/v3/weather?location="+cityName+"&ak="+appKey+"&mcode="+sha1+";"+packageName;
        return urlString;
    }

    //内涵段子url
    //参数解析参考 https://github.com/jokermonn/-Api/blob/master/Neihan.md
    public static String MakeJokeUrl(String contentType,int conunt,String screenInfo) {
        return "http://iu.snssdk.com/neihan/stream/mix/v1/?mpic=1&webp=1&essence=1&" +
                "content_type="+contentType+"&message_cursor=-1&am_longitude=110&am_latitude=120&" +
                "am_city=%E5%8C%97%E4%BA%AC%E5%B8%82&am_loc_time=1463225362314&count="+conunt+"&" +
                "min_time=1465232121&screen_width=1450&do00le_col_mode=0&iid=3216590132&" +
                "device_id=32613520945&ac=wifi&channel=360&aid=7&app_name=joke_essay&" +
                "version_code=612&version_name=6.1.2&device_platform=android&ssmix=a&" +
                "device_type=sansung&device_brand=xiaomi&os_api=28&os_version=6.10.1&" +
                "uuid=326135942187625&openudid=3dg6s95rhg2a3dg5&manifest_version_code=612&" +
                "resolution="+screenInfo+"&dpi=620&update_version_code=6120";
    }

    public static String getContentTypeUrl() {
        return "http://lf.snssdk.com/neihan/service/tabs/?essence=1&iid=3216590132&" +
                "device_id=32613520945&ac=wifi&channel=360&aid=7&app_name=joke_essay&" +
                "version_code=612&version_name=6.1.2&device_platform=android&ssmix=a&" +
                "device_type=sansung&device_brand=xiaomi&os_api=28&os_version=6.10.1&" +
                "uuid=326135942187625&openudid=3dg6s95rhg2a3dg5&manifest_version_code=612&" +
                "resolution=1450*2800&dpi=620&update_version_code=6120";
    }

}
