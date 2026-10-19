<template>
  <div>
    <el-dialog
      :title="record.id ? '编辑健康记录' : '新增健康记录'"
      :visible.sync="dialogVisible"
      width="560px"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <!-- 标题装饰条 -->
      <div class="dialog-title-bar">
        <span class="dialog-title-icon">{{ record.id ? '✏️' : '📋' }}</span>
        <span>{{ record.id ? '修改健康指标记录' : '填写健康指标记录' }}</span>
      </div>

      <el-form ref="form" :model="record" label-width="100px" class="health-form">
        <el-form-item label="家庭成员">
          <el-select v-model="record.memberid" placeholder="请选择家人" style="width: 100%">
            <el-option
              v-for="item in memberOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="体温(℃)">
              <el-input v-model.number="record.temperature" placeholder="如 36.5">
                <template slot="suffix">℃</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体重(kg)">
              <el-input v-model.number="record.weight" placeholder="如 65">
                <template slot="suffix">kg</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="高压">
              <el-input v-model.number="record.bpHigh" placeholder="收缩压">
                <template slot="suffix">mmHg</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="低压">
              <el-input v-model.number="record.bpLow" placeholder="舒张压">
                <template slot="suffix">mmHg</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="血氧">
              <el-input v-model.number="record.bloodOxygen" placeholder="如 98">
                <template slot="suffix">%</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="血糖">
              <el-input v-model.number="record.bloodSugar" placeholder="如 5.6">
                <template slot="suffix">mmol/L</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注">
          <el-input
            v-model="record.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息（可选）"
          ></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="saveRecord()" icon="el-icon-check">
          {{ record.id ? '确认修改' : '保 存' }}
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
      memberOptions: [],
      record: {
        id: null,
        memberid: '',
        temperature: null,
        bpHigh: null,
        bpLow: null,
        bloodOxygen: null,
        weight: null,
        bloodSugar: null,
        remark: ''
      }
    }
  },
  mounted() {
    this.$http.get('/memberweb/members').then(res => {
      this.memberOptions = res.data
    })
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    resetForm() {
      this.record = {
        id: null,
        memberid: '',
        temperature: null,
        bpHigh: null,
        bpLow: null,
        bloodOxygen: null,
        weight: null,
        bloodSugar: null,
        remark: ''
      }
    },
    saveRecord() {
      if (this.record.id != null) {
        this.$http.post('/health/update', this.record).then(res => {
          this.$message({
            message: '修改成功',
            type: 'success',
            duration: 1000,
            onClose: () => {
              this.dialogVisible = false
              this.$emit('success')
            }
          })
        }).catch(() => {
          this.$message.error('修改失败')
        })
      } else {
        this.$http.post('/health/save', this.record).then(res => {
          this.$message({
            message: '新增成功',
            type: 'success',
            duration: 1000,
            onClose: () => {
              this.dialogVisible = false
              this.$emit('success')
            }
          })
        }).catch(() => {
          this.$message.error('新增失败')
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
.health-form >>> .el-input__inner,
.health-form >>> .el-textarea__inner {
  border-radius: 6px;
  transition: all 0.3s ease;
}
.health-form >>> .el-input__suffix {
  color: #909399;
  font-size: 12px;
  right: 8px;
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
