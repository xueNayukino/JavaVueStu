package cn.cuit.service;

import cn.cuit.domain.Log;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
* @author wft
* @description 针对表【log(日志表)】的数据库操作Service
* @createDate 2025-01-13 10:58:43
*/
public interface LogService extends IService<Log> {
    List<Log> getAllLogs();
    List<Log> searchLogs(String query);
}
