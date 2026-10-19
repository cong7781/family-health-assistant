<template>
  <div>
    <el-card class="box-card record-card">
      <!-- 头部标题 -->
      <div slot="header" class="card-header-custom">
        <div class="header-title-wrap">
          <span class="header-title">健康监测记录管理</span>
          <span class="header-count" v-if="total">共 {{ total }} 条记录</span>
        </div>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          class="btn-add"
          @click="opendialog()"
        >新增健康记录</el-button>
      </div>

      <!-- 表格 -->
      <el-table
        :data="healthList"
        border
        stripe
        style="width: 100%"
        :header-cell-style="{ background: '#f5f7fa', color: '#2c3e50', fontWeight: 600 }"
        empty-text="暂无健康记录，点击上方按钮添加"
      >
        <el-table-column prop="id" label="记录ID" width="80"></el-table-column>
        <el-table-column prop="memberName" label="所属家人" min-width="100">
          <template slot-scope="scope">
            <span class="member-link">
              <i class="el-icon-user" style="margin-right:4px;color:#2eabd1;"></i>
              {{ scope.row.memberName }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="temperature" label="体温(℃)" width="100">
          <template slot-scope="scope">
            <span :class="temperatureClass(scope.row.temperature)">
              {{ scope.row.temperature }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="bpHigh" label="高压" width="100"></el-table-column>
        <el-table-column prop="bpLow" label="低压" width="100"></el-table-column>
        <el-table-column prop="bloodOxygen" label="血氧(%)" width="100"></el-table-column>
        <el-table-column prop="weight" label="体重(kg)" width="100"></el-table-column>
        <el-table-column prop="bloodSugar" label="血糖" width="100">
          <template slot-scope="scope">
            <span :class="sugarClass(scope.row.bloodSugar)">
              {{ scope.row.bloodSugar }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="140" show-overflow-tooltip></el-table-column>
        <el-table-column
          prop="createTime"
          label="记录时间"
          width="170"
          :formatter="formatDateTime"
        ></el-table-column>

        <el-table-column fixed="right" label="操作" width="120">
          <template slot-scope="scope">
            <el-button @click="handleEdit(scope.row)" type="text" size="small" icon="el-icon-edit">编辑</el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-delete"
              style="color: #f56c6c"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrap">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page="page"
          :page-size="size"
          :page-sizes="[5, 10, 20, 50]"
          :total="total"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <HealthAdd ref="healthAdd" @success="loadList"></HealthAdd>
  </div>
</template>

<script>
import HealthAdd from './HealthAdd.vue'
export default {
  components: { HealthAdd },
  data() {
    return {
      healthList: [],
      page: 1,
      size: 10,
      total: 0
    }
  },
  methods: {
    handleDelete(row) {
      this.$confirm('确定删除这条健康记录？删除后不可恢复！', '删除提示', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$http
            .delete('/health/' + row.id)
            .then(() => {
              this.$message.success('删除成功！')
              // 删掉当前页最后一条时回退一页，避免停留在空白页
              if (this.healthList.length === 1 && this.page > 1) {
                this.page -= 1
              }
              this.loadList()
            })
            .catch(() => {
              this.$message.error('删除失败，请重试')
            })
        })
        .catch(() => {
          this.$message.info('已取消删除')
        })
    },
    loadList() {
      this.$http
        .get('/health/all', { params: { page: this.page, size: this.size } })
        .then(res => {
          const data = res.data || {}
          this.healthList = data.records || []
          this.total = data.total || 0
        })
        .catch(() => {
          this.$message.error('健康记录加载失败，请稍后重试')
        })
    },
    handlePageChange(page) {
      this.page = page
      this.loadList()
    },
    handleSizeChange(size) {
      this.size = size
      this.page = 1
      this.loadList()
    },
    opendialog() {
      this.$refs.healthAdd.resetForm()
      this.$refs.healthAdd.dialogVisible = true
    },
    handleEdit(row) {
      this.$http.get('/health/' + row.id).then(res => {
        this.$refs.healthAdd.record = res.data
        this.$refs.healthAdd.dialogVisible = true
      })
    },
    formatDateTime(row, column, cellValue) {
      return cellValue || ''
    },
    // 体温颜色：正常36-37绿色，偏高红色，偏低蓝色
    temperatureClass(val) {
      if (!val) return ''
      if (val >= 37.3) return 'value-danger'
      if (val >= 37) return 'value-warning'
      if (val < 36) return 'value-info'
      return 'value-success'
    },
    // 血糖颜色
    sugarClass(val) {
      if (!val) return ''
      if (val > 7.0 || val < 3.9) return 'value-danger'
      if (val > 6.1) return 'value-warning'
      return 'value-success'
    }
  },
  mounted() {
    this.loadList()
  }
}
</script>

<style scoped>
/* 卡片头部 */
.card-header-custom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.header-title-wrap {
  display: flex;
  align-items: center;
  gap: 12px;
}
.header-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  position: relative;
  padding-left: 14px;
}
.header-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 18px;
  background: linear-gradient(135deg, #2eabd1, #3cc6e8);
  border-radius: 2px;
}
.header-count {
  font-size: 12px;
  color: #909399;
  background: #f0f4f8;
  padding: 2px 10px;
  border-radius: 12px;
}

/* 新增按钮 */
.btn-add {
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}
.btn-add:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(46, 171, 209, 0.35);
}

/* 指标颜色标识 */
.value-success { color: #67c23a; font-weight: 600; }
.value-warning { color: #e6a23c; font-weight: 600; }
.value-danger { color: #f56c6c; font-weight: 600; }
.value-info { color: #2eabd1; font-weight: 600; }

/* 成员链接 */
.member-link {
  color: #2c3e50;
  font-weight: 500;
}

/* 操作按钮 */
.record-card >>> .el-button--text {
  padding: 4px 6px;
  transition: all 0.2s ease;
}
.record-card >>> .el-button--text:hover {
  background: #f0f4f8;
  border-radius: 4px;
}

/* 分页 */
.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
