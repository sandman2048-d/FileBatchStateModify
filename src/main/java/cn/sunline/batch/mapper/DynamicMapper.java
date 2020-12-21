package cn.sunline.batch.mapper;

import cn.sunline.batch.pojo.KappSysdat;
import cn.sunline.batch.pojo.KsysDatasource;
import cn.sunline.batch.pojo.KsysZdysql;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:33 2020/11/17
 */
@Mapper
public interface DynamicMapper {

    @Select("select * from ksys_datasource")
    List<KsysDatasource> queryAllDatasource();

    @Select("select * from ksys_zdysql")
    List<KsysZdysql> querySql();
    @Select("select * from ksys_zdysql where sqlid=#{sqlid}")
    KsysZdysql querySqlById(int sqlid);
    @Select("select * from ksys_datasource where id=#{datasource}")
    KsysDatasource queryDataSourceById(int datasource);
}
