package cn.sunline.batch.mapper;

import cn.sunline.batch.pojo.KappSysdat;
import cn.sunline.batch.pojo.KsysJykzhq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:33 2020/11/17
 */
@Mapper
public interface SysDatMapper {

    @Select("select * from kapp_sysdat limit 1")
    KappSysdat query();

}
