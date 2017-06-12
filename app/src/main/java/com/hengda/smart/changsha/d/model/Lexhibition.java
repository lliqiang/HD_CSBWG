package com.hengda.smart.changsha.d.model;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by lenovo on 2017/5/13.
 */

public class Lexhibition extends DataSupport {

    /**
     * autonum : 1
     * exhibit_id : 0001
     * byname : 123123
     */

    private List<Lchinse> CHINESE;
    /**
     * autonum : 1
     * exhibit_id : 0001
     * byname : 234234
     */

    private List<Lenglish> ENGLISH;
    /**
     * autonum : 1
     * exhibit_id : 0001
     * byname : 345234
     */

    private List<Ljapanese> JAPANESE;
    /**
     * autonum : 1
     * exhibit_id : 0001
     * byname : 32423234
     */

    private List<Lkorean> KOREAN;

    public List<Lchinse> getCHINESE() {
        return CHINESE;
    }

    public void setCHINESE(List<Lchinse> CHINESE) {
        this.CHINESE = CHINESE;
    }

    public List<Lenglish> getENGLISH() {
        return ENGLISH;
    }

    public void setENGLISH(List<Lenglish> ENGLISH) {
        this.ENGLISH = ENGLISH;
    }

    public List<Ljapanese> getJAPANESE() {
        return JAPANESE;
    }

    public void setJAPANESE(List<Ljapanese> JAPANESE) {
        this.JAPANESE = JAPANESE;
    }

    public List<Lkorean> getKOREAN() {
        return KOREAN;
    }

    public void setKOREAN(List<Lkorean> KOREAN) {
        this.KOREAN = KOREAN;
    }


}
