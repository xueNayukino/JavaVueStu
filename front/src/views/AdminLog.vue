<template>
    <div class="admin-log">
        <h1>日志管理</h1>
        <!-- 搜索框 -->
        <div class="search-bar">
            <input type="text" v-model="searchQuery" placeholder="搜索日志内容" @input="filterLogs" />
        </div>
        <!-- 日志列表 -->
        <div v-if="currentLogs.length > 0" class="log-list">
            <div v-for="log in currentLogs" :key="log.id" class="log-container">
                <p class="log-content">{{ log.content }}</p>
                <p class="log-timestamp">{{ log.timestamp }}</p>
                <p class="log-admin">操作人: {{ log.adminId }}</p>
                <p class="log-operation">操作类型: {{ log.operationType }}</p>
            </div>
        </div>
        <!-- 无日志时的提示 -->
        <div v-else class="no-logs">
            暂无匹配的日志
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'AdminLog',
    data() {
        return {
            logs: [], // 日志数据
            filteredLogs: [], // 筛选后的日志数据
            searchQuery: '' // 搜索框输入内容
        };
    },
    computed: {
        currentLogs() {
            return this.searchQuery.trim()
                ? this.filteredLogs
                : this.logs;
        }
    },
    methods: {
        async get_logs() {
            try {
                const response = await axios.get('/api/admin/getLogs');
                if (response.data.success) {
                    this.logs = response.data.data.map(log => ({
                        id: log.id,
                        content: log.content,
                        timestamp: log.timestamp,
                        adminId: log.adminId,
                        operationType: log.operationType
                    }));
                    this.filteredLogs = this.logs;
                    console.log("请求日志成功", this.logs);
                } else {
                    console.error("请求日志失败");
                }
            } catch (error) {
                console.error("请求日志时发生错误:", error);
            }
        },
        filterLogs() {
            const query = this.searchQuery.trim().toLowerCase();
            this.filteredLogs = this.logs.filter(log =>
                (log.content && log.content.toLowerCase().includes(query)) ||
                (log.operationType && log.operationType.toLowerCase().includes(query))
            );
        }
    },
    mounted() {
        this.get_logs();
    }
};
</script>

<style scoped>
.admin-log {
    padding: 10px;
    max-width: 1000px;
    margin: 0 auto;
    background-color: #f9f9f9;
}

.search-bar {
    margin-bottom: 15px;
    text-align: center;
}

.search-bar input {
    width: 100%;
    padding: 8px;
    font-size: 14px;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.log-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.log-container {
    background-color: #fff;
    border: 1px solid #ddd;
    padding: 12px;
    border-radius: 6px;
    margin: 5px 0;
    font-size: 13px;
    line-height: 1.3;
    transition: background-color 0.3s ease;
    cursor: pointer;
    max-width: 100%;
    word-wrap: break-word;
    overflow: hidden;
}

.log-container:hover {
    background-color: #f0f0f0;
    border-color: #007bff;
}

.log-container p {
    margin: 0;
    color: #333;
}

.log-timestamp {
    font-size: 12px;
    color: #777;
    margin-top: 6px;
}

.log-admin,
.log-operation {
    font-size: 12px;
    color: #555;
    margin-top: 4px;
}

.no-logs {
    text-align: center;
    color: #777;
    font-size: 15px;
    margin-top: 15px;
}
</style>
