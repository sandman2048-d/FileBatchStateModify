package cn.sunline.batch.controller;


import cn.sunline.batch.pojo.*;
import cn.sunline.batch.service.*;
import cn.sunline.batch.tools.CommTools;
import cn.sunline.batch.tools.InstanceTool;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:49 2020/11/17
 */
@CrossOrigin
@Controller
@RequestMapping("/dynamic")
public class DynamicController {
    @Resource
    private DynamicSerivce dynamicSerivce;
    @Resource
    private DynamicExcuteSerivce dynamicExcuteSerivce;

    @GetMapping("/queryDatasource")
    @ResponseBody
    public Result queryDatasource() {
        try {
            List<KsysDatasource> ksysDatasources = dynamicSerivce.queryDataSource();
            ArrayList<Map<String, String>> list = new ArrayList<>();
            for (KsysDatasource ksysDatasource : ksysDatasources) {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",String.valueOf(ksysDatasource.getId()));
                map.put("env",ksysDatasource.getEnv());
                map.put("dcnno",ksysDatasource.getDcnno());
                list.add(map);
            }
            return new Result(BaseEnum.success.name(),"查询成功",list);
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }

    @GetMapping("/querySql")
    @ResponseBody
    public Result querySql() {
        try {
            List<KsysZdysql> ksysZdysqls = dynamicSerivce.querySql();
            ArrayList<Map<String, Object>> list = new ArrayList<>();
            for (KsysZdysql ksysZdysql : ksysZdysqls) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("sqlid",String.valueOf(ksysZdysql.getSqlid()));
                map.put("sqlname",ksysZdysql.getSqlname());
                map.put("sql",ksysZdysql.getSql());
                if (ksysZdysql.getSqlparam()!=null&&!"".equals(ksysZdysql.getSqlparam().trim())){
                    ArrayList<String> paramList = new ArrayList<>();
                    String[] split = ksysZdysql.getSqlparam().split(",");
                    for (String s : split) {
                        paramList.add(s);
                    }
                    map.put("param",paramList);
                }

                list.add(map);
            }
            return new Result(BaseEnum.success.name(),"查询成功",list);
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }

    @PostMapping("/excuteSql")
    @ResponseBody
    public Result querySql(HttpServletRequest request,String datasource,String sqlid, String paramMap,String authcode,String username) {
        try {
            CommTools.checkParamNotNull("username",username);
            CommTools.checkAuthCode(authcode);
            Map map = (Map<String,String>)JSONArray.parse(paramMap);
            CommTools.checkParamNotNull("datasource",datasource);
            CommTools.checkParamNotNull("sqlid",sqlid);
            Result result = dynamicExcuteSerivce.excute(Integer.valueOf(datasource), Integer.valueOf(sqlid), map,username,request.getRemoteAddr());
            return result;
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }
}
