package com.example.hua.huachuang.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;

import com.example.hua.huachuang.R;

import java.text.DecimalFormat;


/**
 * Created by Administrator on 2015/10/19.
 * 各种转换
 */
public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 功能：把毫秒值转换为形如，3：45 的字符串
     */
    static public String valueTimeToString(int ms){
        int second = ms/1000;
        int minute = second/60;
        String timeString;
        if(second % 60 > 9)
            timeString = minute+":"+second % 60;
        else
            timeString = minute+":0"+second % 60;
        return timeString;
    }

    /**
     *  Lrc存储时间的格式是[00:12.30]，分别是分，秒，毫秒
     */
    static public int LrcTime2Int(String lrcString){
        String splitData1[] = lrcString.split(":");
        int totalMs;
        if(splitData1[1].contains(".")){
            String splitData2[] = splitData1[1].split("\\.");
            int minute = Integer.parseInt(splitData1[0]);
            int second = Integer.parseInt(splitData2[0]);
            int ms = Integer.parseInt(splitData2[1]);
            totalMs = minute * 60 * 1000 + second * 1000 + ms;
        }
        else{  //有些lrc文本并没有毫秒
            int minute = Integer.parseInt(splitData1[0]);
            int second = Integer.parseInt(splitData1[1]);
            totalMs = minute * 60 * 1000 + second * 1000;
        }
        return totalMs;
    }

    public static String valueToSize(double sizeByte){
        double sizeM = sizeByte / (1024 * 1024);
        String result;
        if(sizeM>=1) {
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            result = decimalFormat.format(sizeM)+"M";
        } else {
            int sizeKb = (int) (sizeM*1024);
            result = String.valueOf(sizeKb)+"KB";
        }
        return result;
    }

    /**
     * 由路径提取文件夹名称
     */
    public static String getFolderPath(String path){
        String[] tmp = path.split("/");
        String filePath = "";
        int partNum = tmp.length;
        for(int i = 0; i < partNum - 1; i++){
            filePath += tmp[i]+"/";
        }
        return filePath;
    }

    /**
     * 强转类型，主要是去掉警告
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }

    /**
     * 切换夜间模式时，红色图片转为白色的
     * 注意这里的上下文要用传的，全局那个是针对app的，不能用
     */
    public static Drawable changeDrawableColor(Context context,@DrawableRes int id) {
        Resources resources = context.getResources();
        Drawable drawable = new BitmapDrawable(resources, BitmapFactory.decodeResource(resources,id));
        TypedValue color = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.huaRedAndWhite,color,true);
        changeColor(drawable, color.data);
        return drawable;
    }

    /**
     * 给图片着色，选中为白色，非选择为暗白
     */
    public static Drawable tintDrawable(Context context,@DrawableRes int id) {
        Resources resources = context.getResources();
        Drawable drawable = new BitmapDrawable(resources, BitmapFactory.decodeResource(resources,id));
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        ColorStateList colorStateList = context.getResources().getColorStateList(R.color.white_and_white2);
        DrawableCompat.setTintList(wrappedDrawable, colorStateList);
        return wrappedDrawable;
    }

    /**
     * 执行颜色转换
     */
    private static void changeColor(Drawable drawable,int color) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color));
    }

    /**
     * 设置某个View的margin
     *
     * @param view   需要设置的view
     * @param isDp   需要设置的数值是否为DP
     * @param left   左边距
     * @param right  右边距
     * @param top    上边距
     * @param bottom 下边距
     * @return
     */
//    public static ViewGroup.LayoutParams setViewMargin(View view, boolean isDp, int left, int right, int top, int bottom) {
//        if (view == null) {
//            return null;
//        }
//
//        int leftPx = left;
//        int rightPx = right;
//        int topPx = top;
//        int bottomPx = bottom;
//        ViewGroup.LayoutParams params = view.getLayoutParams();
//        ViewGroup.MarginLayoutParams marginParams = null;
//        //获取view的margin设置参数
//        if (params instanceof ViewGroup.MarginLayoutParams) {
//            marginParams = (ViewGroup.MarginLayoutParams) params;
//        } else {
//            //不存在时创建一个新的参数
//            marginParams = new ViewGroup.MarginLayoutParams(params);
//        }
//
//        //根据DP与PX转换计算值
//        if (isDp) {
//            leftPx = dip2px(left);
//            rightPx = dip2px(right);
//            topPx = dip2px(top);
//            bottomPx = dip2px(bottom);
//        }
//        //设置margin
//        marginParams.setMargins(leftPx, topPx, rightPx, bottomPx);
//        view.setLayoutParams(marginParams);
//        view.requestLayout();
//        return marginParams;
//    }
}