package cn.sunline.batch.tools;

import cn.sunline.batch.pojo.KapbPlztxgrz;
import cn.sunline.batch.service.FileBatchStateLogService;
import com.alibaba.fastjson.JSONArray;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommTools {
    private static FileBatchStateLogService fileBatchStateLogService;
    public static void checkAuthCode(String authcode) {
        if (authcode.equals("rlcskf")){
            return;
        }
        String dateAuth = genAuthCode();
        if (dateAuth.equals(authcode)){
            return;
        }
        throw new RuntimeException("认证标识错误");
    }

    public static void checkAdminAuthCode(String authcode) {
        if (authcode.equals("rlcskf")){
            return;
        }
        throw new RuntimeException("认证标识错误");
    }

    public static void checkParamNotNull(String paramName,String param){
        if(param==null||"".equals(param.trim())){
            throw new RuntimeException("["+paramName+"]不得为空，请重新输入");
        }
    }

    public static String genAuthCode(){
        //认证标识生成规则：yyyymmdd/(mm+dd)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        String dateAuth = String.valueOf((Integer.valueOf(date))/((Integer.valueOf(date.substring(4,6)))+Integer.valueOf((date.substring(6)))));
        return dateAuth;
    }

    public static void logInfo(String datasource,String ip,String username,Object oldData,Object newData,String job,String comments) throws NoSuchFieldException, IllegalAccessException {
        if (fileBatchStateLogService==null){
            fileBatchStateLogService = InstanceTool.getInstance("dev",FileBatchStateLogService.class);
        }
        KapbPlztxgrz kapbPlztxgrz = new KapbPlztxgrz();
        kapbPlztxgrz.setDate(new Timestamp((new Date()).getTime()));
        kapbPlztxgrz.setJob(job);
        kapbPlztxgrz.setComments(comments);
        kapbPlztxgrz.setEnvironment(datasource);
        kapbPlztxgrz.setIp(ip);
        kapbPlztxgrz.setUser(username);
        kapbPlztxgrz.setOldData(JSONArray.toJSONString(oldData));
        kapbPlztxgrz.setNewData(JSONArray.toJSONString(newData));
        fileBatchStateLogService.addLog(kapbPlztxgrz);
    }
}
