<template>
  <div>
    <el-dialog
      :title="member.id ? '编辑家庭成员' : '新增家庭成员'"
      :visible.sync="dialogVisible"
      width="520px"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <!-- 标题装饰条 -->
      <div class="dialog-title-bar">
        <span class="dialog-title-icon">{{ member.id ? '✏️' : '➕' }}</span>
        <span>{{ member.id ? '修改成员信息' : '填写新成员信息' }}</span>
      </div>

      <el-form ref="form" :model="member" label-width="90px" class="member-form">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="账号">
              <el-input v-model="member.account" placeholder="请输入账号" prefix-icon="el-icon-user"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名">
              <el-input v-model="member.name" placeholder="请输入姓名" prefix-icon="el-icon-edit-outline"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="电话">
              <el-input v-model="member.phone" placeholder="请输入电话" prefix-icon="el-icon-phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-radio-group v-model="member.gender">
                <el-radio label="男" border size="small">👨 男</el-radio>
                <el-radio label="女" border size="small">👩 女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="血型">
              <el-select v-model="member.bloodType" placeholder="请选择血型" style="width: 100%">
                <el-option label="🅰️ A型" value="A型"></el-option>
                <el-option label="🅱️ B型" value="B型"></el-option>
                <el-option label="🅾️ O型" value="O型"></el-option>
                <el-option label="🆎 AB型" value="AB型"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生日">
              <el-date-picker
                v-model="member.birthday"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择日期"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="既往病史">
          <el-input
            v-model="member.illnessHistory"
            type="textarea"
            :rows="2"
            placeholder="请输入既往病史"
          ></el-input>
        </el-form-item>
        <el-form-item label="过敏史">
          <el-input
            v-model="member.allergy"
            type="textarea"
            :rows="2"
            placeholder="请输入过敏史"
          ></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveMember()" icon="el-icon-check">
          {{ member.id ? '确认修改' : '保 存' }}
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      member: {
        id: null,
        account: '',
        name: '',
        gender: '',
        phone: '',
        birthday: '',
        bloodType: '',
        illnessHistory: '',
        allergy: ''
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    resetForm() {
      this.member = {
        id: null,
        account: '',
        name: '',
        gender: '',
        phone: '',
        birthday: '',
        bloodType: '',
        illnessHistory: '',
        allergy: ''
      }
    },
    saveMember() {
      if (this.member.id != null) {
        this.$http.put('/memberweb/updateMember', this.member).then(res => {
          if (res.data == '修改成功') {
            this.$message({
              message: '修改成功',
              type: 'success',
              duration: 1000,
              onClose: () => {
                this.dialogVisible = false
                this.$emit('success')
              }
            })
          }
        })
      } else {
        this.$http.post('/memberweb/saveMember', this.member).then(res => {
          if (res.data == '保存成功') {
            this.$message({
              message: '保存成功',
              type: 'success',
              duration: 1000,
              onClose: () => {
                this.dialogVisible = false
                this.$emit('success')
              }
            })
          }
        })
      }
    }
  }
}
</script>

<style scoped>
/* 弹窗标题装饰 */
.dialog-title-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0 16px;
  color: #606266;
  font-size: 13px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 16px;
}
.dialog-title-icon {
  font-size: 16px;
}

/* 表单样式 */
.member-form >>> .el-input__inner,
.member-form >>> .el-textarea__inner {
  border-radius: 6px;
  transition: all 0.3s ease;
}
.member-form >>> .el-radio.is-bordered {
  margin-right: 10px;
  border-radius: 6px;
  transition: all 0.3s ease;
}
.member-form >>> .el-radio.is-bordered.is-checked {
  border-color: #2eabd1;
  background: rgba(46, 171, 209, 0.04);
}

/* 底部按钮 */
.dialog-footer .el-button {
  border-radius: 8px;
  min-width: 90px;
  transition: all 0.3s ease;
}
.dialog-footer .el-button--primary {
  background: linear-gradient(135deg, #2eabd1, #3cc6e8) !important;
  border: none !important;
}
.dialog-footer .el-button--primary:hover {
  box-shadow: 0 4px 12px rgba(46, 171, 209, 0.35);
  transform: translateY(-1px);
}
</style>
