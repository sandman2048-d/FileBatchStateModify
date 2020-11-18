package cn.sunline.batch.mapper;

import cn.sunline.batch.pojo.BaseEnum;
import cn.sunline.batch.pojo.KapbWjxxib;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:33 2020/11/17
 */
@Mapper
public interface FileBatchStateModifyMapper {
    @Select("select * from kapb_wjxxib where pljypich=#{pljypich}")
    public KapbWjxxib query(String pljypich, String picihaoo, BaseEnum plwenjzht);

    @Update("update kapb_wjxxib set plwenjzt = 'S8' where pljypich = #{pljypich}")
    public int turnS8ByPljypich(String pljypich);
}
