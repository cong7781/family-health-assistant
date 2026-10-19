<template>
  <div class="login-container">
    <!-- 背景遮罩层 -->
    <div class="login-overlay"></div>

    <!-- 品牌标语区 -->
    <div class="login-brand">
      <div class="brand-icon">
        <img src="./assets/img/help.png" alt="logo" />
      </div>
      <h1 class="brand-title">家庭健康管理</h1>
      <p class="brand-subtitle">守护您和家人的每一次心跳</p>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <div class="card-header">
        <div class="card-avatar">
          <img src="./assets/img/help.png" alt="avatar" />
        </div>
        <h2 class="card-title">欢迎回来</h2>
        <p class="card-desc">登录您的健康管理账户</p>
      </div>

      <el-form ref="form" :model="member" class="login-form" @keyup.enter.native="login">
        <el-form-item>
          <el-input
            v-model="member.account"
            placeholder="请输入账号"
            prefix-icon="el-icon-user"
            size="medium"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="member.password"
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            show-password
            size="medium"
          ></el-input>
        </el-form-item>
        <el-form-item class="login-actions">
          <el-button
            type="primary"
            @click="login"
            :loading="loading"
            class="btn-login"
            size="medium"
          >登 录</el-button>
          <el-button @click="resetForm" size="medium" class="btn-reset">重 置</el-button>
        </el-form-item>
      </el-form>

      <div class="card-footer">
        <span class="footer-hint">还没有账号？请联系管理员</span>
      </div>
    </div>

    <!-- 底部版权 -->
    <div class="login-copyright">
      <span>© 2026 Family Health. All rights reserved.</span>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      member: {
        account: '',
        password: ''
      }
    }
  },
  methods: {
    login() {
      if (!this.member.account.trim() || !this.member.password.trim()) {
        this.$message({
          message: '请输入账号和密码',
          type: 'warning',
          duration: 2000
        })
        return
      }

      this.loading = true
      this.$http.post('loginweb/login', this.member)
        .then(res => {
          const user = res.data
          if (user && user.id) {
            sessionStorage.setItem('id', user.id)
            sessionStorage.setItem('name', user.name)
            sessionStorage.setItem('gender', user.gender)
            sessionStorage.setItem('phone', user.phone)
            sessionStorage.setItem('account', user.account)
            sessionStorage.setItem('token', user.token)

            this.$router.push('/main')
          } else {
            this.$message({
              message: '账号或密码错误',
              type: 'warning',
              duration: 2000
            })
          }
        })
        .catch(err => {
          // 后端现在用 HTTP 401 表示「账号或密码错误」，要和「网络断了」区分开
          const status = err && err.response && err.response.status
          if (status === 401) {
            this.$message({
              message: '账号或密码错误',
              type: 'warning',
              duration: 2000
            })
          } else {
            this.$message({
              message: '服务器连接失败，请检查网络',
              type: 'error',
              duration: 2000
            })
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    resetForm() {
      this.member.account = ''
      this.member.password = ''
    }
  }
}
</script>

<style scoped>
/* ===== 容器 & 背景 ===== */
.login-container {
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url(./assets/img/豆包.png);
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  overflow: hidden;
}

.login-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    135deg,
    rgba(26, 35, 50, 0.75) 0%,
    rgba(46, 171, 209, 0.55) 100%
  );
  z-index: 1;
}

/* ===== 左侧品牌区 ===== */
.login-brand {
  position: relative;
  z-index: 2;
  margin-right: 80px;
  text-align: center;
  animation: brandFadeIn 1s ease;
}
@keyframes brandFadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.brand-icon {
  width: 100px;
  height: 100px;
  margin: 0 auto 20px;
  border-radius: 50%;
  padding: 4px;
  background: linear-gradient(135deg, #fff, rgba(255, 255, 255, 0.6));
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  animation: pulse 2.5s ease-in-out infinite;
}
.brand-icon img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}
@keyframes pulse {
  0%,
  100% {
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  }
  50% {
    box-shadow: 0 8px 48px rgba(46, 171, 209, 0.5);
  }
}
.brand-title {
  font-size: 36px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 8px;
  letter-spacing: 2px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}
.brand-subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.85);
  margin: 0;
  letter-spacing: 1px;
}

/* ===== 登录卡片 ===== */
.login-card {
  position: relative;
  z-index: 2;
  width: 420px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 40px 36px 30px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(10px);
  animation: cardSlideUp 0.8s ease;
}
@keyframes cardSlideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 卡片头部 */
.card-header {
  text-align: center;
  margin-bottom: 30px;
}
.card-avatar {
  width: 72px;
  height: 72px;
  margin: 0 auto 14px;
  border-radius: 50%;
  padding: 3px;
  background: linear-gradient(135deg, #2eabd1, #3cc6e8);
  box-shadow: 0 4px 16px rgba(46, 171, 209, 0.35);
}
.card-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: #fff;
  padding: 4px;
}
.card-title {
  font-size: 22px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 6px;
}
.card-desc {
  font-size: 13px;
  color: #909399;
  margin: 0;
}

/* 表单 */
.login-form {
  margin-top: 6px;
}
.login-form >>> .el-input__prefix {
  left: 12px;
}
.login-form >>> .el-input__inner {
  padding-left: 38px;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
}
.login-form >>> .el-input__inner:hover {
  border-color: #2eabd1;
}
.login-form >>> .el-input__inner:focus {
  border-color: #2eabd1;
  box-shadow: 0 0 0 3px rgba(46, 171, 209, 0.12);
}

/* 按钮 */
.login-actions {
  margin-top: 28px !important;
  margin-bottom: 0 !important;
}
.login-actions >>> .el-form-item__content {
  display: flex;
  gap: 12px;
}
.btn-login {
  flex: 1;
  height: 44px;
  font-size: 16px;
  letter-spacing: 2px;
  border-radius: 8px;
  background: linear-gradient(135deg, #2eabd1, #3cc6e8) !important;
  border: none !important;
  transition: all 0.3s ease;
}
.btn-login:hover {
  box-shadow: 0 6px 20px rgba(46, 171, 209, 0.4);
  transform: translateY(-2px);
}
.btn-reset {
  width: 100px;
  height: 44px;
  border-radius: 8px;
  border: 1px solid #dcdfe6;
  color: #606266;
  transition: all 0.3s ease;
}
.btn-reset:hover {
  border-color: #2eabd1;
  color: #2eabd1;
}

/* 卡片底部 */
.card-footer {
  text-align: center;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}
.footer-hint {
  font-size: 12px;
  color: #c0c4cc;
  cursor: default;
}

/* ===== 底部版权 ===== */
.login-copyright {
  position: absolute;
  bottom: 24px;
  z-index: 2;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
  letter-spacing: 0.5px;
}

/* ===== 响应式 ===== */
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }
  .login-brand {
    margin-right: 0;
    margin-bottom: 30px;
  }
  .brand-title {
    font-size: 26px;
  }
  .brand-icon {
    width: 70px;
    height: 70px;
  }
  .login-card {
    width: 90%;
    padding: 30px 24px 24px;
  }
}
</style>
