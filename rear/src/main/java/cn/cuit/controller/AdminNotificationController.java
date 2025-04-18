package cn.cuit.controller;

import cn.cuit.domain.Notification;
import cn.cuit.domain.Log;
import cn.cuit.service.NotificationService;
import cn.cuit.service.LogService;
import cn.cuit.utils.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminNotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private LogService logService;

    @GetMapping("/notification")
    public ResponseEntity<ResponseWrapper<Map<String, Object>>> getNotifications() {
        log.info("获取通知列表请求");
        try {
            // 使用QueryWrapper按发布时间降序排序
            LambdaQueryWrapper<Notification> queryWrapper = Wrappers.<Notification>lambdaQuery()
                    .orderByDesc(Notification::getPublishDate);
            
            List<Notification> notifications = notificationService.list(queryWrapper);
            Map<String, Object> data = new HashMap<>();
            data.put("notifications", notifications);
            
            // 记录查询操作日志
            try {
                Log operationLog = new Log();
                operationLog.setModifyOperationType(Log.OPERATION_QUERY);
                operationLog.setModifyContent("查询通知列表");
                operationLog.setModifyTime(new Date());
                logService.save(operationLog);
            } catch (Exception logError) {
                log.warn("记录日志失败，但不影响主要业务: ", logError);
            }
            
            return ResponseEntity.ok(new ResponseWrapper<>(true, data));
        } catch (Exception e) {
            log.error("获取通知列表失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "获取通知列表失败"));
        }
    }

    @PostMapping("/editNotification")
    public ResponseEntity<ResponseWrapper<Void>> editNotification(@RequestBody Notification notification) {
        log.info("编辑通知请求: {}", notification);
        try {
            if (notification.getFileId() == null) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "通知ID不能为空"));
            }
            if (notification.getFileTitle() == null || notification.getFileTitle().trim().isEmpty()) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "通知标题不能为空"));
            }
            if (notification.getFileContent() == null || notification.getFileContent().trim().isEmpty()) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "通知内容不能为空"));
            }

            // 获取原有通知信息
            Notification existingNotification = notificationService.getById(notification.getFileId());
            if (existingNotification == null) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "通知不存在"));
            }

            // 设置修改日期
            notification.setModifyDate(new Date());
            // 保留原有的其他字段
            notification.setPublishDate(existingNotification.getPublishDate());
            notification.setPublisherId(existingNotification.getPublisherId());
            notification.setViewCount(existingNotification.getViewCount());

            boolean success = notificationService.updateById(notification);
            if (success) {
                // 记录修改操作日志
                try {
                    Log operationLog = new Log();
                    operationLog.setModifyOperationType(Log.OPERATION_MODIFY);
                    operationLog.setModifyContent("修改通知: ID=" + notification.getFileId() + 
                                               ", 标题=" + notification.getFileTitle());
                    operationLog.setModifyTime(new Date());
                    operationLog.setFileId(notification.getFileId());
                    logService.save(operationLog);
                } catch (Exception logError) {
                    log.warn("记录日志失败，但不影响主要业务: ", logError);
                }
                
                return ResponseEntity.ok(new ResponseWrapper<>(true, null, "修改成功"));
            } else {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "修改失败"));
            }
        } catch (Exception e) {
            log.error("编辑通知失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "编辑失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/deleteNotification")
    public ResponseEntity<ResponseWrapper<Void>> deleteNotification(@RequestParam Integer id) {
        log.info("删除通知请求: {}", id);
        try {
            // 获取通知信息用于日志记录
            Notification notification = notificationService.getById(id);
            if (notification == null) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "通知不存在"));
            }

            boolean success = notificationService.removeById(id);
            if (success) {
                // 记录删除操作日志
                try {
                    Log operationLog = new Log();
                    operationLog.setModifyOperationType(Log.OPERATION_MODIFY);
                    operationLog.setModifyContent("删除通知: ID=" + id + 
                                               ", 标题=" + notification.getFileTitle());
                    operationLog.setModifyTime(new Date());
                    operationLog.setFileId(id);
                    logService.save(operationLog);
                } catch (Exception logError) {
                    log.warn("记录日志失败，但不影响主要业务: ", logError);
                }
                
                return ResponseEntity.ok(new ResponseWrapper<>(true, null, "删除成功"));
            } else {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "删除失败"));
            }
        } catch (Exception e) {
            log.error("删除通知失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "删除失败: " + e.getMessage()));
        }
    }

    @PostMapping("/addNotification")
    public ResponseEntity<ResponseWrapper<Void>> addNotification(@RequestBody Notification notification) {
        log.info("添加通知请求: {}", notification);
        try {
            // 验证必要字段
            if (notification.getFileTitle() == null || notification.getFileTitle().trim().isEmpty()) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "通知标题不能为空"));
            }
            if (notification.getFileContent() == null || notification.getFileContent().trim().isEmpty()) {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "通知内容不能为空"));
            }

            // 设置默认值
            Date now = new Date();
            notification.setPublishDate(now);
            notification.setModifyDate(now);
            notification.setViewCount(0);  // 初始访问次数为0
            // TODO: 设置发布者ID，需要从登录信息中获取
            // notification.setPublisherId(getCurrentAdminId());

            boolean success = notificationService.save(notification);
            if (success) {
                // 记录添加操作日志
                try {
                    Log operationLog = new Log();
                    operationLog.setModifyOperationType(Log.OPERATION_PUBLISH);
                    operationLog.setModifyContent("发布通知: 标题=" + notification.getFileTitle());
                    operationLog.setModifyTime(now);
                    // 获取新增通知的ID
                    operationLog.setFileId(notification.getFileId());
                    logService.save(operationLog);
                } catch (Exception logError) {
                    log.warn("记录日志失败，但不影响主要业务: ", logError);
                }
                
                return ResponseEntity.ok(new ResponseWrapper<>(true, null, "添加成功"));
            } else {
                return ResponseEntity.ok(new ResponseWrapper<>(false, null, "添加失败"));
            }
        } catch (Exception e) {
            log.error("添加通知失败: ", e);
            return ResponseEntity.ok(new ResponseWrapper<>(false, null, "添加失败: " + e.getMessage()));
        }
    }
}
