package com.hengda.frame.tileview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hengda.frame.tileview.bean.Marker;
import com.hengda.frame.tileview.bitmaploader.DiskBitmapProvider;
import com.hengda.frame.tileview.bitmaploader.HttpBitmapProviderWithGlide;
import com.hengda.frame.tileview.bitmaploader.HttpBitmapProviderWithPicasso;
import com.qozix.tileview.TileView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiwei on 2017/1/22.
 */

public class HDTileView extends TileView {

    private Context mContext;

    private List<Marker> markers = new ArrayList<>();

    private static float lastScale=0f;

    public HDTileView(Context context) {
        this(context, null);
    }

    public HDTileView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HDTileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    /**
     * 初始化
     *
     * @param maxScale 缩放级数
     * @param width    总宽度
     * @param height   总高度
     * @param mapUrl   瓦片路徑
     */
    public void init(int maxScale, int width, int height,String mapUrl) {
        setSize(width, height);
        setScaleLimits(0, maxScale);
        addDetailLevel(1.000f, mapUrl + "/1000/%d_%d.png");
        addDetailLevel(0.500f, mapUrl + "/500/%d_%d.png");
        addDetailLevel(0.250f, mapUrl + "/250/%d_%d.png");
        addDetailLevel(0.125f, mapUrl + "/125/%d_%d.png");
        addZoomPanListener(new ZoomPanListener() {
            @Override
            public void onPanBegin(int x, int y, Origination origin) {
            }

            @Override
            public void onPanUpdate(int x, int y, Origination origin) {
            }

            @Override
            public void onPanEnd(int x, int y, Origination origin) {
                Log.i("onPanEnd","-----------------onPanEnd");
            }

            @Override
            public void onZoomBegin(float scale, Origination origin) {
            }

            @Override
            public void onZoomUpdate(float scale, Origination origin) {
            }

            @Override
            public void onZoomEnd(float scale, Origination origin) {
                Log.i("onZoomEnd","-----------------current scale:"+scale);
                Log.i("onZoomEnd","-----------------lastScale:"+lastScale);
                 if (markers.size() > 0&&Math.abs(lastScale-scale)>0.3) {
                     lastScale=scale;
//                    if (scale<0.8){
//                        lastScale=1.0f;
//                    }
//                    if (scale>=0.8&&scale<=1.6){
//                        lastScale=1.2f;
//                    }else if (scale>1.6){
//                        lastScale=1.4f;
//                    }
                     float temp_scale=scale<=0.5?scale:scale/2;
                    for (Marker marker : markers) {
                        removeMarker(marker.getMarkerView());
                        LayoutParams para = marker.getMarkerView().getLayoutParams();
                        para.width = (int) (marker.getOriginWidth() * temp_scale);
                        para.height = (int) (marker.getOriginHeight() * temp_scale);
                        marker.getMarkerView().setLayoutParams(para);
                        placeMarker(marker, marker.getX(), marker.getY());
                    }
                }else {
                    Log.i("onZoomEnd", "-----------------: false");
                }
            }
        });
    }

    public Bitmap zoomBitmap(Bitmap bm, float zoomlevel) {
        Matrix matrix = new Matrix();
        matrix.postScale(zoomlevel, zoomlevel);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    public void loadMapFromDisk(){
        setBitmapProvider(new DiskBitmapProvider());
    }

    public void loadMapFromHttpUseGlide(){
        setBitmapProvider(new HttpBitmapProviderWithGlide());
    }

    public void loadMapFromHttpUsePicasso(){
        setBitmapProvider(new HttpBitmapProviderWithPicasso());
    }

    /**
     * 设置预加载图片
     *
     * @param path
     */
    public void addSample(String path,boolean isHttp) {
        String url=isHttp?path:"file://" + path;
        ImageView sample = new ImageView(mContext);
        Picasso.with(mContext).load(url).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(sample);
        addView(sample, 0);
    }

    public void addSample(int resId) {
        ImageView sample = new ImageView(mContext);
        Picasso.with(mContext).load(resId).into(sample);
        addView(sample, 0);
    }

    /**
     * 最小全屏模式
     */
    public void setMinimumScaleFullScreen() {
        setMinimumScaleMode(MinimumScaleMode.FIT);
        setScale(0);
    }

    /**
     * delay时间内滑动到指定位置
     *
     * @param x
     * @param y
     * @param delay
     */
    public void slideToPosition(final double x, final double y, final int delay) {
        post(new Runnable() {
            @Override
            public void run() {
                setAnimationDuration(delay);
                slideToAndCenter(x, y);
            }
        });
    }

    /**
     * delay时间内滑动到指定位置并放大/缩小到指定scale
     *
     * @param x
     * @param y
     * @param scale
     * @param delay
     */
    public void slideToPositionWithScale(final double x, final double y, final float scale, final int delay) {
        post(new Runnable() {
            @Override
            public void run() {
                setAnimationDuration(delay);
                slideToAndCenterWithScale(x, y, scale);
            }
        });
    }

    /**
     * 添加一个marker
     *
     * @param resId
     * @param x
     * @param y
     */
    public void placeMarkerWithScale(int resId, double x, double y, Object tag) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(resId);
        placeMarker(imageView, x, y,tag);
    }

    /**
     * 添加一个marker
     *
     * @param bitmap
     * @param x
     * @param y
     */
    public void placeMarkerWithScale(Bitmap bitmap, double x, double y, Object tag) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(bitmap);
        placeMarker(imageView, x, y,tag);
    }

    /**
     * 移除所有marker
     */
    public void remmoveAllMarkers() {
        if (markers.size() > 0) {
            for (Marker marker : markers) {
                removeMarker(marker.getMarkerView());
            }
            markers.clear();
        }
    }

    /***************************
     * private methods
     *******************************************/
    private void placeMarker(ImageView imageView, double x, double y, Object tag) {
        //这里不能直接使用imageView.getWidth()和getHeight()方法获取大小，因为此时onMeasure（）方法还没有执行，可已使用如下方式或者ViewTreeObserver
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        imageView.measure(w, h);
        int width = imageView.getMeasuredWidth();
        int height = imageView.getMeasuredHeight();
        if (null != tag)
            imageView.setTag(tag);
        //把当前imageview 的大小，坐标加入集合为后续缩放做准备
        markers.add(new Marker(imageView, width, height, x, y));
        //初始化需要根据地图当前的scale重新调整一下imageview的大小
        imageView.setLayoutParams(new LayoutParams((int) (width * getScale()), (int) (height * getScale())));
        addMarker(imageView, x, y, -0.5f, -1.0f);
        statScaleAnimator(imageView);
    }

    /**
     * 暂时仅供内部调用
     *
     * @param marker
     * @param x
     * @param y
     */
    private void placeMarker(Marker marker, double x, double y) {
        addMarker(marker.getMarkerView(), x, y, -0.5f, -1.0f);
        statScaleAnimator(marker.getMarkerView());
    }

    /**
     * 生长动画
     *
     * @param view
     */
    private void statScaleAnimator(ImageView view) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
        view.startAnimation(animation);
    }

}
