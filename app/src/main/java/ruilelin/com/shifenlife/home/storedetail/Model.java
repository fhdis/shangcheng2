package ruilelin.com.shifenlife.home.storedetail;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private int cId;
    private String cName;
    private List<SubModel> subModelList;


    private int badge;

    public Model(int cId, String cName, List<SubModel> subModelList) {
        this.cId = cId;
        this.cName = cName;
        this.subModelList = subModelList;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public List<SubModel> getSubModelList() {
        return subModelList;
    }

    public void setSubModelList(List<SubModel> subModelList) {
        this.subModelList = subModelList;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public static class SubModel {

        /*****tag*****/
        private int cId;
        private String cName;
        /*************/

        private int num;

        private String name;
        private int age;

        public int getcId() {
            return cId;
        }

        public void setcId(int cId) {
            this.cId = cId;
        }

        public String getcName() {
            return cName;
        }

        public void setcName(String cName) {
            this.cName = cName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public SubModel(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }


    public static List<Model> initData(){

        List<Model> list = new ArrayList<>();

        List<SubModel> men = new ArrayList<>();
        men.add(new SubModel("榴莲",20));
        men.add(new SubModel("榴莲",18));
        men.add(new SubModel("榴莲",22));
        men.add(new SubModel("榴莲",60));
        men.add(new SubModel("榴莲",20));
        men.add(new SubModel("榴莲",10));
        men.add(new SubModel("榴莲",23));
        men.add(new SubModel("榴莲",25));
        men.add(new SubModel("榴莲",14));
        men.add(new SubModel("榴莲",17));
        men.add(new SubModel("榴莲",60));
        men.add(new SubModel("榴莲",24));

        List<SubModel> women = new ArrayList<>();
        women.add(new SubModel("芒果",10));
        women.add(new SubModel("芒果",18));
        women.add(new SubModel("芒果",21));
        women.add(new SubModel("芒果",21));
        women.add(new SubModel("芒果",11));
        women.add(new SubModel("芒果",18));
        women.add(new SubModel("芒果",58));

        List<SubModel> xiangjiao = new ArrayList<>();
        xiangjiao.add(new SubModel("香蕉",10));
        xiangjiao.add(new SubModel("香蕉",18));
        xiangjiao.add(new SubModel("香蕉",21));
        xiangjiao.add(new SubModel("香蕉",21));
        xiangjiao.add(new SubModel("香蕉",11));
        xiangjiao.add(new SubModel("香蕉",18));
        xiangjiao.add(new SubModel("香蕉",58));


        list.add(new Model(1,"新鲜水果",women));
        list.add(new Model(2,"田园时蔬",men));
        list.add(new Model(3,"禽蛋肉类",xiangjiao));
      //  list.add(new Model(4,"水产海鲜",men));
      //  list.add(new Model(5,"净菜熟食",women));
      //  list.add(new Model(6,"豆蜡加工",women));
      //  list.add(new Model(7,"粮油副食",men));
      //  list.add(new Model(8,"调味百货",women));

        return list;
    }
}

