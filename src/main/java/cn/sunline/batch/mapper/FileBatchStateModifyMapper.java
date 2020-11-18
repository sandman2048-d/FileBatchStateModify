package cn.sunline.batch.mapper;

import cn.sunline.batch.pojo.BaseEnum;
import cn.sunline.batch.pojo.KapbPlztxgrz;
import cn.sunline.batch.pojo.KapbWjxxib;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

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

    @Insert("insert into kapb_plztxgrz (user,ip,old_data,new_data,environment,date) values (#{user},#{ip},#{oldData},#{newData},#{environment},#{date})")
    void addLog(String user, String ip, String oldData, String newData, String environment, Timestamp date);

}
