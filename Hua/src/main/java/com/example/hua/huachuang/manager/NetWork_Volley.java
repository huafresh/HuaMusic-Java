package com.example.hua.huachuang.manager;//package com.example.hua.huachuang.manager;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.util.LruCache;
//import android.widget.ImageView;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageLoader;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.example.hua.huachuang.Network.NetWorkCallBack;
//import com.example.hua.huachuang.Network.NetWorkRequest;
//import com.example.hua.huachuang.bean.BillBoard;
//import com.example.hua.huachuang.bean.BillBoardRequestData;
//import com.example.hua.huachuang.bean.Weather;
//import com.example.hua.huachuang.custom.MyStringRequest;
//import com.example.hua.huachuang.custom.XMLRequest;
//import com.example.hua.huachuang.utils.ParserUtil;
//import com.example.hua.huachuang.utils.SDUtil;
//import com.example.hua.huachuang.utils.CommonUtil;
//import com.example.hua.huachuang.utils.UrlUtil;
//
//import org.xmlpull.v1.XmlPullParser;
//
///**
// * Created by hua on 2016/12/27.
// * 网络请求实现类，使用volley框架实现，如果需要改变实现框架，只需重新实现接口NetWorkRequest即可
// */
//public class NetWork_Volley implements NetWorkRequest {
//    private static NetWork_Volley mNetWorkRequestManager;
//    private Activity context;
//    private RequestQueue requestQueue;
//    public LruCache<String,Bitmap> mLruCache = new LruCache<>(10*1024*1024);
//
//    public NetWork_Volley(Activity context) {
//        this.context = context;
//        requestQueue = Volley.newRequestQueue(context);
//    }
//
//    public static NetWork_Volley getInstance(Activity context) {
//        if(mNetWorkRequestManager == null) {
//            mNetWorkRequestManager = new NetWork_Volley(context);
//            return mNetWorkRequestManager;
//        }
//        return mNetWorkRequestManager;
//    }
//
//    @Override
//    public void BillBoardRequest(final BillBoardRequestData data,
//                                 final NetWorkCallBack<BillBoard> callBack) {
//        String urlString = UrlUtil.MakeBillBoardUrl(data.getType(),data.getSum(),data.getOffset());
//        final XMLRequest xmlRequest = new XMLRequest(Request.Method.GET, urlString,
//                new Response.Listener<XmlPullParser>() {
//                    @Override
//                    public void onResponse(XmlPullParser pullParser) {
//                        BillBoard billBoard = new BillBoard();
//                        billBoard.setBillBoardType(data.getType());
//                        ParserUtil.ResolveBillboardXML(pullParser,billBoard);
//                        callBack.onRespond(billBoard);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                callBack.onError(volleyError);
//            }
//        });
//        requestQueue.add(xmlRequest);
//    }
//
//    @Override
//    public void BillBoardRequest(final BillBoard billBoard,final BillBoardRequestData data,
//                                 final NetWorkCallBack<BillBoard> callBack) {
//        String urlString = UrlUtil.MakeBillBoardUrl(data.getType(),data.getSum(),data.getOffset());
//        final XMLRequest xmlRequest = new XMLRequest(Request.Method.GET, urlString,
//                new Response.Listener<XmlPullParser>() {
//                    @Override
//                    public void onResponse(XmlPullParser pullParser) {
//                        ParserUtil.ResolveBillboardXML(pullParser,billBoard);
//                        callBack.onRespond(billBoard);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                callBack.onError(volleyError);
//            }
//        });
//        requestQueue.add(xmlRequest);
//    }
//
//    @Override
//    public void BillBoardRequestSync(final BillBoard billBoard, final BillBoardRequestData data) {
//        String urlString = UrlUtil.MakeBillBoardUrl(data.getType(),data.getSum(),data.getOffset());
//        final XMLRequest xmlRequest = new XMLRequest(Request.Method.GET, urlString,
//                new Response.Listener<XmlPullParser>() {
//                    @Override
//                    public void onResponse(XmlPullParser pullParser) {
//                        billBoard.setBillBoardType(data.getType());
//                        ParserUtil.ResolveBillboardXML(pullParser,billBoard);
//                        billBoard.notify();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                billBoard.notify();
//            }
//        });
//        requestQueue.add(xmlRequest);
//        synchronized (billBoard) {
//            try {
//                billBoard.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void LoadImage(String urlString, final NetWorkCallBack<Bitmap> callBack) {
//        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
//            @Override
//            public Bitmap getBitmap(String s) {
//                return mLruCache.get(s);
//            }
//            @Override
//            public void putBitmap(String s, Bitmap bitmap) {
//                mLruCache.put(s,bitmap);
//            }
//        });
//        imageLoader.get(urlString, new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                callBack.onRespond(imageContainer.getBitmap());
//            }
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                callBack.onError(volleyError);
//            }
//        });
//    }
//
//
//    @Override
//    public void LoadImageSync(final String url, final ImageView imageView) {
//        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
//            @Override
//            public Bitmap getBitmap(String s) {
//                return mLruCache.get(s);
//            }
//            @Override
//            public void putBitmap(String s, Bitmap bitmap) {
//                mLruCache.put(s,bitmap);
//            }
//        });
//        imageLoader.get(url, new ImageLoader.ImageListener() {
//            @Override
//            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
//                if(imageView != null) imageView.setImageBitmap(imageContainer.getBitmap());
//                url.notify();
//            }
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                url.notify();
//            }
//        });
//        synchronized (url) {
//            try {
//                url.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void DownMusicLink(String songId, final NetWorkCallBack<String> callBack) {
//        String url = UrlUtil.MakeMp3DownLoadUrl(songId);
//        StringRequest stringRequest = new StringRequest(url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        String musicLink = ParserUtil.ResolveJason(s);
//                        callBack.onRespond(musicLink);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                callBack.onError(volleyError);
//            }
//        });
//        requestQueue.add(stringRequest);
//    }
//
//    @Override
//    public void DownLrc(String url, final String musicName, final NetWorkCallBack<String> callBack) {
//        MyStringRequest stringRequest = new MyStringRequest(url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(final String s) {
//                        if(SDUtil.saveLrc(context,s,musicName.replace(".mp3",".lrc"))==-1) {
//                            CommonUtil.toast("歌词保存失败");
//                        }
//                        context.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                callBack.onRespond(s);
//                            }
//                        });
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                callBack.onError(volleyError);
//            }
//        });
//        requestQueue.add(stringRequest);
//    }
////http://api.map.baidu.com/telematics/v3/weather？location=南昌&output=json&ak=YwbLfjfg66OvO2jwFcBGxl6ElCHF6pMy &mcode=90:ED:F0:C6:B4:58:AA:CD:5E:5A:D8:C3:76:5A:35:73:FB:18:6C:0E;com.example.hua.myponymusic
//    @Override
//    public void getWeather(String urlString, final NetWorkCallBack<Weather> callBack) {
//        XMLRequest request = new XMLRequest(Request.Method.GET, "http://api.map.baidu.com/telematics/v3/weather?location=114.04,22&ak=FK9mkfdQsloEngodbFl4FeY3",
//                new Response.Listener<XmlPullParser>() {
//                    @Override
//                    public void onResponse(XmlPullParser pullParser) {
//                        Weather weather = new Weather();
//                        ParserUtil.ResolverWeatherXML(pullParser,weather);
//                        callBack.onRespond(weather);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                callBack.onError(volleyError);
//            }
//        });
////        StringRequest request = new StringRequest("http://api.map.baidu.com/telematics/v3/weather?location=117.28029,31.843926&ak=FK9mkfdQsloEngodbFl4FeY3",
////                new Response.Listener<String>() {
////                    @Override
////                    public void onResponse(String s) {
////                        Log.i("***respond**",s);
////                    }
////                }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError volleyError) {
////
////            }
////        });
//        requestQueue.add(request);
//    }
//}
