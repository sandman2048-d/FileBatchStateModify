package cn.sunline.batch.mapper;

import cn.sunline.batch.pojo.KapbWjxxib;
import cn.sunline.batch.pojo.KapbWjxxibSimply;
import cn.sunline.batch.pojo.KappJioyxx;
import cn.sunline.batch.pojo.KsysJykzhq;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:33 2020/11/17
 */
@Mapper
public interface KsysJykzhqModifyMapper {


    @Update("update ksys_jykzhq set zhixbzhi = '1' where pljioyma = #{jiaoyima} and pljyzbsh = #{pljyzbsh}")
    int changeToRunning(String pljyzbsh,String jiaoyima);

    @Update("update ksys_jykzhq set zhixbzhi = '0' where pljioyma = #{jiaoyima} and pljyzbsh = #{pljyzbsh}")
    int changeToStop(String pljyzbsh,String jiaoyima);

    @Select("select * from ksys_jykzhq where pljioyma=#{pljioyma} and pljyzbsh = #{pljyzbsh}")
    KsysJykzhq queryOne(String pljioyma,String pljyzbsh);

    @Select("select * from ksys_jykzhq where pljyzbsh=#{pljyzbsh}")
    List<KsysJykzhq> queryByPljyzbsh(String pljyzbsh);


}
