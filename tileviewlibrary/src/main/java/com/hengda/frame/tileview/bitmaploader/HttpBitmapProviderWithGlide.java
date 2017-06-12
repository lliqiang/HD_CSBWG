package com.hengda.frame.tileview.bitmaploader;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.tiles.Tile;

/**
 * @author Mike Dunn, 2/19/16.
 */
public class HttpBitmapProviderWithGlide implements BitmapProvider {
    public Bitmap getBitmap(Tile tile, Context context) {
        Object data = tile.getData();
        if (data instanceof String) {
            String unformattedFileName = (String) tile.getData();
            String formattedFileName = String.format(unformattedFileName, tile.getColumn(), tile.getRow());
            try {
                return Glide.with(context)
                        .load(formattedFileName)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(tile.getWidth(), tile.getHeight())
                        .get()  ;
            } catch (Throwable t) {
            }
        }
        return null;
    }
}