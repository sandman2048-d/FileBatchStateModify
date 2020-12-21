package cn.sunline.batch.tools;

import cn.sunline.batch.service.KsysJykzhqModifyService;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

public class InstanceTool {
    @Resource
    public static ApplicationContext applicationContext;
    public static <T> T getInstance(String datasource , Class<T> aClass) throws NoSuchFieldException, IllegalAccessException {
        DS ds = aClass.getDeclaredAnnotation(DS.class);
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(ds);
        Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
        memberValues.setAccessible(true);
        Map map = (Map)memberValues.get(invocationHandler);
        map.put("value",datasource);
        T bean = applicationContext.getBean(aClass);
        return bean;
    }
}
