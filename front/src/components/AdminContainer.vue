<template>
  <el-container class="admin-container">
    <!-- 头部 -->
    <el-header class="header">
      <div class="header-content">
        <span class="header-title">管理系统</span>
        <el-button type="text" class="logout-button" @click="handleLogout">登出</el-button>
      </div>
    </el-header>

    <!-- 主体 -->
    <el-container class="main-container">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="aside">
        <el-menu :default-active="activeMenu" @select="handleMenuSelect" class="custom-menu">
          <el-menu-item index="overview">
            <i class="el-icon-house"></i> 总览
          </el-menu-item>
          <el-menu-item index="student-management">
            <i class="el-icon-user"></i> 学生管理
          </el-menu-item>
          <el-menu-item index="notification-management">
            <i class="el-icon-bell"></i> 通知管理
          </el-menu-item>
          <el-menu-item index="Log-management">
            <i class="el-icon-bell"></i> 日志
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
      activeMenu: 'overview',
    };
  },
  methods: {
    handleMenuSelect(index) {
      this.activeMenu = index;
      this.$router.push(`/admin/${index}`);
    },
    handleLogout() {
      localStorage.removeItem('token');
      this.$router.push('/');
    },
  },
};
</script>

<style scoped>
.admin-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 头部样式 */
.header {
  background-color: #343a40;
  color: #ffffff;
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
  color: #e57373;
}

/* 主容器样式 */
.main-container {
  flex: 1;
  overflow: hidden;
}

/* 侧边栏样式 */
.aside {
  background-color: #f8f9fa;
  color: #343a40;
  height: 100%;
  border-right: 1px solid #dee2e6;
  box-shadow: 1px 0 5px rgba(0, 0, 0, 0.1);
}

/* 自定义菜单样式 */
.custom-menu {
  background-color: transparent;
  border: none;
}

.custom-menu .el-menu-item {
  color: #495057;
  font-size: 14px;
  padding: 12px 20px;
  border-radius: 4px;
  transition: all 0.2s ease-in-out;
}

.custom-menu .el-menu-item.is-active {
  background-color: #e9ecef;
  color: #212529;
  font-weight: 500;
}

.custom-menu .el-menu-item:hover {
  background-color: #f1f3f5;
  color: #212529;
  cursor: pointer;
}

.custom-menu .el-icon-house,
.custom-menu .el-icon-user,
.custom-menu .el-icon-bell {
  margin-right: 10px;
  font-size: 16px;
  color: #868e96;
}

/* 内容区域样式 */
.main-content {
  background-color: #ffffff;
  padding: 20px;
  height: 100%;
  overflow-y: auto;
  box-sizing: border-box;
}
</style>
  