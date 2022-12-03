<template>

  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;" />
    <el-table v-loading="loading" :header-cell-style="{ background: '#FAFAFA',color: '#262626' }" :data="tableData" style="width: 100%">
      <!-- <el-table-column label="放心吗ID" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column> -->

      <el-table-column label="省级" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.province }}</span>
        </template>
      </el-table-column>

      <el-table-column label="市级" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.city }}</span>
        </template>
      </el-table-column>

      <el-table-column label="区级" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.area }}</span>
        </template>
      </el-table-column>
      <el-table-column label="监管所" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.supervisionName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="监管所账号" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.username }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="监管员" width="160" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.roleDesc }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号"  align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.roleDesc }}</span>
        </template>
      </el-table-column> -->

      <el-table-column label="创建时间" width="160" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" min-width="150" align="center">
        <template slot-scope="scope">
          <!--<el-button @click="handRest(scope.row)" type="warning" size="small">重置密码</el-button>-->
          <el-button size="small" type="text" @click="handleEdit(scope.row)">修改密码</el-button>
        </template>
      </el-table-column>

    </el-table>

    <!-- 添加或修改对话框 -->
    <el-dialog title="修改密码" :close-on-click-modal="false" :visible.sync="dialogFormVisible" width="40%">
      <el-alert
        class="elAlert"
        title="温馨提示：密码由字母、数字组成、特殊符号中的两种以上组成，长度必须大于8位"
        type="warning"
        :closable="false"
      />
      <el-form ref="dataForm" :model="dataForm" :rules="rules2" label-width="100px" size="small" label-position="right">

        <el-form-item label="新的密码" :label-width="formLabelWidth" prop="password" required>
          <el-input onkeyup="value=value.replace(/[\u4E00-\u9FA5]/g,'')" v-model="dataForm.password" show-password auto-complete="off" style="width:90%" placeholder="请输入新的密码" />
        </el-form-item>

        <el-form-item label="确认密码" :label-width="formLabelWidth" prop="checkPass" required>
          <el-input v-model="dataForm.checkPass" show-password auto-complete="off" style="width:90%" placeholder="请再一次输入新密码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">保 存</el-button>
      </div>
    </el-dialog>
    <!--分页-->
    <div class="pagination">
      <el-pagination current-page.sync="currentPage" :current-page="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" background :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

  </div>
</template>

<script>
import { getList, updateList, adminUserList } from '@/api/overseeList'

export default {

  data() {
    var validatePass = (rule, value, callback) => {
      // var regex = new RegExp('^(?=.{8,10})(((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$')
      var regex = new RegExp(/^(((?=.*[0-9])(?=.*[a-zA-Z])|(?=.*[0-9])(?=.*[^\s0-9a-zA-Z])|(?=.*[a-zA-Z])(?=.*[^\s0-9a-zA-Z]))[^\s]+)$/)
      var info=new RegExp(/[\u4e00-\u9fa5/\s+/]/ig,'')

      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value.length < 9) {
        callback(new Error('密码必须大于八位'))
      } else if (!regex.test(value)) {
        callback(new Error('密码由字母、数字组成、特殊符号中的两种以上组成，长度必须大于8位'))
      }else if(!regex.test(info)){
        callback(new Error('密码由字母、数字组成、特殊符号中的两种以上组成，长度必须大于8位'))
      }else {
        if (this.dataForm.checkPass !== '') {
          this.$refs.dataForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.dataForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      tableData: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0, // 总数量
      deptIds: [],
      dataForm: {
        checkPass: '',
        password: ''
      },
      formLabelWidth: '120px',
      dialogFormVisible: false,
      rules2: {
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }, { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { required: true, message: '请确认密码', trigger: 'blur' }, { validator: validatePass2, trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    getDataList() {
      this.loading = true
      const params = {
        current: this.currentPage,
        size: this.pageSize
      }
      adminUserList(params).then(response => {
        this.loading = false
        this.tableData = response.data.data.records
        this.total = response.data.data.total
      })
    },
    // 编辑
    handleEdit: function(row) {
      this.dataForm.userId = row.userId
      this.dialogFormVisible = true
      this.dataForm.checkPass = ''
      this.dataForm.password = ''
      setTimeout(() => {
        this.$refs['dataForm'].clearValidate()
      }, 100)
    },
    submitForm() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 删除确认密码
          // delete this.dataForm.checkPass
          updateList(this.dataForm).then((res) => {
            this.$message({
              type: 'success',
              message: '密码修改成功'
            })
            this.dialogFormVisible = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // 页码
    handleSizeChange(val) {
      this.pageSize = val
      this.getDataList()
    },
    // 换页
    handleCurrentChange: function(val) {
      this.currentPage = val
      this.getDataList()
    }
  }
}
</script>

<style scoped>
 .show-enter-active{
    width: 20px;
    transition: all .2s ease;
  }
   .show-leave-active{
    width: 20px;
    transition: all .2s ease;
  }
  .show-enter, .show-leave-to{
    /* transform: translateZ(-10px); */
    opacity: 0;
    /* height: 0; */
  }
  .elAlert{
    position: relative;
    top: -20px;
  }
</style>
