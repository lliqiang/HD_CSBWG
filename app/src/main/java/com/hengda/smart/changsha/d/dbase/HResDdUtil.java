package com.hengda.smart.changsha.d.dbase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hengda.smart.changsha.d.app.HdAppConfig;


/**
 * 作者：Tailyou
 * 时间：2016/1/12 15:12
 * 邮箱：tailyou@163.com
 * 描述：资源数据库工具类-单例模式
 */
public class HResDdUtil {

    private SQLiteDatabase db = null;

    /**
     * 单例模式
     */
    private static volatile HResDdUtil instance = null;

    /**
     * 获取实例
     *
     * @return
     */
    public static HResDdUtil getInstance() {
        if (instance == null) {
            synchronized (HResDdUtil.class) {
                if (instance == null) {
                    instance = new HResDdUtil();
                }
            }
        }
        return instance;
    }


    /**
     * 私有构造方法
     */
    private HResDdUtil() {
    }


    /**
     * 打开指定文件路径对应的数据库
     *
     * @param dbFilePath
     */
    public void openDB(String dbFilePath) {
        closeDB();
        try {

            db = SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase
                    .NO_LOCALIZED_COLLATORS);
        } catch (Exception e) {
            e.printStackTrace();
            closeDB();
        }
    }

    /**
     * 关闭数据库
     */
    public void closeDB() {
        if (db != null)
            db = null;
    }

    /**
     * 基础查询方法
     *
     * @param sql 输入SQL语句
     * @return 返回Cursor
     */
    private Cursor queryBase(String sql) {
        Cursor cursor = null;
        try {
            if (db == null)
                openDB(HdAppConfig.getDbFilePath());
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cursor;
    }

    /**
     * 根据自动编号加载资源
     *
     * @param autoNo
     * @return
     */
    public Cursor loadExhibitByAutoNo(int autoNo) {
        String sql = String.format("SELECT * FROM %s WHERE autonum =%d", HdAppConfig.getLanguage(), autoNo);

        return queryBase(sql);
    }

    /*
    * 查询展区
    *
    * */
    public Cursor loadAreaByFloor(int floor) {
        String sql = String.format("SELECT * FROM %s WHERE floor = %d",
                HdAppConfig.getLanguage() + "_Unit", floor);

        return queryBase(sql);
    }

    /*
    * 查询展品编号查询展品
    *
    * */
    public Cursor QueryExhibit(String fileNO) {

        String sql = String.format("SELECT * FROM %s WHERE FileNo = '%s'",
                HdAppConfig.getLanguage(), fileNO);
        Log.i("spl", "---------spl:" + sql);
        return queryBase(sql);
    }


    public Cursor QueryExhibitByFileNO(String exhibit_id) {
        String sql = "SELECT * FROM CHINESE WHERE exhibit_id =" + "'" + exhibit_id + "'";

        return queryBase(sql);
    }

/*
* 根据 exhibit_id 查询临时展品
* */
public Cursor QueryTexhibitByFileNo(String exhibit_id) {
    String sql = "SELECT * FROM lchinse WHERE exhibit_id =" + "'" + exhibit_id + "'";
    Log.i("sql","cursrs: --------------"+sql);
    return queryBase(sql);
}



    /*
* 根据Unit和floor查询展区名
* */
    public Cursor QueryName(int floor, int unitNo) {
        String sql = String.format("SELECT * FROM %s WHERE floor = %d AND  UnitNo= %d",
                HdAppConfig.getLanguage() + "_Unit", floor, unitNo);

        return queryBase(sql);
    }

    /*
       * 查询展品编号查询展品
       *
       * */
    public Cursor QueryByLanguageExhibition(String Language) {
        String sql = "SELECT * FROM " + Language + "_UNIT";
        return queryBase(sql);
    }


    /*
    * 查询展品编号查询展品
    *
    * */
    public Cursor QueryByTableExhibition(String Language) {
        String sql = "SELECT * FROM " + Language;
        return queryBase(sql);
    }
    /*
    * 查询Map表
    *
    * */
    public Cursor QueryMapInfo(int langugage) {
        String sql = String.format("SELECT * FROM %s WHERE language = %d",
                "mapmodel", langugage);
        return queryBase(sql);
    }
    public Cursor QueryByUnitNo(int map_id,int language) {
        String sql = String.format("SELECT * FROM %s WHERE map_id = %d AND  language= %d","mapmodel", map_id, language);
        return queryBase(sql);
    }


    //删除数据库资源
    public static void deleteTable(SQLiteDatabase db, String tableName) {
        db.delete(tableName, null, null);
        db.execSQL("update sqlite_sequence set seq='0' where name='" + tableName + "'");
    }

    public Cursor loadAllExhibit(int map_id) {
        String sql = String.format("SELECT * FROM %s WHERE map_id = %d",
                HdAppConfig.getLanguage(), map_id);

        return queryBase(sql);
    }

//    /**
//     * 根据输入查询数据
//     **/
//    public Cursor LoadExhibitByInput( String input) {
//        String sql = String.format("SELECT * FROM %s WHERE FileNo LIKE '%", HdAppConfig.getLanguage(), input);
//        return queryBase(sql);
//    }

    /**
     * 根据输入查询数据
     **/
    public Cursor search(String CurLang, int floor, String input) {
//        String sql= "SELECT  * FROM " + CurLang + " WHERE Name LIKE '%" + input + "%' OR FileNo LIKE '%" + input + "%'";
        String sql = "SELECT  * FROM " + CurLang + " WHERE Name LIKE '%" + input + "%' OR FileNo LIKE '%" + input + "%' AND Floor = " + floor;
//        String sql= "SELECT  * FROM " + CurLang +" WHERE Floor ="+floor+"AND Name LIKE '%" + input + "%' OR FileNo LIKE '%" + input + "%'";
        return queryBase(sql);
    }

    /**
     * 根据楼层加载点位
     *
     * @param floor
     * @return
     */
    public Cursor loadLocsByFloor(int floor) {
        String sql = String.format("SELECT * FROM %s WHERE Floor = %d",
                HdAppConfig.getLanguage(), floor);
        return queryBase(sql);
    }

    /**
     * 加载地图信息（长、宽、缩放级别）
     *
     * @return
     */
    public Cursor loadMapInfo(int person) {
        String sql = "SELECT * FROM MAP WHERE person=" + person;
        return queryBase(sql);
    }

    //查询AR
    public Cursor QueryAR(String ARID) {
        String sql = "SELECT * FROM ARLIST WHERE ARID=" + ARID;
        return queryBase(sql);
    }
}