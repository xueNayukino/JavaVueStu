package cn.cuit.service.impl;

import cn.cuit.domain.Log;
import cn.cuit.mapper.LogMapper;
import cn.cuit.service.LogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    
    @Override
    public List<Log> getAllLogs() {
        return this.list();
    }

    @Override
    public List<Log> searchLogs(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllLogs();
        }

        LambdaQueryWrapper<Log> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Log::getModifyContent, query)
                .or()
                .like(Log::getModifyOperationType, query);
        
        return this.list(wrapper);
    }
}







