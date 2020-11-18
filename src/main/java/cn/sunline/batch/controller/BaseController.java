package cn.sunline.batch.controller;


import cn.sunline.batch.pojo.BaseEnum;
import cn.sunline.batch.pojo.KapbPlztxgrz;
import cn.sunline.batch.pojo.KapbWjxxib;
import cn.sunline.batch.pojo.Result;
import cn.sunline.batch.service.FileBatchStateLogService;
import cn.sunline.batch.service.FileBatchStateModifyService;
import cn.sunline.batch.service.FileBatchStateQueryService;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:49 2020/11/17
 */
@Controller
@RequestMapping("/base")
public class BaseController {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private FileBatchStateLogService fileBatchStateLogService;
    @GetMapping("/query")
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
    }

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
            if (oldKapbWjxxib.getPlwenjzt().equals(BaseEnum.S8.name())||oldKapbWjxxib.getPlwenjzt().equals(BaseEnum.D8.name())){
                throw new RuntimeException("只能修改非D8 S8状态的批量状态");
            }
            Result result = fileBatchStateModifyService.turnS8ByPljypich(pljypich);
            KapbWjxxib newKapbWjxxib = fileBatchStateModifyService.queryOne(pljypich);
            if (result.getCode().equals(BaseEnum.success.name())){
                KapbPlztxgrz kapbPlztxgrz = new KapbPlztxgrz();
                kapbPlztxgrz.setDate(new Timestamp((new Date()).getTime()));
                kapbPlztxgrz.setEnvironment(datasource);
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

    private void checkParamNotNull(String paramName,String param){
        if(param==null||"".equals(param.trim())){
            throw new RuntimeException("["+paramName+"]不得为空，请重新输入");
        }
    }
}
