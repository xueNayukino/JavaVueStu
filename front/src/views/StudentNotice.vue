<template>
    <div class="student-notice">
        <!-- 加载中提示 -->
        <div v-if="isLoading" class="loading">
            加载中...
        </div>
        
        <!-- 通知列表 -->
        <div v-else class="notification-list">
            <div v-if="notification_list.length === 0" class="no-data">
                暂无通知
            </div>
            <div v-else
                v-for="(notification, index) in notification_list"
                :key="notification.id"
                class="notification-item"
                @click="showDetails(notification)"
                @mouseenter="hoverNotification(index)"
                @mouseleave="hoverNotification(null)"
                :class="{ 'hovered': hoveredIndex === index }">
                
                <div class="notification-header">
                    <h3>{{ notification.title }}</h3>
                    <span class="timestamp">{{ notification.timestamp }}</span>
                </div>
                <p class="notification-content">{{ notification.content }}</p>
            </div>
        </div>

        <!-- 修改模态框组件的使用方式 -->
        <NotificationModel
            v-if="isModelVisible"
            :visible="isModelVisible"
            :notification="selectedNotification"
            @close="closeModal"
        />
    </div>
</template>

<script>
import axios from 'axios';
import NotificationModel from '@/components/StudentNotification.vue';

export default {
    name: 'StudentNotice',
    components: {
        NotificationModel,
    },
    data() {
        return {
            notification_list: [],
            isModelVisible: false,
            selectedNotification: null,
            isLoading: true,
            hoveredIndex: null,
        };
    },
    methods: {
        showDetails(notification) {
            console.log('显示通知详情:', notification); // 添加调试日志
            this.selectedNotification = notification;
            this.isModelVisible = true;
        },
        closeModal() {
            console.log('关闭模态框'); // 添加调试日志
            this.isModelVisible = false;
            this.selectedNotification = null;
        },
        async get_notification() {
            this.isLoading = true;
            try {
                const response = await axios.get('/api/notification');
                console.log('获取到的通知数据:', response.data);
                
                if (response.data.success) {
                    this.notification_list = response.data.result.notifications || [];
                    
                    // 格式化时间戳
                    this.notification_list = this.notification_list.map(notice => ({
                        ...notice,
                        timestamp: this.formatDate(notice.timestamp)
                    }));
                    
                    // 按时间戳降序排序
                    this.notification_list.sort((a, b) => 
                        new Date(b.timestamp) - new Date(a.timestamp)
                    );
                } else {
                    console.error("获取通知失败：", response.data.message);
                }
            } catch (error) {
                console.error("获取通知失败：", error);
            } finally {
                this.isLoading = false;
            }
        },
        formatDate(dateStr) {
            const date = new Date(dateStr);
            return date.toLocaleString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            });
        },
        hoverNotification(index) {
            this.hoveredIndex = index;
        }
    },
    mounted() {
        this.get_notification();
    },
};
</script>

<style scoped>
.student-notice {
    padding: 20px;
    max-width: 800px;
    margin: 0 auto;
}

.loading {
    text-align: center;
    padding: 20px;
    color: #666;
}

.no-data {
    text-align: center;
    padding: 20px;
    color: #999;
}

.notification-item {
    background: white;
    padding: 15px;
    margin-bottom: 15px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    cursor: pointer;
    transition: all 0.3s ease;
}

.notification-item.hovered {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0,0,0,0.15);
}

.notification-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
}

.notification-header h3 {
    margin: 0;
    color: #333;
    font-size: 18px;
}

.timestamp {
    color: #999;
    font-size: 14px;
}

.notification-content {
    color: #666;
    margin: 0;
    line-height: 1.5;
}
</style>
