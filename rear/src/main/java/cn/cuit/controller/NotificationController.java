package cn.cuit.controller;

////import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.cuit.domain.Notification;
import cn.cuit.service.NotificationService;
import cn.cuit.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 通知信息接口
 */
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 获取所有通知信息
     */
    @GetMapping
    public AjaxResult getNotifications() {
        try {
            List<Notification> notificationList = notificationService.list();
            List<Map<String, Object>> formattedList = notificationList.stream()
                .map(notification -> {
                    Map<String, Object> formattedNotification = new HashMap<>();
                    formattedNotification.put("id", notification.getFileId());
                    formattedNotification.put("title", notification.getFileTitle());
                    formattedNotification.put("content", notification.getFileContent());
                    formattedNotification.put("timestamp", 
                        notification.getPublishDate() != null ? 
                        notification.getPublishDate().toString() : 
                        notification.getModifyDate().toString());
                    return formattedNotification;
                })
                .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("notifications", formattedList);
            
            return AjaxResult.me()
                .setSuccess(true)
                .setResult(response);
        } catch (Exception e) {
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("获取通知失败: " + e.getMessage());
        }
    }

    /**
     * 添加新通知
     */
    @PostMapping("/add")
    public AjaxResult addNotification(@RequestBody Map<String, String> notificationData) {
        try {
            // 参数校验
            if (notificationData == null || 
                notificationData.get("title") == null || 
                notificationData.get("content") == null) {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("标题和内容不能为空");
            }

            Notification notification = new Notification();
            notification.setFileTitle(notificationData.get("title"));
            notification.setFileContent(notificationData.get("content"));
            notification.setPublishDate(new Date());
            notification.setModifyDate(new Date());
            notification.setViewCount(0);
            notification.setPublisherId(1); // 设置一个默认的发布者ID
            
            boolean saved = notificationService.save(notification);
            if (saved) {
                return AjaxResult.me()
                    .setSuccess(true)
                    .setMessage("添加通知成功");
            } else {
                return AjaxResult.me()
                    .setSuccess(false)
                    .setMessage("添加通知失败");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 添加详细日志
            return AjaxResult.me()
                .setSuccess(false)
                .setMessage("添加通知失败: " + e.getMessage());
        }
    }
} 