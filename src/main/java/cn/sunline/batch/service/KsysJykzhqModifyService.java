package cn.sunline.batch.service;

import cn.sunline.batch.mapper.KsysJykzhqModifyMapper;
import cn.sunline.batch.pojo.*;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Tom
 * @Discription:
 * @Date: Create in 10:29 2020/11/17
 */
@Service
@DS("dev")
public class KsysJykzhqModifyService {
    @Resource
    private KsysJykzhqModifyMapper mapper;



    public Result changeToRunning(String pljyzbsh,String jiaoyima) {

        int row = mapper.changeToRunning(pljyzbsh,jiaoyima);
        if (row == 1) {
            return new Result(BaseEnum.success.name(), "交易成功", null);
        }
        throw new RuntimeException("数据修改失败，未知错误");

    }


    public Result changeToStop(String pljyzbsh,String jiaoyima) {
        int row = mapper.changeToStop(pljyzbsh,jiaoyima);
        if (row == 1) {
            return new Result(BaseEnum.success.name(), "交易成功", null);
        }
        throw new RuntimeException("数据修改失败，未知错误");
    }

//    public KsysJykzhq queryByJiaoyima(String jiaoyima) {
//        KsysJykzhq ksysJykzhq = mapper.queryByJiaoyima(jiaoyima);
//        if (ksysJykzhq==null){
//            throw new RuntimeException("未查询到对应记录");
//        }
//        return ksysJykzhq;
//    }

    public KsysJykzhq queryOne(String pljyzbsh,String jiaoyima) {
        KsysJykzhq ksysJykzhq = mapper.queryOne(jiaoyima,pljyzbsh);
        if (ksysJykzhq==null){
            throw new RuntimeException("未查询到对应记录");
        }
        return ksysJykzhq;
    }

    public List<KsysJykzhq> queryByPljyzbsh(String pljyzbsh) {
        List<KsysJykzhq> ksysJykzhqList = mapper.queryByPljyzbsh(pljyzbsh);
        if (ksysJykzhqList==null||ksysJykzhqList.size()==0){
            throw new RuntimeException("未查询到对应记录");
        }
        return ksysJykzhqList;
    }

    public KsysJykzhq queryByPljioyma(String pljyzbsh, String pljioyma) {
        KsysJykzhq ksysJykzhq = mapper.queryOne(pljioyma,pljyzbsh);
        if (ksysJykzhq==null){
            throw new RuntimeException("未查询到对应记录");
        }
        return ksysJykzhq;
    }
}
