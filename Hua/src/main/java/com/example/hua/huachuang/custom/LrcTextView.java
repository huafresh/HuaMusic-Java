package com.example.hua.huachuang.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.hua.huachuang.R;
import com.example.hua.huachuang.bean.music.LrcContent;

import java.util.List;


public class LrcTextView extends TextView {
    private Paint currentPaint;
    private Paint nonePaint;
    private Paint noLrcPaint;
    private Context context;
    private float LrcTextSize;
    private float lrcHeight;
    private int curColor;
    private int noneColor;
    //歌词
    private List<LrcContent> lrcContents;
    //歌词的位置
    public int index;
    //显示歌词的视图宽高
    private int with;
    private int height;

    public LrcTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LrcTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public LrcTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void init(){
        currentPaint = new Paint();
        currentPaint.setTextAlign(Paint.Align.CENTER);

        nonePaint = new Paint();
        nonePaint.setTextAlign(Paint.Align.CENTER);

        noLrcPaint = new Paint();
        noLrcPaint.setTextAlign(Paint.Align.CENTER);

        index = 0;

        LrcTextSize = context.getResources().getDimension(R.dimen.lrc_text_size);
        lrcHeight = context.getResources().getDimension(R.dimen.lrc_height);
        curColor = context.getResources().getColor(R.color.lrc_current_color);
        noneColor = context.getResources().getColor(R.color.lrc_none_color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        currentPaint.setColor(curColor);
        currentPaint.setTextSize(LrcTextSize);

        nonePaint.setColor(noneColor);
        nonePaint.setTextSize(LrcTextSize);

        noLrcPaint.setColor(Color.argb(210, 251, 248, 29));
        noLrcPaint.setTextSize(LrcTextSize);

        if(lrcContents == null) {
            canvas.drawText("暂无歌词", with / 2, height / 2, noLrcPaint);
            return;
        }
        if(lrcContents.size() == 0)
        {
            canvas.drawText("暂无歌词", with / 2, height / 2, noLrcPaint);
            return;
        }

        //画当前歌词
        canvas.drawText(lrcContents.get(index).getContent(), with / 2,
                height / 2 , currentPaint);

        //下部分
        float tmpHeight = LrcTextSize + lrcHeight;
        int downIndex = index;
        while(downIndex < lrcContents.size())
        {
            downIndex++;
            if((tmpHeight < height / 2) && (downIndex < lrcContents.size()))
            {
                canvas.drawText(lrcContents.get(downIndex).getContent(), with / 2,
                        height / 2 + tmpHeight, nonePaint);
                tmpHeight += LrcTextSize + lrcHeight;
            }
        }

        //上部分
        tmpHeight = LrcTextSize + lrcHeight;
        int upIndex = index;
        while(upIndex > 0)
        {
            upIndex--;
            if((tmpHeight + LrcTextSize < height / 2) && (downIndex >= 0))
            {
                canvas.drawText(lrcContents.get(upIndex).getContent(), with / 2,
                        height / 2 - tmpHeight, nonePaint);
                tmpHeight += LrcTextSize + lrcHeight;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        this.with = w;
        this.height = h;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public void setLrc(List<LrcContent> lrcList){
        this.lrcContents = lrcList;
    }

    public void startLrcAnimation(){
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation animation = new TranslateAnimation(
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, LrcTextSize+lrcHeight,
                Animation.ABSOLUTE, 0f
        );
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animationSet.addAnimation(animation);
        this.startAnimation(animationSet);
    }
}
