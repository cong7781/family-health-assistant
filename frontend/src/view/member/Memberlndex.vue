<template>
  <div>
    <el-card class="box-card member-card">
      <!-- 头部标题 -->
      <div slot="header" class="card-header-custom">
        <div class="header-title-wrap">
          <span class="header-title">家庭成员信息列表</span>
          <span class="header-count" v-if="members.length">共 {{ members.length }} 人</span>
        </div>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          class="btn-add"
          @click="opendialog()"
        >新增成员</el-button>
      </div>

      <!-- 表格区 -->
      <el-table
        :data="members"
        border
        stripe
        style="width: 100%"
        :header-cell-style="{ background: '#f5f7fa', color: '#2c3e50', fontWeight: 600 }"
        empty-text="暂无家庭成员数据，点击上方按钮添加"
      >
        <el-table-column prop="account" label="账号" min-width="100"></el-table-column>
        <el-table-column prop="name" label="姓名" min-width="80"></el-table-column>
        <el-table-column prop="password" label="密码" min-width="100"></el-table-column>
        <el-table-column prop="gender" label="性别" width="70">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.gender === '男' ? 'primary' : 'danger'"
              size="small"
              effect="plain"
            >{{ scope.row.gender }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" min-width="120"></el-table-column>
        <el-table-column prop="birthday" label="生日" min-width="110"></el-table-column>
        <el-table-column prop="bloodType" label="血型" width="80">
          <template slot-scope="scope">
            <span class="blood-badge">{{ scope.row.bloodType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="illnessHistory" label="病史" min-width="140" show-overflow-tooltip></el-table-column>
        <el-table-column prop="allergy" label="过敏史" min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
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
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <Add ref="add" @success="loadList"></Add>
  </div>
</template>

<script>
import Add from './Add.vue'
export default {
  components: { Add },
  data() {
    return {
      members: []
    }
  },
  methods: {
    handleDelete(row) {
      this.$confirm('确定要删除【' + row.name + '】这条家庭成员数据吗？删除后不可恢复！', '删除提示', {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$http
            .delete('/memberweb/members/' + row.id)
            .then(() => {
              this.$message.success('删除成功！')
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
      this.$http.get('/memberweb/members').then(res => {
        this.members = res.data
      })
    },
    opendialog() {
      this.$refs.add.resetForm()
      this.$refs.add.dialogVisible = true
    },
    handleEdit(row) {
      this.$http.get('/memberweb/member/' + row.id).then(res => {
        this.$refs.add.member = res.data
        this.$refs.add.dialogVisible = true
      })
    },
    formatDateTime(row, column, cellValue) {
      return cellValue || ''
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

/* 血型标识 */
.blood-badge {
  display: inline-block;
  background: #fef0f0;
  color: #e74c3c;
  padding: 2px 10px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 1px;
}

/* 操作按钮 */
.member-card >>> .el-button--text {
  padding: 4px 6px;
  transition: all 0.2s ease;
}
.member-card >>> .el-button--text:hover {
  background: #f0f4f8;
  border-radius: 4px;
}
</style>
