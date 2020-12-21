package cn.sunline.batch.controller;


import cn.sunline.batch.pojo.*;
import cn.sunline.batch.service.FileBatchStateLogService;
import cn.sunline.batch.service.FileBatchStateModifyService;
import cn.sunline.batch.service.KappSysdatService;
import cn.sunline.batch.service.KsysJykzhqModifyService;
import cn.sunline.batch.tools.CommTools;
import cn.sunline.batch.tools.InstanceTool;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.beans.Transient;
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
    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;

    @GetMapping("/turnS8")
    @ResponseBody
    public Result turnS8ByPljypich(HttpServletRequest request,String authcode, String datasource, String pljypich,String username) {
        try {
            CommTools.checkParamNotNull("datasource",datasource);
            CommTools.checkParamNotNull("pljypich",pljypich);
            CommTools.checkParamNotNull("username",username);
            FileBatchStateModifyService fileBatchStateModifyService = InstanceTool.getInstance(datasource, FileBatchStateModifyService.class);
            KapbWjxxib oldKapbWjxxib = fileBatchStateModifyService.queryOne(pljypich);
            if (oldKapbWjxxib==null){
                throw new RuntimeException("未找到该批次号对应记录");
            }
            if (oldKapbWjxxib.getPlwenjzt().equals(BaseEnum.S8.name())||oldKapbWjxxib.getPlwenjzt().equals(BaseEnum.D8.name())){
                throw new RuntimeException("不可修改D8 S8状态批量");
            }
            if (!oldKapbWjxxib.getPlwenjzt().startsWith("F")){
                try {
                    CommTools.checkAuthCode(authcode);
                }catch (RuntimeException e){
                    throw new RuntimeException("修改正常状态的批量需要输入认证标识");
                }

            }
            Result result = fileBatchStateModifyService.turnS8ByPljypich(pljypich);
            KapbWjxxib newKapbWjxxib = fileBatchStateModifyService.queryOne(pljypich);
            if (result.getCode().equals(BaseEnum.success.name())){
                CommTools.logInfo(datasource,request.getRemoteAddr(),username,oldKapbWjxxib,newKapbWjxxib,"单条修改批量状态",oldKapbWjxxib.getPlwenjzt()+"->"+newKapbWjxxib.getPlwenjzt());
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
            CommTools.checkParamNotNull("datasource",datasource);
            CommTools.checkParamNotNull("authcode",authcode);
            CommTools.checkParamNotNull("username",username);
            checkAuthCode(authcode);
            FileBatchStateModifyService fileBatchStateModifyService = InstanceTool.getInstance(datasource, FileBatchStateModifyService.class);
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

            if (pljypichList.size()==0){
                throw new RuntimeException("无异常状态批量");
            }

            Result result = fileBatchStateModifyService.turnS8All(pljypichList);

            List<KapbWjxxibSimply> newKapbWjxxibList = fileBatchStateModifyService.queryList(pljypichList);

            if (result.getCode().equals(BaseEnum.success.name())){
                if (JSONArray.toJSONString(oldKapbWjxxibList).length()<3500){
                    CommTools.logInfo(datasource,request.getRemoteAddr(),username,oldKapbWjxxibList,newKapbWjxxibList,"全量修改批量状态",null);
                }else {
                    ArrayList<String> templist = new ArrayList<>();
                    int size=pljypichList.size()/100+1;
                    int count=1;
                    for (int i=0;i<pljypichList.size();i++){
                        templist.add(pljypichList.get(i));
                        if (templist.size()>=100){
                            CommTools.logInfo(datasource,request.getRemoteAddr(),username,templist,templist,"全量修改批量状态",size+"-"+count);
                            count++;
                            templist.clear();
                        }
                    }
                    if (templist.size()!=0){
                        CommTools.logInfo(datasource,request.getRemoteAddr(),username,templist,templist,"全量修改批量状态",size+"-"+count);
                    }
                }
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
            CommTools.checkParamNotNull("authcode",authcode);
            CommTools.checkAdminAuthCode(authcode);
            return new Result(BaseEnum.success.name(),null,CommTools.genAuthCode());
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }

    @GetMapping("/queryByPljyzbsh")
    @ResponseBody
    public Result queryByPljyzbsh(String pljyzbsh,String datasource ) {
        try {
            CommTools.checkParamNotNull("datasource",datasource);
            CommTools.checkParamNotNull("pljyzbsh",pljyzbsh);
            KsysJykzhqModifyService jykzhqModifyService = InstanceTool.getInstance(datasource, KsysJykzhqModifyService.class);
            List<KsysJykzhq> ksysJykzhqs = jykzhqModifyService.queryByPljyzbsh(pljyzbsh);
            ArrayList<Map> list = new ArrayList<>();
            for (KsysJykzhq ksysJykzhq : ksysJykzhqs) {
                HashMap<String, String> map = new HashMap<>();
                map.put("pljioyma",ksysJykzhq.getPljioyma());
                map.put("pljyzwmc",ksysJykzhq.getPljioyma()+"--"+ksysJykzhq.getPljyzwmc());
//                map.put("zhixbzhi",ksysJykzhq.getZhixbzhi());
                list.add(map);
            }
            return new Result(BaseEnum.success.name(),null,list);
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }

    @GetMapping("/queryByPljioyma")
    @ResponseBody
    public Result queryByPljioyma(String pljyzbsh,String datasource,String pljioyma ) {
        try {
            CommTools.checkParamNotNull("datasource",datasource);
            CommTools.checkParamNotNull("pljyzbsh",pljyzbsh);
            CommTools.checkParamNotNull("pljioyma",pljioyma);
            KsysJykzhqModifyService jykzhqModifyService = InstanceTool.getInstance(datasource, KsysJykzhqModifyService.class);
            KsysJykzhq ksysJykzhqs = jykzhqModifyService.queryByPljioyma(pljyzbsh,pljioyma);
            return new Result(BaseEnum.success.name(),null,ksysJykzhqs);
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }



    @GetMapping("/changeRunningState")
    @ResponseBody
    public Result changeRunningState(HttpServletRequest request,String authcode, String username,String datasource,String zhixbzhi,String pljioyma,String pljyzbsh ) {
        try {
            CommTools.checkParamNotNull("datasource",datasource);
            CommTools.checkParamNotNull("zhixbzhi",zhixbzhi);
            CommTools.checkParamNotNull("pljioyma",pljioyma);
            CommTools.checkParamNotNull("username",username);
            CommTools.checkParamNotNull("pljyzbsh",pljyzbsh);
            CommTools.checkAuthCode(authcode);
            KsysJykzhqModifyService ksysJykzhqModifyService = InstanceTool.getInstance(datasource, KsysJykzhqModifyService.class);
            KsysJykzhq oldKsysJykzhq = ksysJykzhqModifyService.queryOne(pljyzbsh,pljioyma);
            if (zhixbzhi.equals(oldKsysJykzhq.getZhixbzhi())){
                throw new RuntimeException("输入执行状态与数据库状态一致，无需修改");
            }
            Result result;
            if ("0".equals(zhixbzhi)){
                result = ksysJykzhqModifyService.changeToStop(pljyzbsh,pljioyma);
            }else if("1".equals(zhixbzhi)){
                result = ksysJykzhqModifyService.changeToRunning(pljyzbsh,pljioyma);
            }else{
                throw new RuntimeException("非法运行标志["+zhixbzhi+"]");
            }
            KsysJykzhq newKsysJykzhq = ksysJykzhqModifyService.queryOne(pljyzbsh,pljioyma);

            if (result.getCode().equals(BaseEnum.success.name())){
                CommTools.logInfo(datasource,request.getRemoteAddr(),username,oldKsysJykzhq,newKsysJykzhq,"修改批量交易运行状态",oldKsysJykzhq.getZhixbzhi()+"->"+newKsysJykzhq.getZhixbzhi());
            }
            return result;
        }  catch (Exception e) {
            return new Result(BaseEnum.fail.name(),e.getMessage(),null);
        }
    }

    @GetMapping("/queryDate")
    @ResponseBody
    public Result queryDate(String datasource) {
        try {
            CommTools.checkParamNotNull("datasource",datasource);
            Class<KappSysdatService> aClass = KappSysdatService.class;
            DS ds = aClass.getDeclaredAnnotation(DS.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(ds);
            Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
            memberValues.setAccessible(true);
            Map map = (Map)memberValues.get(invocationHandler);
            map.put("value",datasource);
            KappSysdatService kappSysdatService = applicationContext.getBean(aClass);
            KappSysdat kappSysdat = kappSysdatService.query();
            return new Result(BaseEnum.success.name(),"查询成功",kappSysdat);
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

}
