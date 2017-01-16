package com.infosupport.team2.febestelling.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Robin on 16-1-2017.
 */

public class AppSingleton {
    private static AppSingleton mAppSingletonInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    private AppSingleton(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache(){
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url){
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap){
                cache.put(url,bitmap);
            }
        });
    }

    public static synchronized AppSingleton getInstance(Context context){
        if(mAppSingletonInstance == null){
            mAppSingletonInstance = new AppSingleton(context);
        }
        return mAppSingletonInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T>request, String tag){
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public ImageLoader getmImageLoader(){
        return mImageLoader;
    }

    public void cancelPendingRequest(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
