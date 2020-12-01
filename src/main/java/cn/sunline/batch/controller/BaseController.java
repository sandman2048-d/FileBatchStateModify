package cn.sunline.batch.controller;


import cn.sunline.batch.pojo.*;
import cn.sunline.batch.service.FileBatchStateLogService;
import cn.sunline.batch.service.FileBatchStateModifyService;
import cn.sunline.batch.service.KappJioyxxModifyService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:49 2020/11/17
 */
@CrossOrigin
@Controller
@RequestMapping("/base")
public class BaseController {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private FileBatchStateLogService fileBatchStateLogService;


    /*@GetMapping("/query")
    @ResponseBody
    public Result query(HttpServletRequest request, String pljypich, String picihaoo, BaseEnum plwenjzht, String datasource) throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
        KapbWjxxib wjxxib=null;
        try {
            Class<FileBatchStateQueryService> aClass = FileBatchStateQueryService.class;
            DS ds = aClass.getDeclaredAnnotation(DS.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(ds);
            Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
            memberValues.setAccessible(true);
            Map map = (Map)memberValues.get(invocationHandler);
            map.put("value",datasource);
            FileBatchStateQueryService fileBatchStateQuery = applicationContext.getBean(aClass);
            wjxxib = fileBatchStateQuery.query(pljypich, picihaoo, plwenjzht);
        }  catch (Exception e) {
            return new Result("fail",e.getMessage(),null);
        }
        ArrayList<KapbWjxxib> kapbWjxxibArrayList = new ArrayList<>();
        kapbWjxxibArrayList.add(wjxxib);
        return new Result(BaseEnum.success.name(),"",kapbWjxxibArrayList);
    }*/

    @GetMapping("/turnS8")
    @ResponseBody
    public Result turnS8ByPljypich(HttpServletRequest request, String datasource, String pljypich,String username) {
        try {
            checkParamNotNull("datasource",datasource);
            checkParamNotNull("pljypich",pljypich);
            checkParamNotNull("username",username);
            Class<FileBatchStateModifyService> aClass = FileBatchStateModifyService.class;
            DS ds = aClass.getDeclaredAnnotation(DS.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(ds);
            Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
            memberValues.setAccessible(true);
            Map map = (Map)memberValues.get(invocationHandler);
            map.put("value",datasource);
            FileBatchStateModifyService fileBatchStateModifyService = applicationContext.getBean(aClass);
            KapbWjxxib oldKapbWjxxib = fileBatchStateModifyService.queryOne(pljypich);
            if (oldKapbWjxxib==null){
                throw new RuntimeException("未找到该批次号对应记录");
            }
            /*if (oldKapbWjxxib.getPlwenjzt().equals(BaseEnum.S8.name())||oldKapbWjxxib.getPlwenjzt().equals(BaseEnum.D8.name())){
                throw new RuntimeException("只能修改非D8 S8状态的批量状态");
            }*/
            if (!oldKapbWjxxib.getPlwenjzt().startsWith("F")){
                throw new RuntimeException("只能修改异常的批量状态");
            }
            Result result = fileBatchStateModifyService.turnS8ByPljypich(pljypich);
            KapbWjxxib newKapbWjxxib = fileBatchStateModifyService.queryOne(pljypich);
            if (result.getCode().equals(BaseEnum.success.name())){
                KapbPlztxgrz kapbPlztxgrz = new KapbPlztxgrz();
                kapbPlztxgrz.setDate(new Timestamp((new Date()).getTime()));
                kapbPlztxgrz.setEnvironment(datasource);
                kapbPlztxgrz.setJob("单条修改批量状态");
                kapbPlztxgrz.setIp(request.getRemoteAddr());
                kapbPlztxgrz.setUser(username);
                kapbPlztxgrz.setOldData(JSONArray.toJSONString(oldKapbWjxxib));
                kapbPlztxgrz.setNewData(JSONArray.toJSONString(newKapbWjxxib));
                fileBatchStateLogService.addLog(kapbPlztxgrz);
            }
            return result;
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }

    @GetMapping("/turnS8All")
    @ResponseBody
    public Result turnS8All(HttpServletRequest request, String datasource, String authcode,String username) {
        try {
            checkParamNotNull("datasource",datasource);
            checkParamNotNull("authcode",authcode);
            checkParamNotNull("username",username);
            checkAuthCode(authcode);
            Class<FileBatchStateModifyService> aClass = FileBatchStateModifyService.class;
            DS ds = aClass.getDeclaredAnnotation(DS.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(ds);
            Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
            memberValues.setAccessible(true);
            Map map = (Map)memberValues.get(invocationHandler);
            map.put("value",datasource);
            FileBatchStateModifyService fileBatchStateModifyService = applicationContext.getBean(aClass);
            List<KapbWjxxibSimply> oldKapbWjxxibList = fileBatchStateModifyService.queryAll();

            if (oldKapbWjxxibList==null||oldKapbWjxxibList.size()==0){
                throw new RuntimeException("无异常状态批量");
            }

            ArrayList<String> pljypichList = new ArrayList<>();
            for (KapbWjxxibSimply kapbWjxxib : oldKapbWjxxibList) {
                if (kapbWjxxib.getPlwenjzt().startsWith("F")){
                    pljypichList.add(kapbWjxxib.getPljypich());
                }
            }

            Result result = fileBatchStateModifyService.turnS8All(pljypichList);

            List<KapbWjxxibSimply> newKapbWjxxibList = fileBatchStateModifyService.queryList(pljypichList);

            if (result.getCode().equals(BaseEnum.success.name())){
                KapbPlztxgrz kapbPlztxgrz = new KapbPlztxgrz();
                kapbPlztxgrz.setDate(new Timestamp((new Date()).getTime()));
                kapbPlztxgrz.setEnvironment(datasource);
                kapbPlztxgrz.setIp(request.getRemoteAddr());
                kapbPlztxgrz.setUser(username);
                kapbPlztxgrz.setJob("全量修改批量状态");
                if (JSONArray.toJSONString(oldKapbWjxxibList).length()>3500){
                    kapbPlztxgrz.setOldData(JSONArray.toJSONString(pljypichList));
                    kapbPlztxgrz.setNewData(JSONArray.toJSONString(pljypichList));
                }else{
                    kapbPlztxgrz.setOldData(JSONArray.toJSONString(oldKapbWjxxibList));
                    kapbPlztxgrz.setNewData(JSONArray.toJSONString(newKapbWjxxibList));
                }
                fileBatchStateLogService.addLog(kapbPlztxgrz);
                result.setData(pljypichList);
            }
            return result;
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }

    @GetMapping("/getTempAuthCode")
    @ResponseBody
    public Result getTempAuthCode(String authcode ) {
        try {
            checkParamNotNull("authcode",authcode);
            if (!authcode.equals("rlcskf")){
                throw new RuntimeException("认证标识错误");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String date = sdf.format(new Date());
            String tempAuthCode = String.valueOf((Integer.valueOf(date))/((Integer.valueOf(date.substring(4,6)))+Integer.valueOf((date.substring(6)))));
            return new Result(BaseEnum.success.name(),null,tempAuthCode);
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }

    @GetMapping("/changeRunningState")
    @ResponseBody
    public Result changeRunningState(HttpServletRequest request, String username,String datasource,String yunxzxbz,String jiaoyima ) {
        try {
            checkParamNotNull("datasource",datasource);
            checkParamNotNull("yunxzxbz",yunxzxbz);
            checkParamNotNull("jiaoyima",jiaoyima);
            Class<KappJioyxxModifyService> aClass = KappJioyxxModifyService.class;
            DS ds = aClass.getDeclaredAnnotation(DS.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(ds);
            Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
            memberValues.setAccessible(true);
            Map map = (Map)memberValues.get(invocationHandler);
            map.put("value",datasource);
            KappJioyxxModifyService kappJioyxxModifyService = applicationContext.getBean(aClass);
            KappJioyxx oldKappJioyxx = kappJioyxxModifyService.queryOne(jiaoyima);
            if (yunxzxbz.equals(oldKappJioyxx.getYunxzxbz())){
                throw new RuntimeException("输入执行状态与数据库状态一直，无需修改");
            }
            Result result;
            if ("0".equals(yunxzxbz)){
                result = kappJioyxxModifyService.changeToStop(jiaoyima);
            }else if("1".equals(yunxzxbz)){
                result = kappJioyxxModifyService.changeToRunning(jiaoyima);
            }else{
                throw new RuntimeException("非法运行标志["+yunxzxbz+"]");
            }
            KappJioyxx newKappJioyxx = kappJioyxxModifyService.queryOne(jiaoyima);

            if (result.getCode().equals(BaseEnum.success.name())){
                KapbPlztxgrz kapbPlztxgrz = new KapbPlztxgrz();
                kapbPlztxgrz.setDate(new Timestamp((new Date()).getTime()));
                kapbPlztxgrz.setJob("修改交易执行状态");
                kapbPlztxgrz.setEnvironment(datasource);
                kapbPlztxgrz.setIp(request.getRemoteAddr());
                kapbPlztxgrz.setUser(username);
                kapbPlztxgrz.setOldData(JSONArray.toJSONString(oldKappJioyxx));
                kapbPlztxgrz.setNewData(JSONArray.toJSONString(newKappJioyxx));
                fileBatchStateLogService.addLog(kapbPlztxgrz);
            }
            return result;
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }
    @GetMapping("/queryByJiaoyima")
    @ResponseBody
    public Result queryByJiaoyima(HttpServletRequest request, String username,String datasource,String jiaoyima ) {
        try {
            checkParamNotNull("datasource",datasource);
            checkParamNotNull("jiaoyima",jiaoyima);
            Class<KappJioyxxModifyService> aClass = KappJioyxxModifyService.class;
            DS ds = aClass.getDeclaredAnnotation(DS.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(ds);
            Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
            memberValues.setAccessible(true);
            Map map = (Map)memberValues.get(invocationHandler);
            map.put("value",datasource);
            KappJioyxxModifyService kappJioyxxModifyService = applicationContext.getBean(aClass);
            KappJioyxx kappJioyxx = kappJioyxxModifyService.queryOne(jiaoyima);
            return new Result(BaseEnum.success.name(),"查询成功",kappJioyxx);
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }



    private void checkAuthCode(String authcode) {
        if (authcode.equals("rlcskf")){
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        String dateAuth = String.valueOf((Integer.valueOf(date))/((Integer.valueOf(date.substring(4,6)))+Integer.valueOf((date.substring(6)))));
        if (dateAuth.equals(authcode)){
            return;
        }
        throw new RuntimeException("认证标识错误");
    }

    private void checkParamNotNull(String paramName,String param){
        if(param==null||"".equals(param.trim())){
            throw new RuntimeException("["+paramName+"]不得为空，请重新输入");
        }
    }
}
