/*
 * Copyright (C) 2011 Alex Kuiper <http://www.nightwhistler.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hua.huachuang.htmlspanner.handlers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;

import com.example.hua.huachuang.htmlspanner.MyImageSpan;
import com.example.hua.huachuang.htmlspanner.SpanStack;
import com.example.hua.huachuang.htmlspanner.TagNodeHandler;

import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.URL;

/**
 * Handles image tags.
 * 
 * The default implementation tries to load images through a URL.openStream(),
 * override loadBitmap() to implement your own loading.
 * 
 * @author Alex Kuiper
 * 
 */
public class ImageHandler extends TagNodeHandler {

	Context context;
	Handler handler;
	int screenWidth ;

	public ImageHandler() {
	}

	public ImageHandler(Context context,int screenWidth, Handler handler) {
		this.context=context;
		this.screenWidth=screenWidth;
		this.handler=handler;
	}

	@Override
	public void handleTagNode(TagNode node, SpannableStringBuilder builder,int start, int end, SpanStack stack) {
		int height;
		String src = node.getAttributeByName("src");
		builder.append("\uFFFC");
		Bitmap bitmap = loadBitmap(src);
		if (bitmap != null) {
			Drawable drawable = new BitmapDrawable(bitmap);
			if(screenWidth!=0){
				Message message = handler.obtainMessage();
				message.obj = src;
				message.what = 1;
				message.sendToTarget();
				height=screenWidth*bitmap.getHeight()/bitmap.getWidth();
				drawable.setBounds(0, 0, screenWidth,height);
			}else{
				drawable.setBounds(0, 0, bitmap.getWidth() - 1,bitmap.getHeight() - 1);
			}
			MyImageSpan span=new MyImageSpan(drawable);
			span.setUrl(src);
            stack.pushSpan( span, start, builder.length() );
		}


	}

	/**
	 * Loads a Bitmap from the given url.
	 * 
	 * @param url
	 * @return a Bitmap, or null if it could not be loaded.
	 */
	protected Bitmap loadBitmap(String url) {
		try {
			return BitmapFactory.decodeStream(new URL(url).openStream());
		} catch (IOException io) {
			return null;
		}
	}
}
