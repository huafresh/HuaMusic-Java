package com.example.hua.huachuang.bean.music;

import java.util.List;

/**
 * 百度接口返回的jason结构（一首歌）
     {"song_list":
     [
       {
         "artist_id":"166",
         "language":"\u56fd\u8bed",
         "pic_big":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_150",
         "pic_small":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_90",
         "country":"\u6e2f\u53f0",
         "area":"1",
         "publishtime":"2017-02-14",
         "album_no":"0",
         "lrclink":"http:\/\/musicdata.baidu.com\/data2\/lrc\/e156f9f097cb5cc929b1774dd58c807e\/533358233\/533358233.lrc",
         "copy_type":"1",
         "hot":"297351",
         "all_artist_ting_uid":"1118",
         "resource_type":"0",
         "is_new":"1",
         "rank_change":"0",
         "rank":"1",
         "all_artist_id":"166",
         "style":"",
         "del_status":"0",
         "relate_status":"0",
         "toneid":"0",
         "all_rate":"64,128,256,320,flac",
         "file_duration":223,
         "has_mv_mobile":0,
         "versions":"\u5f71\u89c6\u539f\u58f0",
         "bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}",
         "song_id":"533357685",
         "title":"\u590f\u591c\u661f\u7a7a\u6d77",
         "ting_uid":"1118",
         "author":"\u5f20\u4fe1\u54f2",
         "album_id":"533357576",
         "album_title":"\u590f\u591c\u661f\u7a7a\u6d77",
         "is_first_publish":0,
         "havehigh":2,"charge":0,
         "has_mv":1,"learn":0,
         "song_source":"web",
         "piao_id":"0",
         "korean_bb_song":"0",
         "resource_type_ext":"0",
         "mv_provider":"0000000000",
         "artist_name":"\u5f20\u4fe1\u54f2"
        }
     ],

     "billboard":
     {
        "billboard_type":"1",
         "billboard_no":"2123",
         "update_date":"2017-03-04",
         "billboard_songnum":"189",
         "havemore":1,
         "name":"\u65b0\u6b4c\u699c",
         "comment":"\u8be5\u699c\u5355\u662f\u6839\u636e\u767e\u5ea6\u97f3\u4e50\u5e73\u53f0\u6b4c\u66f2\u6bcf\u65e5\u64ad\u653e\u91cf\u81ea\u52a8\u751f\u6210\u7684\u6570\u636e\u699c\u5355\uff0c\u7edf\u8ba1\u8303\u56f4\u4e3a\u8fd1\u671f\u53d1\u884c\u7684\u6b4c\u66f2\uff0c\u6bcf\u65e5\u66f4\u65b0\u4e00\u6b21",
         "pic_s640":"http:\/\/c.hiphotos.baidu.com\/ting\/pic\/item\/f7246b600c33874495c4d089530fd9f9d62aa0c6.jpg",
         "pic_s444":"http:\/\/d.hiphotos.baidu.com\/ting\/pic\/item\/78310a55b319ebc4845c84eb8026cffc1e17169f.jpg",
         "pic_s260":"http:\/\/b.hiphotos.baidu.com\/ting\/pic\/item\/e850352ac65c1038cb0f3cb0b0119313b07e894b.jpg",
         "pic_s210":"http:\/\/business.cdn.qianqian.com\/qianqian\/pic\/bos_client_c49310115801d43d42a98fdc357f6057.jpg",
         "web_url":"http:\/\/music.baidu.com\/top\/new"},
         "error_code":22000
     }
 */
public class BillBoard{
    //通过榜单类型返回的榜单信息
    private List<musicInfo> song_list;
    private BillBoardInfo billboard;

    //通过id请求返回的单曲数据
    private musicInfo songinfo;
    private fileInfo bitrate;

    //在scrollView中的顺序
    private int order;

    public musicInfo getSonginfo() {
        return songinfo;
    }

    public fileInfo getBitrate() {
        return bitrate;
    }

    public void setSonginfo(musicInfo songinfo) {
        this.songinfo = songinfo;
    }

    public List<musicInfo> getSong_list() {
        return song_list;
    }

    public BillBoardInfo getBillboard() {
        return billboard;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * 歌曲详细信息
     */
    public static class musicInfo {
        private String artist_id;
        private String language;
        private String pic_big;
        private String pic_small;
        private String country;
        private String lrclink;
        private String hot;
        private String file_duration;
        private String song_id;
        private String title;
        private String author;
        private String album_id;
        private String album_title;
        private String has_mv;
        private String artist_name;

        public String getArtist_id() {
            return artist_id;
        }

        public String getLanguage() {
            return language;
        }

        public String getPic_big() {
            return pic_big;
        }

        public String getPic_small() {
            return pic_small;
        }

        public String getCountry() {
            return country;
        }

        public String getLrclink() {
            return lrclink;
        }

        public String getHot() {
            return hot;
        }

        public String getFile_duration() {
            return file_duration;
        }

        public String getSong_id() {
            return song_id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getAlbum_id() {
            return album_id;
        }

        public String getAlbum_title() {
            return album_title;
        }

        public String getHas_mv() {
            return has_mv;
        }

        public String getArtist_name() {
            return artist_name;
        }
    }

        /**
         * 榜单信息
         */
        public static class BillBoardInfo {
            private int billboard_type;
            private String update_date;
            private int billboard_songnum;
            private int havemore;
            private String name;
            private String comment;
            private String pic_s640;
            private String pic_s444;
            private String pic_s260;
            private String pic_s210;
            private String web_url;

            public int getBillboard_type() {
                return billboard_type;
            }

            public String getUpdate_date() {
                return update_date;
            }

            public int getBillboard_songnum() {
                return billboard_songnum;
            }

            public int getHavemore() {
                return havemore;
            }

            public String getName() {
                return name;
            }

            public String getComment() {
                return comment;
            }

            public String getPic_s640() {
                return pic_s640;
            }

            public String getPic_s444() {
                return pic_s444;
            }

            public String getPic_s260() {
                return pic_s260;
            }

            public String getPic_s210() {
                return pic_s210;
            }

            public String getWeb_url() {
                return web_url;
            }
        }

    /**  单曲信息
         {"songinfo":
             {"special_type":0,
             "pic_huge":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_1000",
             "resource_type":"0",
             "pic_premium":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_500",
             "havehigh":2,
             "author":"张信哲",
             "toneid":"0",
             "has_mv":0,
             "song_id":"533357685",
             "piao_id":"0",
             "artist_id":"166",
             "lrclink":"http:\/\/musicdata.baidu.com\/data2\/lrc\/e156f9f097cb5cc929b1774dd58c807e\/533358233\/533358233.lrc",
             "relate_status":"1",
             "learn":0,
             "pic_big":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_150",
             "play_type":0,
             "album_id":"533357576",
             "album_title":"夏夜星空海",
             "bitrate_fee":"{\"0\":\"0|0\",\"1\":\"0|0\"}",
             "song_source":"web",
             "all_artist_id":"166",
             "all_artist_ting_uid":"1118",
             "all_rate":"64,128,256,320,flac",
             "charge":0,"copy_type":"0",
             "is_first_publish":0,
             "korean_bb_song":"0",
             "pic_radio":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_300",
             "has_mv_mobile":0,
             "title":"夏夜星空海",
             "pic_small":"http:\/\/musicdata.baidu.com\/data2\/pic\/5e521f1114507bf1a2b1df859ee62d28\/533357573\/533357573.jpg@s_0,w_90",
             "album_no":"0",
             "resource_type_ext":"0",
             "ting_uid":"1118"},
             "error_code":22000,
             "bitrate":{"show_link":"http:\/\/zhangmenshiting.baidu.com\/data2\/music\/34e6fb21e745abf77d4ce97ec74dbd59\/533357704\/533357704.mp3?xcode=d1595e5412a180ee9d88bb873c3ec0e5",
             "free":1,
             "song_file_id":533357704,
             "file_size":1781791,
             "file_extension":"mp3",
             "file_duration":223,
             "file_bitrate":64,
             "file_link":"http:\/\/yinyueshiting.baidu.com\/data2\/music\/34e6fb21e745abf77d4ce97ec74dbd59\/533357704\/533357704.mp3?xcode=d1595e5412a180ee9d88bb873c3ec0e5",
             "hash":"d349dfde206a1bfbde8d745c1daf5ff1fae807ed"}
         }
     */
    public static class fileInfo {
        private String file_size;
        private String file_extension;
        private String file_duration;
        private String file_bitrate;
        private String file_link;

        public String getFile_size() {
            return file_size;
        }

        public String getFile_extension() {
            return file_extension;
        }

        public String getFile_duration() {
            return file_duration;
        }

        public String getFile_bitrate() {
            return file_bitrate;
        }

        public String getFile_link() {
            return file_link;
        }
    }

        //榜单类型
        public static class Type {
            public static final int NEW = 1;
            public static final int HOT = 2;
            public static final int ROCK = 11;
            public static final int CHINA = 24;  //暂时用百度的影视金曲榜代替，等有了api再改
            public static final int EUROPE = 21;
            public static final int OLD = 22;
            public static final int NET = 25;
        }
}
