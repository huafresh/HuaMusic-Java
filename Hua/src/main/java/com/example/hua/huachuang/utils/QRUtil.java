package com.example.hua.huachuang.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by hua on 2017/2/14.
 */

public class QRUtil {
    /**
     * 生成边长为sizePix的二维码图片，返回保存的路径
     *
     */
    public static String createQRImage(Context context, final String content, final int sizePix,
                                       int logoResId) {

        final Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(),logoResId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //encodeQRImage(content,sizePix,sizePix,logoBitmap);
            }
        }).start();
        return null;

    }

    static String encodeQRImage(final Activity context, final String content, final int width, final int height) {
        File file = Environment.getExternalStorageDirectory();
        final String path = file.getAbsolutePath()+"/hua.png";
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<EncodeHintType, String> hints = new HashMap<>();
                hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
                    int[] pixels = new int[width*height];
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            if(bitMatrix.get(j,i)) {
                                pixels[i*width+j] = 0xff000000;
                            } else {
                                pixels[i*width+j] = 0xffffffff;
                            }
                        }
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(path);
                    Bitmap bitmap = Bitmap.createBitmap(pixels,width,height, Bitmap.Config.ARGB_8888);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"生成完毕",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (WriterException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return path;
    }


    static void resolveQRImage(final Activity context, final String QRImagePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(QRImagePath);
                    MultiFormatReader reader = new MultiFormatReader();
                    HybridBinarizer hybridBinarizer = new HybridBinarizer(new RgbLuminanceSource(bitmap));
                    BinaryBitmap binaryBitmap = new BinaryBitmap(hybridBinarizer);
                    final Result result = reader.decode(binaryBitmap);
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,result.getText(),Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private static class RgbLuminanceSource extends LuminanceSource {
        private byte[] bitmapPixels;

        RgbLuminanceSource(Bitmap bitmap) {
            super(bitmap.getWidth(), bitmap.getHeight());
            bitmapPixels = new byte[bitmap.getWidth()*bitmap.getHeight()];
            int[] intPixels = new int[bitmap.getWidth()*bitmap.getHeight()];
            bitmap.getPixels(intPixels,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
            for (int i = 0; i < intPixels.length; i++) {
                bitmapPixels[i] = (byte) intPixels[i];
            }
        }

        @Override
        public byte[] getRow(int i, byte[] bytes) {
            System.arraycopy(bitmapPixels,i*getWidth(),bytes,0,getWidth());
            return bytes;
        }

        @Override
        public byte[] getMatrix() {
            return bitmapPixels;
        }
    }
}
