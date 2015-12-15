package com.slk.buymood.utils.image;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class ImageFileCache implements ImageCache{
	 
	  @Override
	  public Bitmap getBitmap(String url) {
	    return ImageFileCacheUtils.getInstance().getImage(url);
	  }
	 
	  @Override
	  public void putBitmap(String url, Bitmap bitmap) {
	    ImageFileCacheUtils.getInstance().saveBitmap(bitmap, url);
	  }
	 
	}