<template>
    <div v-if="isLoading">正在加载通知，请稍后...</div>
    <div v-else>
        <h1>通知管理</h1>
        <button @click="AddButtonVisible">新建通知</button>
        <div v-if="isAddVisible">
            <AddNotification :isVisible="isAddVisible" @update:isVisible="isAddVisible = $event" @add="addNotification" />
        </div>
        <div v-if="notification_list.length === 0" class="no-notification">
            暂无通知
        </div>
        <div v-else class="notification-container">
            <div v-for="(notification, index) in notification_list" :key="notification.fileId" class="notification-item"
                @mouseenter="hoverNotification(index)" @mouseleave="hoverNotification(null)"
                :class="{ 'highlight': hoveredIndex === index }">
                <span @click="showDetails(notification)">{{ notification.fileTitle }}</span>
                <div class="notification-info">
                    <span class="publish-date">{{ formatDate(notification.publishDate) }}</span>
                    <!-- <span class="view-count">浏览次数: {{ notification.viewCount }}</span> -->
                </div>
            </div>
        </div>

        <NotificationModel :visible="isModelVisible" :notification="selectedNotification" @close="closeModal"
            @delete="deleteNotification" @edit="editNotification" />
    </div>
</template>

<script>
import axios from 'axios';
import NotificationModel from '@/components/NotificationModel.vue';
import AddNotification from '@/components/AddNotification.vue';
import { ElMessage } from 'element-plus';

export default {
    name: 'AdminNotification',
    components: {
        NotificationModel,
        AddNotification,
    },
    data() {
        return {
            notification_list: [],// 初始化为空数组
            isModelVisible: false,//关联详细信息的显示
            isAddVisible: false,//关联新建通知的显示
            selectedNotification: null,
            isLoading: true, // 加载状态
            hoveredIndex: null, // 当前悬停的通知索引
        };
    },
    methods: {
        AddButtonVisible() {
            this.isAddVisible = true;
        },
        closeAddButton() {
            this.isAddVisible = false;
        },
        showDetails(notification) {
            this.selectedNotification = {
                fileId: notification.fileId,
                fileTitle: notification.fileTitle,
                fileContent: notification.fileContent,
                publishDate: notification.publishDate,
                viewCount: notification.viewCount
            };
            this.isModelVisible = true;
        },
        closeModal() {
            this.selectedNotification = null;
            this.isModelVisible = false;
            this.fetchNotifications();
        },
        async deleteNotification(notification) {
            try {
                this.isLoading = true;
                const response = await axios.delete(`/api/admin/deleteNotification?id=${notification.notificationId}`);
                if (response.data.success) {
                    console.log("删除成功", response.data);
                    this.notification_list = this.notification_list.filter(
                        n => n.notificationId !== notification.notificationId
                    );
                } else {
                    console.error("删除失败：", response.data.message);
                    alert("删除失败：" + response.data.message);
                }
            } catch (error) {
                console.error("删除失败", error);
                alert("删除失败，请检查网络连接");
            } finally {
                this.isLoading = false;
                this.closeModal();
            }
        },
        async fetchNotifications() {
            try {
                this.isLoading = true;
                const response = await axios.get('/api/admin/notification');
                console.log('获取通知成功', response.data);
                
                if (response.data.success) {
                    this.notification_list = response.data.data.notifications;
                    console.log('当前通知列表:', this.notification_list);
                } else {
                    ElMessage({
                        type: 'error',
                        message: response.data.message || '获取通知列表失败'
                    });
                }
            } catch (error) {
                console.error('获取通知列表失败:', error);
                ElMessage({
                    type: 'error',
                    message: '获取通知列表失败：' + error.message
                });
            } finally {
                this.isLoading = false;
            }
        },
        async editNotification(updatedNotification) {
            try {
                console.log('准备发送的更新数据:', updatedNotification); // 添加日志
                const response = await axios.post('/api/admin/editNotification', {
                    fileId: updatedNotification.fileId, // 使用正确的ID字段名
                    fileTitle: updatedNotification.title,
                    fileContent: updatedNotification.content
                });
                
                if (response.data.success) {
                    // 直接更新本地数据
                    const index = this.notification_list.findIndex(
                        item => item.fileId === updatedNotification.fileId // 使用正确的ID字段名
                    );
                    
                    if (index !== -1) {
                        // 保持原有数据结构
                        this.notification_list[index] = {
                            ...this.notification_list[index],
                            fileTitle: updatedNotification.title,
                            fileContent: updatedNotification.content,
                            modifyDate: new Date()
                        };
                    }
                    
                    ElMessage({
                        type: 'success',
                        message: '通知修改成功'
                    });
                    this.closeModal();
                } else {
                    ElMessage({
                        type: 'error',
                        message: response.data.message || '修改失败'
                    });
                }
            } catch (error) {
                console.error('编辑通知失败:', error);
                ElMessage({
                    type: 'error',
                    message: '编辑失败：' + error.message
                });
            }
        },
        hoverNotification(index) {
            this.hoveredIndex = index; // 记录悬停的索引
        },
        async addNotification(notification) {
            try {
                console.log('准备发送的通知数据:', notification); // 添加日志
                this.isLoading = true;
                const response = await axios.post('/api/admin/addNotification', notification);
                console.log('服务器响应:', response.data); // 添加日志
                
                if (response.data.success) {
                    console.log("添加通知成功");
                    ElMessage({
                        type: 'success',
                        message: '添加通知成功'
                    });
                    await this.fetchNotifications(); // 重新获取通知列表
                    this.isAddVisible = false; // 关闭添加窗口
                } else {
                    console.error("添加通知失败：", response.data.message);
                    ElMessage({
                        type: 'error',
                        message: response.data.message || '添加通知失败'
                    });
                }
            } catch (error) {
                console.error("添加通知失败", error);
                ElMessage({
                    type: 'error',
                    message: error.response?.data?.message || '添加通知失败，请检查网络连接'
                });
            } finally {
                this.isLoading = false;
            }
        },
        formatDate(date) {
            if (!date) return '';
            const d = new Date(date);
            return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
        },
    },
    mounted() {
        this.fetchNotifications(); // 初始化时获取通知
    },
};
</script>

<style scoped>
/* 通知容器 */
.notification-container {
    margin-top: 20px;
    display: flex;
    flex-direction: column;
    gap: 10px;
    /* 每个通知之间的间距 */
}

/* 单个通知项 */
.notification-item {
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 8px;
    background-color: #f9f9f9;
    transition: all 0.3s ease;
    cursor: pointer;
}

/* 鼠标悬停高亮 */
.notification-item.highlight {
    background-color: #e8f4fc;
    /* 高亮背景色 */
    text-decoration: underline;
    /* 下划线 */
    border-color: #007bff;
    /* 边框颜色变化 */
}

/* 标题 */
.notification-item span {
    font-size: 16px;
    font-weight: 600;
    color: #333;
}

.notification-item span:hover {
    color: #007bff;
    /* 鼠标悬停时文字颜色 */
}

/* 无通知提示 */
.no-notification {
    color: #777;
    font-size: 14px;
    margin-top: 20px;
}

.notification-info {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
    color: #666;
    margin-top: 5px;
}

.publish-date, .view-count {
    font-size: 12px;
}
</style>