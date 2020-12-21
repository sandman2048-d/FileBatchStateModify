package cn.sunline.batch.service;

import cn.sunline.batch.mapper.DynamicMapper;
import cn.sunline.batch.mapper.SysDatMapper;
import cn.sunline.batch.pojo.KappSysdat;
import cn.sunline.batch.pojo.KsysDatasource;
import cn.sunline.batch.pojo.KsysZdysql;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:29 2020/11/17
 */
@Service
@DS("dev")
public class DynamicSerivce {
    @Resource
    private DynamicMapper dynamicMapper;

    public List<KsysDatasource> queryDataSource() {
        List<KsysDatasource> ksysDatasources = dynamicMapper.queryAllDatasource();
        if (ksysDatasources==null||ksysDatasources.size()==0){
            throw new RuntimeException("未查询到对应记录");
        }
        return ksysDatasources;
    }

    public List<KsysZdysql> querySql() {
        List<KsysZdysql> ksysZdysqls = dynamicMapper.querySql();
        if (ksysZdysqls==null||ksysZdysqls.size()==0){
            throw new RuntimeException("未查询到对应记录");
        }
        return ksysZdysqls;
    }

    public KsysZdysql querySqlById(int sqlid) {
        return dynamicMapper.querySqlById(sqlid);
    }

    public KsysDatasource queryDataSourceById(int datasource) {
        return dynamicMapper.queryDataSourceById(datasource);
    }
}
