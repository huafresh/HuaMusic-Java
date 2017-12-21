package com.example.hua.huachuang.utils;

import com.example.hua.huachuang.bean.news.NewInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hua on 2017/3/12.
 */

public class JSoupUtil {

    /**
     * 获取腾讯新闻详细信息。
     * 返回html字符串
     */
    public static String obtainNewDetailInfo(String url,NewInfo newInfo) {
        Document doc;
        String article = null;
        try {
            doc = Jsoup.connect(url).get();
            //爬取来源和日期, 这个也暂放，因为数据源不稳定
            Elements els = doc.getElementsByTag("span");
            for (Element el : els) {
                String from = null;
                String time = null;
                if(el.className().equals("a_source"))
                    from = el.text();
                else if(el.className().equals("a_time"))
                    time = el.text();
                newInfo.setFrom(from);
                newInfo.setPubTime(time);
            }
            //爬取正文
            Element el = doc.getElementById("Cnt-Main-Article-QQ");
            article = el.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return article;
    }

    /**
     * 获取腾讯新闻预览信息。
     * 算法 ：先收集所有的新闻，放在集合。再分类收集所有图片，最后对比新闻链接，把他们联系在一起
     */
    public static List<NewInfo> obtainNewPreInfo(String url) {
        Document doc = null;
        List<NewInfo> infoLists = new ArrayList<>();
        HashMap<String,String> picHash1 = new HashMap<>();
        HashMap<String,String> picHash2 = new HashMap<>();
        try {
            doc = Jsoup.connect(url).get();
            //爬取所有新闻链接
            Elements newEls = doc.select("a.linkto");
            for (int i = 0; i < newEls.size(); i++) {
                NewInfo info = new NewInfo();
                Element el = newEls.get(i);
                info.setTitle(el.text());
                info.setNewLink(el.attr("href"));
                infoLists.add(info);
            }
            //爬取类型1图片链接
            Elements picEls = doc.select("a.pic");
            for (Element element : picEls) {
                String link = element.attr("href");
                String src = null;

                Elements els = element.select("img.picto");
                for (Element tmp : els) {
                    src = tmp.attr("src");
                    //网站里明明属性名称是src，jsoup非给人家加下划线，而且只是部分加。无语了
                    if(src.equals("")) {
                        src = tmp.attr("_src");
                    }
                }
                picHash1.put(link,src);
            }
            //爬取类型2图片信息，这个暂时放弃，先实现基本功能再说
//            Elements picsEls = doc.select("li.pic");
//            for (Element element : picsEls) {
//                String link = null;
//                String src = null;
//                Elements els = element.getElementsByTag("a");
//                for (Element el : els) {
//                    link = el.attr("href");
//                    Elements els2 = el.select("img.picto");
//                    for (Element tmp : els2) {
//                        src = tmp.attr("src");
//                    }
//                }
//                picHash2.put(link,src);
//            }
            //新闻与图片进行匹配
            matchNew(picHash1,picHash2,infoLists);
//            过滤掉无图新闻
            removeNoPic(infoLists);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return infoLists;
    }

    private static void matchNew(HashMap<String,String> picHash1,
                                 HashMap<String,String> picHash2,
                                 List<NewInfo> lists) {
        matchNew1(picHash1,lists);
        //matchNew2(picHash2,lists);
    }

    private static void matchNew1(HashMap<String,String> picHash, List<NewInfo> lists) {
        for (String key : picHash.keySet()) {
            for (NewInfo info : lists) {
                if(key.equals(info.getNewLink())) {
                    info.setPicLink(picHash.get(key));
                }
            }
        }
    }

    private static void matchNew2(HashMap<String,String> picHash, List<NewInfo> lists) {
        for (String key : picHash.keySet()) {
            for (NewInfo info : lists) {
                if(key.equals(info.getNewLink())) {
                    info.getPicLinks().add(picHash.get(key));
                }
            }
        }
    }

    private static void removeNoPic(List<NewInfo> lists) {
        List<NewInfo> tmpLists = new ArrayList<>();
        tmpLists.addAll(lists);
        lists.clear();
        for (int i = 0; i < tmpLists.size(); i++) {
            NewInfo info = tmpLists.get(i);
            if(info.getPicLink()!=null)
                lists.add(tmpLists.get(i));
        }
    }

    /**
     * 根据链接查找对应的新闻对象
     */
    private static NewInfo getNewInfo(String link, List<NewInfo> lists) {
        for (NewInfo info : lists) {
            if(info.getNewLink().equals(link))
                return info;
        }
        return null;
    }

}
