package cn.sunline.batch.mapper;

import cn.sunline.batch.pojo.BaseEnum;
import cn.sunline.batch.pojo.KapbPlztxgrz;
import cn.sunline.batch.pojo.KapbWjxxib;
import cn.sunline.batch.pojo.KapbWjxxibSimply;
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
public interface FileBatchStateModifyMapper {
    @Select("select * from kapb_wjxxib where pljypich=#{pljypich}")
    KapbWjxxib query(String pljypich);

    @Update("update kapb_wjxxib set plwenjzt = 'S8' where pljypich = #{pljypich}")
    int turnS8ByPljypich(String pljypich);

    @Insert("insert into kapb_plztxgrz (user,ip,old_data,new_data,environment,date,job,comments) values (#{user},#{ip},#{oldData},#{newData},#{environment},#{date},#{job},#{comments})")
    void addLog(String user, String ip, String oldData, String newData, String environment, Timestamp date,String job,String comments);

    @Select("select pljypich,jiaoyirq,wenjleix,plwenjzt from kapb_wjxxib where plwenjzt not in ('D8','S8')")
    List<KapbWjxxibSimply> queryAll();

    @Update({
            "<script>",
            "update kapb_wjxxib set plwenjzt='S8' where pljypich in ",
            "<foreach collection='pljypichList' item='pljypich' open='(' separator= ',' close=')'>",
            "#{pljypich}",
            "</foreach>",
            "</script>"
    })
    int turnS8All(@Param("pljypichList") ArrayList<String> pljypichList);

    @Select({
            "<script>",
            "select pljypich,jiaoyirq,wenjleix,plwenjzt from kapb_wjxxib where pljypich in ",
                "<foreach collection='pljypichList' item='pljypich' open='(' separator= ',' close=')'>",
                "#{pljypich}",
                "</foreach>",
            "</script>"
    })
    List<KapbWjxxibSimply> queryList(@Param("pljypichList")ArrayList<String> pljypichList);
}
