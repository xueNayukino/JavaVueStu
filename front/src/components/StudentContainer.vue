<template>
    <el-container style="height: 100vh;">
      <!-- 头部 -->
      <el-header class="header">
        <div class="header-content">
          <span class="header-title">学生信息查看系统</span>
          <el-button type="text" class="logout-button" @click="handleLogout">注销</el-button>
        </div>
      </el-header>
  
      <!-- 主体 -->
      <el-container>
        <!-- 侧边栏 -->
        <el-aside width="200px" class="aside">
          <el-menu :default-active="activeMenu" @select="handleMenuSelect" class="custom-menu">
            <el-menu-item index="home">
              <i class="el-icon-house"></i> 总览
            </el-menu-item>
            <el-menu-item index="student-change">
              <i class="el-icon-user"></i> 信息修改
            </el-menu-item>
            <el-menu-item index="student-notice">
              <i class="el-icon-user"></i> 学生通知
            </el-menu-item>
            <el-menu-item index="student-registe">
              <i class="el-icon-user"></i> 签到
            </el-menu-item>
          </el-menu>
        </el-aside>
  
        <!-- 内容区域 -->
        <el-main class="main-content">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </template>
  
  <script>
  export default {
    name: 'AdminLayout',
    data() {
      return {
        activeMenu: 'home',
      };
    },
    methods: {
      handleMenuSelect(index) {
        this.activeMenu = index;
        this.$router.push(`/student/${index}`);
      },
      handleLogout() {
        // 清除登录信息并跳转到根目录
        localStorage.removeItem('token');
        this.$router.push('/');
      },
    },
  };
  </script>
  
  <style scoped>
  /* 头部样式 */
  .header {
    background-color: #343a40; /* 深色背景 */
    color: #ffffff; /* 白色字体 */
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    height: 60px;
  }
  .header-content {
    display: flex;
    align-items: center;
    width: 100%;
  }
  .header-title {
    font-size: 18px;
    font-weight: 500;
  }
  .logout-button {
    color: #ffffff;
    font-size: 14px;
    font-weight: 400;
    margin-left: auto;
  }
  .logout-button:hover {
    color: #e57373; /* 鼠标悬停时的颜色 */
  }
  
  /* 简约风格：侧边栏整体样式 */
  .aside {
    background-color: #f8f9fa; /* 浅色背景 */
    color: #343a40; /* 深灰字体 */
    height: 100%; /* 全高 */
    border-right: 1px solid #dee2e6; /* 右边框 */
    box-shadow: 1px 0 5px rgba(0, 0, 0, 0.1); /* 添加轻微阴影 */
  }
  
  /* 自定义菜单样式 */
  .custom-menu {
    background-color: transparent; /* 菜单背景透明 */
    border: none; /* 移除菜单默认边框 */
  }
  
  /* 菜单项样式 */
  .custom-menu .el-menu-item {
    color: #495057; /* 未选中时的字体颜色 */
    font-size: 14px; /* 字体大小 */
    padding: 12px 20px; /* 内边距 */
    border-radius: 4px; /* 圆角 */
    transition: all 0.2s ease-in-out; /* 添加过渡动画 */
  }
  
  /* 选中菜单项的高亮样式 */
  .custom-menu .el-menu-item.is-active {
    background-color: #e9ecef; /* 激活状态的背景色 */
    color: #212529; /* 激活状态的字体颜色 */
    font-weight: 500; /* 加粗选中项 */
  }
  
  /* 鼠标悬停样式 */
  .custom-menu .el-menu-item:hover {
    background-color: #f1f3f5; /* 悬停时背景色 */
    color: #212529; /* 悬停时字体颜色 */
    cursor: pointer; /* 鼠标变为手形 */
  }
  
  /* 图标样式 */
  .custom-menu .el-icon-house,
  .custom-menu .el-icon-user,
  .custom-menu .el-icon-bell {
    margin-right: 10px; /* 图标与文字的间距 */
    font-size: 16px; /* 图标大小 */
    color: #868e96; /* 图标颜色 */
  }
  
  /* 内容区域样式 */
  .main-content {
    background-color: #ffffff;
    padding: 20px;
    height: 100%;
    overflow-y: auto;
  }
  </style>
  