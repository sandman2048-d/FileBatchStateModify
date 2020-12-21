package cn.sunline.batch.service;

import cn.sunline.batch.mapper.KsysJykzhqModifyMapper;
import cn.sunline.batch.mapper.SysDatMapper;
import cn.sunline.batch.pojo.BaseEnum;
import cn.sunline.batch.pojo.KappSysdat;
import cn.sunline.batch.pojo.KsysJykzhq;
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
@DS("dev")
public class KappSysdatService {
    @Resource
    private SysDatMapper mapper;


    public KappSysdat query() {
        KappSysdat kappSysdat = mapper.query();
        if (kappSysdat==null){
            throw new RuntimeException("未查询到对应记录");
        }
        return kappSysdat;
    }

}
