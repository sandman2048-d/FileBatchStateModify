package cn.sunline.batch.service;

import cn.sunline.batch.mapper.DynamicMapper;
import cn.sunline.batch.pojo.*;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:29 2020/11/17
 */
@Service
public class DynamicExcuteSerivce {
    @Resource
    private FileBatchStateLogService fileBatchStateLogService;

    @Resource
    private DynamicSerivce dynamicSerivce;
    public Result excute(int datasource,int sqlid, Map<String,String> paramMap,String username,String ip) throws SQLException {
        Result result = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            KsysZdysql ksysZdysql = dynamicSerivce.querySqlById(sqlid);
            KsysDatasource ksysDatasource = dynamicSerivce.queryDataSourceById(datasource);

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://"+ksysDatasource.getUrl()+":"+ksysDatasource.getPort()+"/"+ksysDatasource.getDcnno()+"?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", ksysDatasource.getUsername(), ksysDatasource.getPassword());

            preparedStatement = connection.prepareStatement(ksysZdysql.getSql());
            String sqlparam = ksysZdysql.getSqlparam();
            if (sqlparam!=null&&!"".equals(sqlparam.trim())){
                String[] params = sqlparam.split(",");
                if (paramMap==null||paramMap.size()==0||params.length!=paramMap.size()){
                    throw new RuntimeException("输入参数与sql不符，请重新提交");
                }
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setString(i+1,paramMap.get(params[i]));
                }
            }


            if (BaseEnum.select.name().equals(ksysZdysql.getType())){
                ResultSet resultSet = preparedStatement.executeQuery();
                result = new Result(BaseEnum.success.name(), "查询成功",resultSet);
            }else if (BaseEnum.update.name().equals(ksysZdysql.getType())||BaseEnum.delete.name().equals(ksysZdysql.getType())||BaseEnum.insert.name().equals(ksysZdysql.getType())) {
                int i = preparedStatement.executeUpdate();
                if (i > 0) {
                    result = new Result(BaseEnum.success.name(), ksysZdysql.getType() + "修改成功", null);
                    KapbPlztxgrz kapbPlztxgrz = new KapbPlztxgrz();
                    kapbPlztxgrz.setEnvironment(ksysDatasource.getEnv()+"-"+ksysDatasource.getDcnno());
                    kapbPlztxgrz.setJob("自定义运行sql");
                    kapbPlztxgrz.setDate(new Timestamp(new Date().getTime()));
                    kapbPlztxgrz.setOldData(ksysZdysql.getSqlname()+"->"+ksysZdysql.getSql()+"->"+paramMap);
                    kapbPlztxgrz.setNewData("修改数量:"+String.valueOf(i));
                    kapbPlztxgrz.setUser(username);
                    kapbPlztxgrz.setIp(ip);
                    fileBatchStateLogService.addLog(kapbPlztxgrz);
                } else {
                    result = new Result(BaseEnum.fail.name(), ksysZdysql.getType() + "失败，请检查sql或数据", null);

                }
            }
        } catch (Exception e) {
            result=new Result(BaseEnum.fail.name(), e.getMessage(),null);
        }finally {
            if (connection!=null){
                connection.close();
            }
            if (preparedStatement!=null){
                preparedStatement.close();
            }
        }
        return result;
    }

}
