package cn.sunline.batch;

import cn.sunline.batch.pojo.KapbWjxxib;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class test {

    public static void main(String[] args) {
        String s = String.valueOf(new BigDecimal(0));
        String s1 = JSONArray.toJSONString(0.00000000);
        System.out.println(s);
        System.out.println(s1);
    }
}
