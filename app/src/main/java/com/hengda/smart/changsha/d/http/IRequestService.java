package com.hengda.smart.changsha.d.http;






import com.hengda.smart.changsha.d.model.CheckResponse;
import com.hengda.smart.changsha.d.model.DataModel;
import com.hengda.smart.changsha.d.model.ScanBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/6/11 16:17
 * 邮箱：tailyou@163.com
 * 描述：
 */
public interface IRequestService {

    /**
     * 检查App版本更新
     *
     * @param appKey
     * @param appSecret
     * @param appKind
     * @param versionCode
     * @param deviceId
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?g=&m=Api&a=checkVersion")
    Observable<CheckResponse> checkVersion(@Field("appKey") String appKey,
                                           @Field("appSecret") String appSecret,
                                           @Field("appKind") int appKind,
                                           @Field("versionCode") int versionCode,
                                           @Field("deviceId") String deviceId);



    /*
    * 位置上传
    * */
    @FormUrlEncoded
    @POST("index.php?g=&m=Api&a=checkVersion")
    Observable<CheckResponse> positonUp(@Field("deviceno") String deviceno,
                                           @Field("app_kind") String app_kind,
                                           @Field("auto_num") String auto_num,
                                           @Field("versionCode") int versionCode,
                                           @Field("electricity") int electricity);
    /**
     * 请求机器号
     *
     * @param app_kind
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?a=request_deviceno")
    Observable<Response<String>> reqDeviceNo(@Field("app_kind") String app_kind);

    /*
    *
    * 更新资源
    * */
    @GET("index.php?g=mapi&m=Resource&a=update_zip")
    Observable<ResUpdate> getRes(@Query("version")int version);



/*
* 绑定机器号
* */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=Gateway&a=bind_deviceno")
    Observable<HttpResponse> bindDevice(@Field("deviceno") String deviceno,
                                           @Field("client_id") String client_id);

    /*
    * 浏览次数上传
    * */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=positions&a=upload_looks")
    Observable<HttpResponse> lookCount(@Field("exhibit_id") String exhibit_id);

    /**
     * 浏览、播放加一
     * @param exhibit_id
     * @param type
     * @param src
     * @return
     */
    @GET("index.php?g=mapi&m=exhibit&a=num_relative")
    Observable<ScanBean> scanCount(@Query("exhibit_id") String exhibit_id,@Query("type") String type,@Query("src") String src);
    /**
     * 数据库
     *
     * @return
     */
    @GET("index.php?g=mapi&m=Resource&a=datas_info")
    Observable<HttpResponse<DataModel>> getDBData();

}
