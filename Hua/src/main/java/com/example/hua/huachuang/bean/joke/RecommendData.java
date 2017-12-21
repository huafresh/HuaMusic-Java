package com.example.hua.huachuang.bean.joke;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hua on 2017/3/13.
 * json数据结构参考 https://github.com/jokermonn/-Api/blob/master/Neihan.md
 */

public class RecommendData {
    private PreviewData data;

    public PreviewData getData() {
        return data;
    }

    //返回数据概述
    public static class PreviewData {
        private String has_more;
        private String tip;
        private boolean has_new_message;
        private List<DetailData> data;

        public String getHas_more() {
            return has_more;
        }

        public String getTip() {
            return tip;
        }

        public boolean isHas_new_message() {
            return has_new_message;
        }

        public List<DetailData> getData() {
            return data;
        }
    }

    //具体的数据
    public static class DetailData {
        private GroupInfo group;
        private String display_time;
        private Bitmap thumbBitmap;

        public Bitmap getBitmap() {
            return thumbBitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.thumbBitmap = bitmap;
        }

        public GroupInfo getGroup() {
            return group;
        }

        public String getDisplay_time() {
            return display_time;
        }
    }

    //对应一条内涵内容
    public static class GroupInfo {
        @SerializedName("360p_video")
        private VideoInfo video360p;
        @SerializedName("480p_video")
        private VideoInfo video480p;
        @SerializedName("720p_video")
        private VideoInfo video720p;

        private String mp4_url;
        private String text;
        private String digg_count;
        private String duration;
        private String create_time;
        private String id;
        private String favorite_count;
        private String go_detail_count;
        private List<CoverUrl> url_list;
        private String user_favorite;
        private String share_type;
        private UserInfo user;
        private String is_can_share;
        private String share_url;
        private String content;
        private String video_height;
        private String comment_count;
        private String cover_image_uri;
        private String share_count;
        private String has_comments;
        private String play_count;
        private String video_width;
        private String category_name;
        private String category_visible;
        private String group_id;

        public VideoInfo getVideo360p() {
            return video360p;
        }

        public VideoInfo getVideo480p() {
            return video480p;
        }

        public VideoInfo getVideo720p() {
            return video720p;
        }

        public String getMp4_url() {
            return mp4_url;
        }

        public String getText() {
            return text;
        }

        public List<CoverUrl> getUrl_list() {
            return url_list;
        }

        public String getDigg_count() {
            return digg_count;
        }

        public String getCreate_time() {
            return create_time;
        }

        public String getDuration() {
            return duration;
        }

        public String getId() {
            return id;
        }

        public String getFavorite_count() {
            return favorite_count;
        }

        public String getGo_detail_count() {
            return go_detail_count;
        }

        public String getUser_favorite() {
            return user_favorite;
        }

        public String getShare_type() {
            return share_type;
        }

        public UserInfo getUser() {
            return user;
        }


        public String getShare_url() {
            return share_url;
        }

        public String getContent() {
            return content;
        }

        public String getVideo_height() {
            return video_height;
        }

        public String getComment_count() {
            return comment_count;
        }

        public String getCover_image_uri() {
            return cover_image_uri;
        }

        public String getShare_count() {
            return share_count;
        }


        public String getPlay_count() {
            return play_count;
        }

        public String getVideo_width() {
            return video_width;
        }

        public String getCategory_name() {
            return category_name;
        }

        public String getGroup_id() {
            return group_id;
        }
    }

    //视频信息
    public static class VideoInfo {
        private String width;
        private List<VideoUrl> url_list;
        private String height;

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }


        public List<VideoUrl> getUrl_list() {
            return url_list;
        }

        public void setUrl_list(List<VideoUrl> url_list) {
            this.url_list = url_list;
        }
    }


    public static class VideoUrl {
        private String url;

        public String getUrl() {
            return url;
        }
    }

    public static class CoverUrl {
        private String url;

        public String getUrl() {
            return url;
        }
    }

    public static class UserInfo {
        private String user_id;
        private String name;

        public String getUser_id() {
            return user_id;
        }

        public String getName() {
            return name;
        }
    }

}
