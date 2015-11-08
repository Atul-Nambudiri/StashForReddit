package com.atulnambudiri.stashforreddit;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by atuln on 11/7/2015.
 */
public class VolleySingleton {
    private static VolleySingleton instance;    //The singleton instance
    private RequestQueue requestQueue;          //The requestQueue to hold all requests
    private ImageLoader imageLoader;            //Used to load images into views
    private Context context;                    //Application context

    /**
     * Since this is a singleton, the constructor is private
     * @param context The context of the application
     */
    private VolleySingleton(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    /**
     * Used to get the instance of the singleton. Either returns the existing instance, or makes one if one is not present
     * @param context   The context of the application
     * @return  The instance
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if(instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    /**
     * Returns the RequestQueue, or makes one if not present
     * @return  The RequestQueue
     */
    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Add a request to the Queue
     * @param req The request
     * @param <T> The type of request. IE JSON, Text, Image
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Gets the ImageLoader used to Load Images into the required views
     * @return The ImageLoader
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }


}
