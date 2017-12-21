package com.example.hua.huachuang.data;

import android.content.Context;
import android.os.Environment;


import com.example.hua.huachuang.base.HuaApplication;
import com.example.hua.huachuang.bean.music.DowningInfo;
import com.example.hua.huachuang.bean.music.LrcContent;
import com.example.hua.huachuang.utils.DensityUtil;
import com.example.hua.huachuang.utils.LrcUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huazai on 2016/9/10.
 * 负责sd卡的存取
 */
public class SDUtil {

    /**
     * 关闭流操作太麻烦了，封装一下
     */
    private static class StreamUtil<T extends Closeable> {
        void close(T stream) {
            if(stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 保存对象到本地文件，保存路径是：/data/data/包名/files/
     */
    public static void object2file(Context context, Object object, String fileName) {
        FileOutputStream outputStream = null;
        ObjectOutputStream os = null;
        try {
            outputStream = context.openFileOutput(fileName,Context.MODE_PRIVATE);
            os = new ObjectOutputStream(outputStream);
            os.writeObject(object);
            os.flush();
        }  catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            new StreamUtil<ObjectOutputStream>().close(os);
        }
    }

    /**
     * 文件转为对象
     * @param fileName 只支持这个目录的文件/data/data/包名/files
     * @return 对象
     */
    public static Object file2object(Context context, String fileName) {
        Object tmp = null;
        FileInputStream inputStream = null;
        ObjectInputStream is = null;
        try {
            inputStream = context.openFileInput(fileName);
            is = new ObjectInputStream(inputStream);
            tmp = is.readObject();
        }  catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            new StreamUtil<ObjectInputStream>().close(is);
        }
        return tmp;
    }

    /**
     * 加载歌词，只扫描固定文件夹。歌词文本不存在返回null
     */
    public static List<LrcContent> loadLrc(String name){
        List<LrcContent> lrcContents = new ArrayList<>();
        FileInputStream fileInputStream = null;
        InputStreamReader read = null;
        BufferedReader buffer = null;
        try {
            String lrcDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+ LrcUtil.FOLDER_NAME+"/";
            File lrcFile = getFile(lrcDir,name);
            if(lrcFile==null) return null;
            fileInputStream = new FileInputStream(lrcFile);
            read = new InputStreamReader(fileInputStream, "utf-8");
            buffer = new BufferedReader(read);
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
        finally {
            new StreamUtil<BufferedReader>().close(buffer);
        }
        return lrcContents;
    }

    /**
     * 保存歌词到歌词文件夹，出错返回-1
     * @param content 歌词文本字符串
     */
    public static int saveLrc(Context context, String content, String saveName) {
        File lrcFolder = getLrcFolder(context);
        if(lrcFolder==null) return -1;
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(lrcFolder.getPath()+"/"+saveName);
            outputStream.write(content.getBytes());
        }  catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            new StreamUtil<FileOutputStream>().close(outputStream);
        }
        return 0;
    }

    /**
     * 下载网络文件到本地，有sd卡时保存在sd卡，没有则保存内部存储器
     * @param is 网络流
     * @param info 文件信息
     */
    public static void saveToFile(InputStream is, DowningInfo info) {
        String savePath = null;
        if(isHaveSD()) {
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/hua/"+info.getDisplayName();
        } else {
            savePath = HuaApplication.getInstance().getFilesDir().getAbsolutePath()+"/hua/"+info.getDisplayName();
        }
        long current = info.getCurrent();
        long total = info.getTotal();
        RandomAccessFile accessFile = null;
        FileChannel channel = null;
        MappedByteBuffer mappedByteBuffer = null;
        byte[] buffer = new byte[2048];
        try {
            accessFile = new RandomAccessFile(savePath,"rwd");
            channel = accessFile.getChannel();
            mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, current, total);
            int len;
            while((len = is.read(buffer)) != -1) {
                current += len;
                mappedByteBuffer.put(buffer,0,buffer.length);
                info.setCurrent(current);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            new StreamUtil<RandomAccessFile>().close(accessFile);
            new StreamUtil<FileChannel>().close(channel);
        }
    }

    /**
     * 缓存文件
     * @param is 数据来源
     * @param name 缓存文件名
     * @param position 缓存文件起始位置
     */
    public static void fileToCache(InputStream is, String name, long position) {
        String cacheDir = HuaApplication.getInstance().getCacheDir().getPath();
        try {
            File file = new File(cacheDir,name+".cache");
            RandomAccessFile accessFile = new RandomAccessFile(file,"rwd");
            accessFile.seek(position);
            byte[] buffer = new byte[2048];
            while(is.read(buffer) != -1) {
                accessFile.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝缓存到SD卡
     * @param name 要拷贝的文件名
     */
    public static void copyCacheToSD(String name) {
        String cacheDir = HuaApplication.getInstance().getCacheDir().getPath();
        File file = getFile(cacheDir, name);
        if(file == null) return;
        String SDDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String savePath = SDDir +"/huaChuang";
        try {
            doCopy(new FileInputStream(file),name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝缓存文件到内部存储器
     * @param name 要拷贝的文件名
     */
    public static void copyCacheToInternal(String name) {
        //判断缓存是否存在
        String cacheDir = HuaApplication.getInstance().getCacheDir().getPath();
        File file = getFile(cacheDir, name);
        if(file == null) return;
        String fileDir = HuaApplication.getInstance().getFilesDir().getPath();
        String savePath = fileDir+"/hua";
        try {
            doCopy(new FileInputStream(file),name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行文件到指定路径
     * @param is 数据来源
     * @param path 路径
     */
    private static void doCopy(InputStream is, String path) {
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            byte[] buffer = new byte[2048];
            bufferedInputStream = new BufferedInputStream(is);
            fileOutputStream = new FileOutputStream(path);
            while(bufferedInputStream.read(buffer) != -1) {
                fileOutputStream.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            new StreamUtil<BufferedInputStream>().close(bufferedInputStream);
            new StreamUtil<FileOutputStream>().close(fileOutputStream);
        }
    }

    /**
     * @return 歌词文件夹的File对象，失败返回null
     */
    private static File getLrcFolder(Context context) {
        String fileDir = context.getFilesDir().getPath();
        File folder = new File(fileDir+"/lrc");
        if(!folder.exists()) {
            boolean result = folder.mkdir();
            if (!result) return null;
        }
        return folder;
    }

    /**
     * 获取某个文件夹下某个文件的File对象，文件不存在返回null
     * @param dirPath 文件夹
     * @param name 文件名
     */
    private static File getFile(String dirPath, String name) {
        if((dirPath==null) || (name==null)) return null;
        File folder = new File(dirPath);
        if(!folder.exists()) {
            boolean result = folder.mkdir();
            if(!result) return null;
        }
        File[] files = folder.listFiles();
        if(files.length==0) return null;
        for (File file1 : files) {
            if (file1.getName().equals(name)) {
                return file1;
            }
        }
        return null;
    }

    /**
     * 判断是否有sd卡
     */
    private static boolean isHaveSD() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}

