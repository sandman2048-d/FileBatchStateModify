package cn.sunline.batch.service;

import cn.sunline.batch.mapper.FileBatchStateModifyMapper;
import cn.sunline.batch.pojo.KapbPlztxgrz;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 13:49 2020/11/18
 */
@Service
@DS("ds1")
public class FileBatchStateLogService {
    @Resource
    private FileBatchStateModifyMapper mapper;

    public void addLog(KapbPlztxgrz plztxgrz){
        mapper.addLog(plztxgrz.getUser(),plztxgrz.getIp(),plztxgrz.getOldData(),plztxgrz.getNewData(),plztxgrz.getEnvironment(),plztxgrz.getDate());
    }
}
