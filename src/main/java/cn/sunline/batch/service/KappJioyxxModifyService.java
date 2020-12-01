package cn.sunline.batch.service;

import cn.sunline.batch.mapper.FileBatchStateModifyMapper;
import cn.sunline.batch.mapper.KappJioyxxModifyMapper;
import cn.sunline.batch.pojo.*;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:29 2020/11/17
 */
@Service
@DS("dev")
public class KappJioyxxModifyService {
    @Resource
    private KappJioyxxModifyMapper mapper;



    public Result changeToRunning(String jiaoyima) {

        int row = mapper.changeToRunning(jiaoyima);
        if (row == 1) {
            return new Result(BaseEnum.success.name(), "交易成功", null);
        }
        throw new RuntimeException("数据修改失败，未知错误");

    }


    public Result changeToStop(String jiaoyima) {
        int row = mapper.changeToStop(jiaoyima);
        if (row == 1) {
            return new Result(BaseEnum.success.name(), "交易成功", null);
        }
        throw new RuntimeException("数据修改失败，未知错误");
    }

    public KappJioyxx queryOne(String jiaoyima) {
        KappJioyxx kappJioyxx = mapper.query(jiaoyima);
        if (kappJioyxx==null){
            throw new RuntimeException("未查询到对应记录");
        }
        return kappJioyxx;
    }

}
