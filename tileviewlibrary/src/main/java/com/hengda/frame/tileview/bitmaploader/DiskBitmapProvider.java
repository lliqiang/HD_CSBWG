package com.hengda.frame.tileview.bitmaploader;

import android.content.Context;
import android.graphics.Bitmap;

import com.qozix.tileview.graphics.BitmapProvider;
import com.qozix.tileview.tiles.Tile;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * @author Mike Dunn, 2/19/16.
 */
public class DiskBitmapProvider implements BitmapProvider {
    public Bitmap getBitmap(Tile tile, Context context) {
        Object data = tile.getData();
        if (data instanceof String) {
            String unformattedFileName = (String) tile.getData();
            String formattedFileName = String.format(unformattedFileName, tile.getColumn(), tile.getRow());
            try {
                return Picasso.with(context).load("file://" + formattedFileName).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).get();
            } catch (Throwable t) {
            }
        }
        return null;
    }
}