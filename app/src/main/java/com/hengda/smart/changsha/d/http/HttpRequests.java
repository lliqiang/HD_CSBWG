package com.hengda.smart.changsha.d.http;


import com.hengda.smart.changsha.d.model.DataModel;
import com.hengda.smart.changsha.d.model.MapUpdate;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by baishiwei on 2016/10/22.
 * Retrofit 请求接口
 */
public interface HttpRequests {


    /**
     * 数据库
     *
     * @return
     */
    @GET("index.php?g=mapi&m=Resource&a=datas_info")
    Observable<HttpResponse<DataModel>> getDBData();

    /**
     * 地图资源更新
     *
     * @return
     */
    @GET("index.php?g=mapi&m=Resource&a=update_map")
    Observable<HttpResponse<MapUpdate>> getMapRes();

    /*
    * 位置上传接口
    * deviceno	string
  机器号

app_kind	int
 设备类型1安卓，2IOS,3导览机

auto_num	string
 点位编号对应app数据表中的AutoNum字段

 electricity	int
电量
    * */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=positions&a=positions")
    Observable<HttpResponse<String>> uploadPosition(@Field("deviceno") String deviceno, @Field("app_kind") int app_kind, @Field("auto_num") String auto_num, @Field("electricity") int electricity);

    /**
     * 导览机报警
     *
     * @param deviceNo
     * @param type
     * @return
     */
    @FormUrlEncoded()
    @POST("index.php?g=mapi&m=positions&a=police")
    Observable<HttpResponse> alarm(@Field("deviceno") String deviceNo, @Field("type") String type);

    /**
     * 导览机预警
     *
     * @param deviceNo
     * @param type
     * @return
     */
    @FormUrlEncoded()
    @POST("index.php?g=mapi&m=positions&a=police")
    Observable<HttpResponse> preAlarm(@Field("deviceno") String deviceNo, @Field("type") String type, @Field("area_num") String areaNum);

/*
* 资源更新
* */
    @GET("index.php?g=mapi&m=Resource&a=update_zip")
    Observable<ResUpdate> getRes(@Query("version") int version);


//    @GET("index.php?g=mapi&m=Introduction&a=zbjd2")
//    Observable<HttpResponse<NearbyAttractions>> getNearbyAttractions();
}
