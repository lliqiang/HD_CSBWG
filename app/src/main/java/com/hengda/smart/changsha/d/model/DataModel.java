package com.hengda.smart.changsha.d.model;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by lenovo on 2017/3/10.
 */

public class DataModel extends DataSupport {
    private ExhibitInfoBean exhibit_info;
    private TeExhibitInfoBean te_exhibit_info;

    public ExhibitInfoBean getExhibit_info() {
        return exhibit_info;
    }

    public void setExhibit_info(ExhibitInfoBean exhibit_info) {
        this.exhibit_info = exhibit_info;
    }

    public TeExhibitInfoBean getTe_exhibit_info() {
        return te_exhibit_info;
    }

    public void setTe_exhibit_info(TeExhibitInfoBean te_exhibit_info) {
        this.te_exhibit_info = te_exhibit_info;
    }

    public static class ExhibitInfoBean {
        /**
         * autonum : 0101
         * exhibit_id : 0101
         * filename : CHINESE/0101
         * byname : 旧石器一组
         * subheading : 摆在我们眼前的这三件石器是浏阳出土的旧石器。不得不承认，假如这三件石器只是随随便
         * map_id : 1
         * img_num : 1
         * axis_x : 566
         * axis_y : 746
         */

        private List<Chinese> CHINESE;
        /**
         * autonum : 0101
         * exhibit_id : 0101
         * filename : ENGLISH/0101
         * byname : Stone Implements
         * subheading : These three stone implements placed in f
         * map_id : 1
         * img_num : 1
         * axis_x : 566
         * axis_y : 746
         */

        private List<English> ENGLISH;
        /**
         * autonum : 0101
         * exhibit_id : 0101
         * filename : JAPANESE/0101
         * byname : 旧石器セット
         * subheading : ご覧になる三件の石器は瀏陽から出土された旧石器です。どこかに放置されたら誰の目も
         * map_id : 1
         * img_num : 1
         * axis_x : 566
         * axis_y : 746
         */

        private List<Japanese> JAPANESE;
        /**
         * autonum : 0101
         * exhibit_id : 0101
         * filename : KOREAN/0101
         * byname : 구석기 한 세트
         * subheading : 여러분 눈 앞에 전시되어 있는 이 3개의 석기는 류양(浏阳)에서 출토한
         * map_id : 1
         * img_num : 1
         * axis_x : 566
         * axis_y : 746
         */

        private List<Korean> KOREAN;
        /**
         * name : 一展厅
         * img : http://192.168.10.20/csbwg/data/upload/project/20170419/58f6c4b103cf7.png
         * map_id : 1
         * language : 1
         */

        private List<MapModel> map_info;

        public List<Chinese> getCHINESE() {
            return CHINESE;
        }

        public void setCHINESE(List<Chinese> CHINESE) {
            this.CHINESE = CHINESE;
        }

        public List<English> getENGLISH() {
            return ENGLISH;
        }

        public void setENGLISH(List<English> ENGLISH) {
            this.ENGLISH = ENGLISH;
        }

        public List<Japanese> getJAPANESE() {
            return JAPANESE;
        }

        public void setJAPANESE(List<Japanese> JAPANESE) {
            this.JAPANESE = JAPANESE;
        }

        public List<Korean> getKOREAN() {
            return KOREAN;
        }

        public void setKOREAN(List<Korean> KOREAN) {
            this.KOREAN = KOREAN;
        }

        public List<MapModel> getMap_info() {
            return map_info;
        }

        public void setMap_info(List<MapModel> map_info) {
            this.map_info = map_info;
        }



        public static class MapInfoBean {
            private String name;
            private String img;
            private String map_id;
            private String language;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getMap_id() {
                return map_id;
            }

            public void setMap_id(String map_id) {
                this.map_id = map_id;
            }

            public String getLanguage() {
                return language;
            }

            public void setLanguage(String language) {
                this.language = language;
            }
        }
    }

    public static class TeExhibitInfoBean {
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


//
//    private ExhibitInfoBean exhibit_info;
//
//    public ExhibitInfoBean getExhibit_info() {
//        return exhibit_info;
//    }
//
//    public void setExhibit_info(ExhibitInfoBean exhibit_info) {
//        this.exhibit_info = exhibit_info;
//    }
//
//    public static class ExhibitInfoBean {
//        private List<Chinese> CHINESE;
//        private List<English> ENGLISH;
//        private List<Japanese> JAPANESE;
//        private List<Korean> KOREAN;
//        private List<MapModel> map_info;
//
//        public List<MapModel> getMap_info() {
//            return map_info;
//        }
//
//        public void setMap_info(List<MapModel> map_info) {
//            this.map_info = map_info;
//        }
//
//        public List<Chinese> getCHINESE() {
//            return CHINESE;
//        }
//
//        public void setCHINESE(List<Chinese> CHINESE) {
//            this.CHINESE = CHINESE;
//        }
//
//        public List<English> getENGLISH() {
//            return ENGLISH;
//        }
//
//        public void setENGLISH(List<English> ENGLISH) {
//            this.ENGLISH = ENGLISH;
//        }
//
//        public List<Japanese> getJAPANESE() {
//            return JAPANESE;
//        }
//
//        public void setJAPANESE(List<Japanese> JAPANESE) {
//            this.JAPANESE = JAPANESE;
//        }
//
//        public List<Korean> getKOREAN() {
//            return KOREAN;
//        }
//
//        public void setKOREAN(List<Korean> KOREAN) {
//            this.KOREAN = KOREAN;
//        }
//    }

}

