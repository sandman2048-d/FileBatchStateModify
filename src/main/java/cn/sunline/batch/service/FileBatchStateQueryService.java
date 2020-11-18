package cn.sunline.batch.service;

import cn.sunline.batch.mapper.FileBatchStateModifyMapper;
import cn.sunline.batch.pojo.BaseEnum;
import cn.sunline.batch.pojo.KapbWjxxib;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:29 2020/11/17
 */
@Service
@DS("ds1")
public class FileBatchStateQueryService {
    @Resource
    private FileBatchStateModifyMapper mapper;

    public KapbWjxxib query(String pljypich, String picihaoo, BaseEnum plwenjzht) {
        KapbWjxxib result = mapper.query(pljypich);
        return result;
    }

}
