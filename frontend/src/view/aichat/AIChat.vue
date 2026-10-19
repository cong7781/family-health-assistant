<template>
  <div class="aichat-container">
    <!-- 聊天主区域 -->
    <div class="chat-wrapper">
      <!-- 顶部标题栏 -->
      <div class="chat-header">
        <i class="el-icon-s-custom" style="font-size:22px;margin-right:8px;"></i>
        <span>智慧医生</span>
        <span class="chat-subtitle">— AI健康助手，为您提供专业健康咨询</span>
      </div>

      <!-- 消息列表区域 -->
      <div class="chat-messages" ref="messageBox" @scroll="onScroll">
        <!-- 欢迎消息 -->
        <div v-if="messages.length === 0" class="welcome-area">
          <div class="welcome-icon">🩺</div>
          <div class="welcome-text">
            <p style="font-size:18px;font-weight:bold;margin:0 0 10px 0;">您好，我是您的智慧医生</p>
            <p style="margin:5px 0;">我可以帮您：</p>
            <div class="feature-list">
              <span class="feature-tag">📋 健康指标解读</span>
              <span class="feature-tag">💊 常见症状分析</span>
              <span class="feature-tag">🏃 运动饮食建议</span>
              <span class="feature-tag">💡 日常保健指导</span>
            </div>
            <p style="color:#999;font-size:12px;margin-top:15px;">⚠ 本AI仅为健康咨询辅助，不能替代专业医生诊断</p>
          </div>
        </div>

        <!-- 聊天消息 -->
        <div v-for="(msg, index) in messages" :key="index" class="message-row" :class="msg.role">
          <div class="message-avatar">
            <span v-if="msg.role === 'assistant'">🤖</span>
            <span v-else>👤</span>
          </div>
          <div class="message-bubble" :class="msg.role">
            <div class="message-content" v-html="formatContent(msg.content)"></div>
            <div class="message-time">{{ msg.time }}</div>
          </div>
        </div>

        <!-- 加载中 -->
        <div v-if="loading" class="message-row assistant">
          <div class="message-avatar"><span>🤖</span></div>
          <div class="message-bubble assistant typing-bubble">
            <span class="typing-dot"></span>
            <span class="typing-dot"></span>
            <span class="typing-dot"></span>
          </div>
        </div>
      </div>

      <!-- 快捷提问 -->
      <div class="quick-questions" v-if="messages.length === 0">
        <el-button
          v-for="q in quickQuestions"
          :key="q"
          type="info"
          plain
          size="small"
          @click="sendQuick(q)"
        >{{ q }}</el-button>
      </div>

      <!-- 底部输入 -->
      <div class="chat-input-area">
        <el-input
          v-model="inputText"
          type="textarea"
          :rows="2"
          placeholder="请输入您的健康问题..."
          @keydown.native.enter="onEnter"
          :disabled="loading"
        ></el-input>
        <el-button
          type="primary"
          @click="sendMessage"
          :loading="loading"
          :disabled="!inputText.trim()"
          class="send-btn"
        >发 送</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AIChat',
  data() {
    return {
      inputText: '',
      messages: [],
      loading: false,
      quickQuestions: [
        '体温多少度算发烧？',
        '血压偏高应该注意什么？',
        '如何科学控制血糖？',
        '每天应该运动多长时间？',
        '血氧饱和度正常范围是多少？'
      ]
    }
  },
  methods: {
    // 发送消息
    sendMessage() {
      const text = this.inputText.trim()
      if (!text || this.loading) return

      // 添加用户消息
      this.messages.push({
        role: 'user',
        content: text,
        time: this.getTime()
      })
      this.inputText = ''
      this.loading = true

      // 滚动到底部
      this.$nextTick(() => this.scrollToBottom())

      // 构建请求消息列表（role + content）
      const reqMessages = this.messages.map(m => ({
        role: m.role,
        content: m.content
      }))

      // 调用后端AI接口
      this.$http.post('/aichat/chat', { messages: reqMessages })
        .then(res => {
          const data = res.data
          if (data.code === 200) {
            this.messages.push({
              role: 'assistant',
              content: data.content,
              time: this.getTime()
            })
          } else {
            this.messages.push({
              role: 'assistant',
              content: '抱歉，服务出现异常：' + data.content,
              time: this.getTime()
            })
          }
        })
        .catch(() => {
          this.messages.push({
            role: 'assistant',
            content: '网络请求失败，请检查网络连接后重试。',
            time: this.getTime()
          })
        })
        .finally(() => {
          this.loading = false
          this.$nextTick(() => this.scrollToBottom())
        })
    },

    // 快捷提问
    sendQuick(question) {
      this.inputText = question
      this.sendMessage()
    },

    // 回车发送（Ctrl+Enter 换行）
    onEnter(e) {
      if (e.ctrlKey || e.shiftKey) return
      e.preventDefault()
      this.sendMessage()
    },

    // 格式化消息内容（支持简单的markdown换行）
    formatContent(content) {
      if (!content) return ''
      return content
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/\n/g, '<br>')
        .replace(/\*\*(.+?)\*\*/g, '<b>$1</b>')
    },

    // 获取当前时间字符串
    getTime() {
      const now = new Date()
      const h = String(now.getHours()).padStart(2, '0')
      const m = String(now.getMinutes()).padStart(2, '0')
      return h + ':' + m
    },

    // 滚动到底部
    scrollToBottom() {
      const box = this.$refs.messageBox
      if (box) {
        box.scrollTop = box.scrollHeight
      }
    },

    onScroll() {
      // 预留滚动事件，可扩展加载历史记录
    }
  },
  mounted() {
    // 页面打开时自动显示欢迎界面（messages为空即显示欢迎区）
  }
}
</script>

<style scoped>
.aichat-container {
  height: 100%;
  display: flex;
  justify-content: center;
  background: #f5f7fa;
}

.chat-wrapper {
  width: 100%;
  max-width: 800px;
  display: flex;
  flex-direction: column;
  height: calc(100vh - 160px);
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  overflow: hidden;
}

/* ===== 顶部标题栏 ===== */
.chat-header {
  background: linear-gradient(135deg, #2eabd1, #3cc6e8);
  color: #fff;
  padding: 16px 24px;
  font-size: 18px;
  font-weight: bold;
  display: flex;
  align-items: center;
  flex-shrink: 0;
}
.chat-subtitle {
  font-size: 13px;
  font-weight: normal;
  margin-left: 6px;
  opacity: 0.85;
}

/* ===== 消息列表 ===== */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px 24px;
  background: #fafbfc;
}

/* 欢迎区域 */
.welcome-area {
  text-align: center;
  padding: 40px 20px;
}
.welcome-icon {
  font-size: 72px;
  margin-bottom: 16px;
  animation: welcomePulse 3s ease-in-out infinite;
}
@keyframes welcomePulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.05); opacity: 0.85; }
}
.welcome-text {
  color: #555;
  line-height: 1.8;
}
.feature-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
}
.feature-tag {
  background: linear-gradient(135deg, #e8f4fd, #f0f9ff);
  color: #2eabd1;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  border: 1px solid rgba(46, 171, 209, 0.15);
  transition: all 0.3s ease;
  cursor: default;
}
.feature-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(46, 171, 209, 0.15);
  border-color: rgba(46, 171, 209, 0.3);
}

/* 消息行 */
.message-row {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
  animation: fadeIn 0.3s ease;
}
.message-row.user {
  flex-direction: row-reverse;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(8px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 头像 */
.message-avatar span {
  font-size: 32px;
  display: block;
  width: 40px;
  text-align: center;
}

/* 气泡 */
.message-bubble {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 12px;
  position: relative;
  word-break: break-word;
}
.message-bubble.assistant {
  background: #fff;
  border: 1px solid #e8e8e8;
  margin-left: 10px;
  border-top-left-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.message-bubble.user {
  background: linear-gradient(135deg, #2eabd1, #3cc6e8);
  color: #fff;
  margin-right: 10px;
  border-top-right-radius: 4px;
  box-shadow: 0 4px 12px rgba(46, 171, 209, 0.3);
}

/* 消息内容 */
.message-content {
  line-height: 1.6;
  font-size: 14px;
}
.message-content >>> b {
  color: #e74c3c;
}

/* 消息时间 */
.message-time {
  font-size: 11px;
  color: #bbb;
  margin-top: 4px;
  text-align: right;
}
.message-bubble.user .message-time {
  color: rgba(255,255,255,0.7);
}

/* 加载动画 */
.typing-bubble {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 14px 20px;
}
.typing-dot {
  width: 8px;
  height: 8px;
  background: #bbb;
  border-radius: 50%;
  animation: typing 1.4s infinite;
}
.typing-dot:nth-child(2) { animation-delay: 0.2s; }
.typing-dot:nth-child(3) { animation-delay: 0.4s; }
@keyframes typing {
  0%, 60%, 100% { opacity: 0.3; transform: scale(0.8); }
  30% { opacity: 1; transform: scale(1.2); }
}

/* ===== 快捷问题 ===== */
.quick-questions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 12px 24px;
  border-top: 1px solid #eee;
  flex-shrink: 0;
  background: #fafbfc;
}
.quick-questions >>> .el-button--info.is-plain {
  border-radius: 20px;
  transition: all 0.3s ease;
  border-color: #dcdfe6;
  color: #606266;
}
.quick-questions >>> .el-button--info.is-plain:hover {
  border-color: #2eabd1;
  color: #2eabd1;
  background: rgba(46, 171, 209, 0.04);
}

/* ===== 底部输入区 ===== */
.chat-input-area {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  padding: 14px 24px;
  border-top: 1px solid #eee;
  background: #fff;
  flex-shrink: 0;
}
.chat-input-area >>> .el-textarea__inner {
  resize: none;
}
.send-btn {
  height: 40px;
  min-width: 80px;
  border-radius: 8px;
  background: linear-gradient(135deg, #2eabd1, #3cc6e8) !important;
  border: none !important;
  transition: all 0.3s ease;
}
.send-btn:hover {
  box-shadow: 0 4px 14px rgba(46, 171, 209, 0.4);
  transform: translateY(-1px);
}
</style>
