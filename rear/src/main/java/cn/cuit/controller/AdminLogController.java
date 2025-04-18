package cn.cuit.controller;

import cn.cuit.domain.Log;
import cn.cuit.service.LogService;
import cn.cuit.utils.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminLogController {
    
    @Autowired
    private LogService logService;

    @GetMapping("/getLogs")
    public ResponseEntity<ResponseWrapper<List<Map<String, Object>>>> getLogs() {
        try {
            List<Log> logs = logService.getAllLogs();
            List<Map<String, Object>> logList = convertLogsToMap(logs);
            return ResponseEntity.ok(new ResponseWrapper<>(true, logList));
        } catch (Exception e) {
            log.error("获取日志失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "获取日志失败"));
        }
    }

    @GetMapping("/filterLogs")
    public ResponseEntity<ResponseWrapper<List<Map<String, Object>>>> filterLogs(
            @RequestParam(required = false) String query) {
        try {
            List<Log> logs = logService.searchLogs(query);
            List<Map<String, Object>> logList = convertLogsToMap(logs);
            return ResponseEntity.ok(new ResponseWrapper<>(true, logList));
        } catch (Exception e) {
            log.error("搜索日志失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "搜索日志失败"));
        }
    }

    private List<Map<String, Object>> convertLogsToMap(List<Log> logs) {
        return logs.stream()
                .map(log -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", log.getModifyId());
                    map.put("content", log.getModifyContent());
                    map.put("timestamp", log.getModifyTime());
                    map.put("adminId", log.getAdminId());
                    map.put("operationType", log.getModifyOperationType());
                    map.put("studentId", log.getStudentId());
                    map.put("fileId", log.getFileId());
                    return map;
                })
                .collect(Collectors.toList());
    }
}
