<template>
    <div class="outer-container">
        <div class="log-container">
            <h2>登录</h2>
            <div>
                <label for="account"></label>
                <input v-model="account" type="text" id="account" placeholder="请输入账户">
            </div>
            <div>
                <label for="password"></label>
                <input v-model="password" type="text" id="password" placeholder="请输入密码">
            </div>
            <div v-if="errorMessage" class="error-message">
                {{ errorMessage }}
            </div>
            <div class="button-container">
                <el-button type="primary" @click="Login('admin')">管理员登录</el-button><br>
                <el-button type="primary" @click="Login('student')">学生登录</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';
    export default {
        name: 'logIndex',
        data() {
            return {
                account: '',
                password: '',
                errorMessage: '',
            }
        },
        methods: {
            async Login(role) {
                if (!this.account || !this.password) {
                    this.errorMessage = '账户或密码不能为空';
                    return;
                }
                try {
                    const response = await axios.post('/api/admin/login', {
                        account: this.account,
                        password: this.password,
                        type: role,
                    })

                    const data = response.data;

                    if (data.success) {
                        localStorage.setItem('token', data.token);
                        localStorage.setItem('role', role);
                        if (role === 'student' && data.result && data.result.studentId) {
                            localStorage.setItem('id', data.result.studentId);
                        }
                        
                        if (role === 'admin') {
                            this.$router.push('/admin/Overview')
                        } else {
                            this.$router.push('/student/home')
                        }
                    } else {
                        this.errorMessage = '账户或密码错误';
                    }

                } catch (error) {
                    this.errorMessage = '未连接上服务器';
                    console.error('登录失败', error);
                }
            },
        }
    }
</script>

<style scoped>
/* 外层容器样式 */
.outer-container {
    display: flex;
    justify-content: center; /* 水平居中 */
    align-items: center; /* 垂直居中 */
    height: 100vh; /* 占满整个视口高度 */
    background-color: #f9f9f9; /* 背景颜色（可选） */
}

/* 全局容器样式 */
.log-container {
    width: 400px;
    padding: 20px;
    border: 1px solid #e0e0e0; /* 边框 */
    border-radius: 8px; /* 圆角 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 阴影 */
    background-color: #fff; /* 背景颜色 */
}

/* 输入框和按钮的整体布局 */
.log-container > div {
    margin-bottom: 15px;
    display: flex;
    flex-direction: column; /* 保证子元素垂直排列 */
}

/* 标签样式 */
label {
    font-size: 14px;
    color: #333;
    margin-bottom: 5px;
}

/* 输入框样式 */
input[type="text"],
input[type="password"] {
    width: 100%;
    padding: 8px 12px;
    font-size: 14px;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box; /* 保证内边距和边框计入总宽度 */
    outline: none;
    transition: border-color 0.3s;
}

input[type="text"]:focus,
input[type="password"]:focus {
    border-color: #409eff; /* 输入框聚焦时的颜色 */
}

/* 错误提示信息样式 */
.error-message {
    color: #f56c6c; /* 红色 */
    font-size: 12px;
    margin-top: -10px;
    margin-bottom: 10px;
    text-align: center;
}

/* 标题样式 */
h2 {
    text-align: center;
    font-size: 24px;
    color: #333;
    margin-bottom: 20px;
}

/* 按钮容器样式 */
.button-container {
    display: flex;
    flex-direction: column; /* 按钮纵向排列 */
    align-items: center; /* 水平居中 */
}

/* 按钮样式 */
.el-button {
    width: 80%; /* 按钮宽度为父容器的 80% */
    height: 40px; /* 统一高度 */
    font-size: 14px;
    border-radius: 4px; /* 保持圆角一致 */
    box-sizing: border-box; /* 保证宽高计算一致 */
}
</style>
