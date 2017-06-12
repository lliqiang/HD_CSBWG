package com.hengda.smart.changsha.d.model;

import android.widget.ImageView;

/**
 * Created by lenovo on 2017/3/31.
 */

public class Mark {
    private ImageView markerView;
    private int originWidth;
    private int originHeight;
    private double x;
    private double y;

    public Mark(ImageView markerView, int originWidth, int originHeight, double x, double y) {
        this.markerView = markerView;
        this.originWidth = originWidth;
        this.originHeight = originHeight;
        this.x = x;
        this.y = y;
    }

    public ImageView getMarkerView() {
        return markerView;
    }

    public void setMarkerView(ImageView markerView) {
        this.markerView = markerView;
    }

    public int getOriginWidth() {
        return originWidth;
    }

    public void setOriginWidth(int originWidth) {
        this.originWidth = originWidth;
    }

    public int getOriginHeight() {
        return originHeight;
    }

    public void setOriginHeight(int originHeight) {
        this.originHeight = originHeight;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
