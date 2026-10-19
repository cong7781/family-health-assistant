<template>
  <div class="main-layout">
    <el-container style="height: 100vh">
      <!-- ===== 左侧边栏 ===== -->
      <el-aside width="220px" class="main-aside">
        <!-- Logo 区域 -->
        <div class="aside-logo">
          <div class="logo-icon">
            <img src="./assets/img/help.png" alt="logo" />
          </div>
          <div class="logo-text">
            <span class="logo-title">家庭健康管理</span>
            <span class="logo-sub">Family Health</span>
          </div>
        </div>

        <!-- 菜单区域 -->
        <div class="aside-menu-wrap">
          <el-menu
            :default-active="activeMenu"
            :default-openeds="['1']"
            router
            background-color="#1a2332"
            text-color="#bfcbd9"
            active-text-color="#ffffff"
            class="aside-menu"
          >
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-s-home"></i>
                <span>系统菜单</span>
              </template>
              <el-menu-item index="/member">
                <i class="el-icon-user"></i>
                <span>家庭成员管理</span>
              </el-menu-item>
              <el-menu-item index="/record">
                <i class="el-icon-document"></i>
                <span>成员档案管理</span>
              </el-menu-item>
              <el-menu-item index="/aichat">
                <i class="el-icon-s-custom"></i>
                <span>智慧医生</span>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </div>

        <!-- 底部信息 -->
        <div class="aside-footer">
          <i class="el-icon-medal"></i>
          <span>v1.0.0</span>
        </div>
      </el-aside>

      <!-- ===== 右侧主体 ===== -->
      <el-container class="main-right">
        <!-- 头部 -->
        <el-header class="main-header">
          <!-- 面包屑 -->
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/main' }">
                <i class="el-icon-s-home"></i> 首页
              </el-breadcrumb-item>
              <el-breadcrumb-item v-if="pageTitle">{{ pageTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>

          <!-- 用户操作区 -->
          <div class="header-right">
            <el-tooltip content="消息通知" placement="bottom">
              <i class="el-icon-bell header-icon"></i>
            </el-tooltip>

            <el-dropdown trigger="click" @command="handleCommand">
              <div class="user-info">
                <div class="user-avatar">
                  <span>{{ name.charAt(0) }}</span>
                </div>
                <span class="user-name">{{ name }}</span>
                <i class="el-icon-arrow-down user-arrow"></i>
              </div>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item icon="el-icon-user" command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item icon="el-icon-lock" command="password">修改密码</el-dropdown-item>
                <el-dropdown-item icon="el-icon-switch-button" command="logout" divided>
                  安全退出
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 主内容区 -->
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: '管理员'
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    },
    pageTitle() {
      const titles = {
        '/member': '家庭成员管理',
        '/record': '成员档案管理',
        '/aichat': '智慧医生'
      }
      return titles[this.$route.path] || ''
    }
  },
  methods: {
    handleCommand(cmd) {
      if (cmd === 'logout') {
        this.$confirm('您确定要退出吗?', '退出确认', {
          confirmButtonText: '确定退出',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            sessionStorage.clear()
            this.$router.replace('/login')
          })
          .catch(() => {})
      } else if (cmd === 'profile') {
        this.$message.info('个人信息功能开发中')
      } else if (cmd === 'password') {
        this.$message.info('修改密码功能开发中')
      }
    }
  },
  mounted() {
    this.name = sessionStorage.getItem('name') || '管理员'
  }
}
</script>

<style scoped>
/* ===== 整体布局 ===== */
.main-layout {
  height: 100vh;
  overflow: hidden;
}

/* ===== 侧边栏 ===== */
.main-aside {
  background-color: #1a2332 !important;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: relative;
}
.main-aside::after {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 1px;
  background: linear-gradient(
    180deg,
    rgba(46, 171, 209, 0.4) 0%,
    rgba(255, 255, 255, 0.05) 50%,
    rgba(46, 171, 209, 0.15) 100%
  );
}

/* Logo 区域 */
.aside-logo {
  display: flex;
  align-items: center;
  padding: 20px 18px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  background: linear-gradient(135deg, rgba(46, 171, 209, 0.15), transparent);
}
.logo-icon {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  border: 2px solid rgba(46, 171, 209, 0.5);
}
.logo-icon img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.logo-text {
  margin-left: 12px;
  display: flex;
  flex-direction: column;
}
.logo-title {
  font-size: 15px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 1px;
  line-height: 1.3;
}
.logo-sub {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.4);
  letter-spacing: 0.5px;
}

/* 菜单区域 */
.aside-menu-wrap {
  flex: 1;
  overflow-y: auto;
  padding-top: 8px;
}
.aside-menu {
  border-right: none !important;
}
.aside-menu >>> .el-submenu__title {
  font-weight: 500;
  letter-spacing: 0.5px;
}
.aside-menu >>> .el-submenu__title:hover {
  background-color: rgba(255, 255, 255, 0.04) !important;
}
.aside-menu >>> .el-submenu__title i {
  color: rgba(255, 255, 255, 0.5);
}
.aside-menu >>> .el-menu-item {
  font-size: 13px;
  height: 44px;
  line-height: 44px;
  margin: 2px 8px;
  border-radius: 8px;
  transition: all 0.2s ease;
}
.aside-menu >>> .el-menu-item:hover {
  background-color: rgba(46, 171, 209, 0.12) !important;
  color: #fff !important;
}
.aside-menu >>> .el-menu-item.is-active {
  background: linear-gradient(90deg, rgba(46, 171, 209, 0.3), rgba(46, 171, 209, 0.08)) !important;
  color: #fff !important;
  font-weight: 600;
  border-left: 3px solid #2eabd1;
  border-radius: 0 8px 8px 0;
  margin-left: 5px;
}
.aside-menu >>> .el-menu-item i {
  margin-right: 8px;
  font-size: 15px;
}

/* 底部信息 */
.aside-footer {
  padding: 14px 18px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  color: rgba(255, 255, 255, 0.3);
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 6px;
}

/* ===== 右侧主体 ===== */
.main-right {
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 头部 */
.main-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  padding: 0 24px;
  height: 56px !important;
  z-index: 10;
  flex-shrink: 0;
}
.header-left {
  display: flex;
  align-items: center;
}
.header-left >>> .el-breadcrumb {
  font-size: 13px;
}
.header-left >>> .el-breadcrumb__inner {
  color: #606266;
  font-weight: 400;
}
.header-left >>> .el-breadcrumb__inner.is-link:hover {
  color: #2eabd1;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}
.header-icon {
  font-size: 20px;
  color: #909399;
  cursor: pointer;
  transition: all 0.2s ease;
  padding: 6px;
  border-radius: 50%;
}
.header-icon:hover {
  color: #2eabd1;
  background: rgba(46, 171, 209, 0.08);
}

/* 用户信息 */
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 10px 4px 4px;
  border-radius: 24px;
  transition: all 0.2s ease;
}
.user-info:hover {
  background: #f5f7fa;
}
.user-avatar {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: linear-gradient(135deg, #2eabd1, #3cc6e8);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 15px;
  font-weight: 600;
}
.user-name {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}
.user-arrow {
  font-size: 12px;
  color: #c0c4cc;
  transition: transform 0.2s ease;
}

/* 主内容区 */
.main-content {
  flex: 1;
  overflow-y: auto;
  background: #f0f4f8;
  padding: 20px 24px;
}
</style>
