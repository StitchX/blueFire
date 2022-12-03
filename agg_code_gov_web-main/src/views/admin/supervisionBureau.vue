<template>
  <div class="dept">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="12">
        <div class="app-container">
          <div class="filter-container">
            <el-button-group>
              <el-button type="primary" plain @click="handleAdd">
                添加
              </el-button>
              <el-button type="primary" plain @click="handleDelete">
                删除
              </el-button>
            </el-button-group>
          </div>
          <el-tree
            :data="tableTreeData"
            :props="popupTreeProps"
            :default-expand-all="true"
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <el-col :xs="24" :sm="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ deptForm.addressId === '' ? '添加' : '编辑' }}</span>
          </div>
          <div>
            <el-form ref="deptForm" :model="deptForm" :rules="dataRule" label-position="right" label-width="100px">
              <el-form-item label="上级编号" prop="supervisionId">
                <popup-tree-input
                  :data="popupTreeData"
                  :props="popupTreeProps"
                  :prop="deptForm.supervisionName == null?'顶级菜单':deptForm.supervisionName"
                  :node-key="''+deptForm.parentSupervisionId"
                  :current-change-handle="handleTreeSelectChange"
                />
              </el-form-item>
              <el-form-item label="监管名称">
                <el-input v-model="deptForm.name" />
              </el-form-item>
              <el-form-item label="所属区域编码" prop="sort">
                <el-select v-model="deptForm.province" style="width:33%" placeholder="省" @change="cityCode">
                  <el-option
                    v-for="item in options"
                    :key="item.code"
                    :label="item.name"
                    :value="item.code"
                  />
                </el-select>
                <el-select v-model="deptForm.city" style="width:33%" placeholder="市" @change="areaCode">
                  <el-option
                    v-for="item in options1"
                    :key="item.code"
                    :label="item.name"
                    :value="item.code"
                  />
                </el-select>
                <el-select v-model="deptForm.area" style="width:32%" placeholder="区" @change="endCode">
                  <el-option
                    v-for="item in options2"
                    :key="item.code"
                    :label="item.name"
                    :value="item.code"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="联系人电话">
                <el-input v-model="deptForm.phone" />
              </el-form-item>
            </el-form>
          </div>
        </el-card>
        <el-card class="box-card" style="margin-top: -2rem;">
          <el-row>
            <el-col :span="24" style="text-align: right">
              <el-button type="primary" plain :loading="editLoading" @click="submitEditForm">{{ deptForm.addressId == '' ?
                '添加' : '编辑' }}
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import PopupTreeInput from '@/components/PopupTreeInput'
import { saveDept, getDept, updateDept, deleteDept, getArea } from '@/api/dept'
import { parseTime } from '@/utils/index'

export default {
  components: { PopupTreeInput },
  data() {
    return {
      deptForm: {
        name: '',
        supervisionId: 0,
        supervisionName: '',
        addressId: 0,
        area: ''
      },
      tableTreeData: [],
      // 表单校验
      dataRule: {
        name: [
          { required: true, message: '机构名称不能为空', trigger: 'blur' }
        ],
        supervisionName: [
          { required: true, message: '上级机构不能为空', trigger: 'change' }
        ]
      },
      popupTreeData: [],
      popupTreeProps: {
        label: 'supervisionName',
        children: 'childSupervisionBureau'
      },
      editLoading: false,
      deptName: '',
      options: [],
      options1: [],
      options2: [],
      value: ''
    }
  },
  created() {
    this.findTreeData()
    this.provinceCode()
  },
  methods: {
    parseTime,
    handleNodeClick(data) {
      console.log(data)
      this.deptForm = data
      this.$refs.deptForm.clearValidate()
    },
    endCode(item) {
      console.log(item)
    },
    // 区级
    areaCode(item) {
      const code = {
        parentCode: item
      }
      this.addressCode(code).then((res) => {
        this.options2 = []
        this.deptForm.area = ''
        this.options2 = res
      })
    },
    // 省级下拉 ==》 市级
    cityCode(item) {
      const code = {
        parentCode: item
      }
      this.addressCode(code).then((res) => {
        // 清空区级
        this.options2 = []
        this.options1 = []
        this.deptForm.city = ''
        this.deptForm.area = ''
        this.options1 = res
      })
    },
    // 省级
    provinceCode() {
      const code = {
        parentCode: 0
      }
      this.addressCode(code).then((res) => {
        // 清空区级和市级
        this.options2 = []
        this.options1 = []
        this.deptForm.city = ''
        this.deptForm.area = ''
        this.options = res
      })
    },
    async addressCode(code) {
      const { data: data } = await getArea(code)
      return data.data
    },
    handleFind: function() {
      this.findTreeData()
    },
    handleNumChange(val) {
      this.deptForm.sort = val
    },
    // 显示新增界面
    handleAdd: function() {
      if (this.deptForm.addressId === 0) {
        this.$message({
          message: '请先选择一个节点哦',
          type: 'warning'
        })
      } else {
        const supervisionId = this.deptForm.supervisionId
        const supervisionName = this.deptForm.supervisionName
        this.deptForm = {
          name: '',
          supervisionId: supervisionId,
          supervisionName: supervisionName,
          sort: 0
        }
      }
    },
    // 获取数据
    findTreeData: function() {
      this.loading = true
      getDept().then(res => {
        this.tableTreeData = res.data.data
        this.popupTreeData = this.getParentMenuTree(res.data.data)
        this.loading = false
      })
    },
    // 获取上级机构树
    getParentMenuTree: function(tableTreeDdata) {
      const parent = {
        supervisionId: 0,
        supervisionName: '顶级菜单',
        childSupervisionBureau: tableTreeDdata
      }
      return [parent]
    },
    // 机构树选中
    handleTreeSelectChange(data) {
      console.log(data)
      this.deptForm.supervisionId = data.supervisionId
      this.deptForm.supervisionName = data.supervisionName
    },
    submitEditForm: function() {
      const deptForm = JSON.parse(JSON.stringify(this.deptForm))
      console.log(deptForm)
      // province 省
      // city     市
      // area     区

      this.$refs['deptForm'].validate((valid) => {
        if (valid) {
          if (deptForm.addressId) {
            console.log('我是编辑')
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.editLoading = true
              if (deptForm.province != '' && deptForm.city != '' && deptForm.area != '') {
                console.log('都不是空得')
                delete deptForm.province
                delete deptForm.city
                deptForm.addressId = deptForm.area
                delete deptForm.area
              } else if (deptForm.province != '' && deptForm.city != '') {
                console.log('区是空得')
                delete deptForm.province
                deptForm.addressId = deptForm.city
              } else if (deptForm.province != '') {
                deptForm.addressId = deptForm.province
              }
              updateDept(deptForm).then((res) => {
                if (res.data.status == '200') {
                  this.$message({ message: '操作成功', type: 'success' })
                } else {
                  this.$message({ message: res.data.msg, type: 'error' })
                }
                this.editLoading = false
                this.$refs.deptForm.clearValidate()
                this.findTreeData()
              })
            })
          } else {
            console.log('我是添加')
            console.log(this.deptForm)
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.editLoading = true
              if (deptForm.province != '' && deptForm.city != '' && deptForm.area != '') {
                console.log('都不是空得')
                delete deptForm.province
                delete deptForm.city
                deptForm.addressId = deptForm.area
                delete deptForm.area
              } else if (deptForm.province != '' && deptForm.city != '') {
                console.log('区是空得')
                delete deptForm.province
                deptForm.addressId = deptForm.city
              } else if (deptForm.province != '') {
                deptForm.addressId = deptForm.province
              }
              saveDept(deptForm).then((res) => {
                if (res.data.status == '200') {
                  this.$message({ message: '操作成功', type: 'success' })
                } else {
                  this.$message({ message: res.data.msg, type: 'error' })
                }
                this.editLoading = false
                this.$refs.deptForm.clearValidate()
                this.findTreeData()
              })
            })
          }
        }
      })
    },
    // 删除
    handleDelete: function() {
      const that = this
      if (that.deptForm.deptId === 0) {
        this.$message({
          message: '请先选择一个部门哦',
          type: 'warning'
        })
      } else {
        this.$confirm('此操作将把部门删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            deleteDept(this.deptForm.deptId).then(response => {
              if (response.data.status == '200') {
                this.$message({
                  type: 'success',
                  message: '操作成功'
                })
                that.findTreeData()
              } else {
                this.$message({ message: res.data.msg, type: 'error' })
              }
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
          })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .dept {
    margin: 10px;

    .app-container {
      margin: 0 0 10px 0 !important;
    }
  }

  .el-card.is-always-shadow {
    box-shadow: none;
  }

  .el-card {
    border-radius: 0;
    border: none;

    .el-card__header {
      padding: 10px 20px !important;
      border-bottom: 1px solid #f1f1f1 !important;
    }
  }
</style>
