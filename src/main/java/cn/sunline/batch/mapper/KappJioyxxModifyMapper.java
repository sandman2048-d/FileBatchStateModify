package cn.sunline.batch.mapper;

import cn.sunline.batch.pojo.KapbWjxxib;
import cn.sunline.batch.pojo.KapbWjxxibSimply;
import cn.sunline.batch.pojo.KappJioyxx;
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
public interface KappJioyxxModifyMapper {


    @Update("update kapp_jioyxx set yunxzxbz = '1' where jiaoyima = #{jiaoyima}")
    int changeToRunning(String jiaoyima);

    @Update("update kapp_jioyxx set yunxzxbz = '0' where jiaoyima = #{jiaoyima}")
    int changeToStop(String jiaoyima);

    @Select("select * from kapp_jioyxx where jiaoyima=#{jiaoyima}")
    KappJioyxx query(String jiaoyima);

}
