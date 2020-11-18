package cn.sunline.batch.service;

import cn.sunline.batch.mapper.FileBatchStateModifyMapper;
import cn.sunline.batch.pojo.BaseEnum;
import cn.sunline.batch.pojo.KapbWjxxib;
import cn.sunline.batch.pojo.Result;
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
public class FileBatchStateModifyService {
    @Resource
    private FileBatchStateModifyMapper mapper;

    public KapbWjxxib queryOne(String pljypich) {
        return mapper.query(pljypich);
    }

    public Result turnS8ByPljypich(String pljypich) {

        int row = mapper.turnS8ByPljypich(pljypich);
        if (row == 1) {
            return new Result(BaseEnum.success.name(), "交易成功", null);
        }
        throw new RuntimeException("数据修改失败，未知错误");

    }
}
