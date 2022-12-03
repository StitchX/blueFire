<template>
  <div>
    <div class="app-container">
      <span class="textSpan">通知类型</span>
      <el-select v-model="body.noticeType" size="small" style="width:20%" placeholder="请选择">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <span style="marginLeft:20px" class="textSpan">创建时间</span>
      <el-date-picker
        v-model="date"
        size="small"
        type="daterange"
        style="width:30%"
        format="yyyy-MM-dd"
        value-format="yyyy-MM-dd"
        range-separator="~"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
      />
      <div style="float:right">
        <el-button class="filter-item" type="primary" size="small" @click="handleFind">搜索
        </el-button>
        <el-button class="filter-item" size="small" @click="handleReset">重置
        </el-button>
      </div>
    </div>
    <div class="app-container">
      <span style="font-size:15px;">共发送：{{ total }}次</span>
      <!-- 表格 -->
      <el-table v-loading="loading" :data="tableData" border style="width: 100%;margin-top:20px">

        <el-table-column label="通知标题" width="200px" align="center">
          <template slot-scope="scope">
            <el-button type="text" @click.native="noticeLook(scope.row)">{{ scope.row.noticeTitle }}</el-button>
          </template>
        </el-table-column>

        <el-table-column label="是否必读" align="center">
          <template slot-scope="scope">
            <div>{{ scope.row.readFlag == 0 ? "否" : scope.row.readFlag == 1 ? "是" : "" }}</div>
          </template>
        </el-table-column>

        <el-table-column label="阅读率" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.readPercent">{{ scope.row.readPercent }} <span v-if="scope.row.readPercent">%</span> </div>
            <div v-else>0 <span>%</span> </div>
          </template>
        </el-table-column>

        <el-table-column label="通知类型" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.noticeType == 1">政策法规</div>
            <div v-if="scope.row.noticeType == 2">培训学习</div>
            <div v-if="scope.row.noticeType == 3">考试通知</div>
            <div v-if="scope.row.noticeType == 4">开会通知</div>
            <div v-if="scope.row.noticeType == 5">处罚通知</div>
            <div v-if="scope.row.noticeType == 6">信息监管</div>
          </template>
        </el-table-column>

        <el-table-column label="签发单位" align="center">
          <template slot-scope="scope">
            <div>{{ scope.row.issuingAgency }}</div>
          </template>
        </el-table-column>

        <el-table-column label="创建时间" align="center">
          <template slot-scope="scope">
            <div>{{ scope.row.createTime }}</div>
          </template>
        </el-table-column>

        <el-table-column label="状态" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.status == 0">待发送</div>
            <div v-if="scope.row.status == 1">已发送</div>
            <div v-if="scope.row.status == 2">已撤销</div>
          </template>
        </el-table-column>

        <el-table-column label="操作" fixed="right" min-width="100" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status == 1" type="text" size="small" @click="handRest(scope.row)">详情</el-button>
            <!-- <el-button  @click="handRest(scope.row)" type="text" size="small">详情</el-button> -->
            <el-button v-if="scope.row.status != 1" size="small" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.status != 1" size="small" type="text" @click="handleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--分页-->
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          current-page.sync="current"
          :current-page="current"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="size"
          layout="total, sizes, prev, pager, next, jumper"
          background
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    <!-- 详情 -->
    <el-dialog title="通知详情" fullscreen :visible.sync="dialogTableVisible">
      <div class="app-container">
        <span class="textSpan">店铺名称</span>
        <el-input
          v-model="dialogList.storeName"
          placeholder="请输入店铺名称"
          size="mini"
          style="width:25%;"
          clearable
        />
        <span style="marginLeft:20px" class="textSpan">法人姓名</span>
        <el-input
          v-model="dialogList.representName"
          placeholder="请输入法人姓名"
          size="mini"
          style="width:20%;"
          clearable
        />
        <span style="marginLeft:20px" class="textSpan">阅读状态</span>
        <el-select v-model="dialogList.readStatus" size="small" style="width:20%" placeholder="请选择">
          <el-option
            v-for="item in options1"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <div style="display:flex;flex-direction:row;justify-content:flex-end">
          <el-button class="filter-item" type="primary" size="small" @click="handleFindRes">搜索
          </el-button>
          <el-button class="filter-item" size="small" @click="handleResetRes">重置
          </el-button>
        </div>
      </div>
      <div class="app-container">
        <!-- <span style="color:#1C92DC;marginRight:20px">标题：{{ dialogBody.noticeTitle }}</span> -->
            <el-button type="text" style="marginRight:20px" @click.native="noticeLook(dialogBody)">标题：{{ dialogBody.noticeTitle }}</el-button>
        <span>共通知商家：{{ formTotal }}家</span>
        <el-table :data="gridData">
          <el-table-column align="center" property="storeName" label="店铺名称" />
          <el-table-column align="center" property="representName" width="200" label="法人" />
          <el-table-column label="阅读状态" width="200" align="center">
            <template slot-scope="scope">
              <div>{{ scope.row.readStatus == 0 ? "未读" : scope.row.readStatus == 1 ? "已读" : "" }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" property="sendTime" width="300" label="发送时间" />
        </el-table>
        <!--分页-->
        <div class="pagination">
          <el-pagination
            current-page.sync="dialogList.current"
            :current-page="dialogList.current"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="dialogList.size"
            layout="total, sizes, prev, pager, next, jumper"
            background
            :total="formTotal"
            @size-change="formhandleSizeChange"
            @current-change="formhandleCurrentChange"
          />
        </div>
      </div>
    </el-dialog>

    <div class="butnDing">
      <el-dialog
        :visible.sync="centerDialogVisible"
        width="30%"
        center
      >
        <notice :data="dataButnList.content" :titel="dataButnList.noticeTitle" :type="dataButnList.noticetype" :date="dataButnList.createtime" :issuing-agency="dataButnList.issuingAgency" :height="523" :adjunctFile="dataButnList.adjunctFile"/>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import notice from './notice'
import { getList, getnoticeId, getnoticeDelete, getNoticeList } from './../../api/inform'
export default {
  components: { notice },
  data() {
    return {
      body: {},
      current: 1,
      size: 10,
      total: 0, // 总数量
      date: [],
      options: [{
        value: '',
        label: '全部类型'
      }, {
        value: '1',
        label: '政策法规'
      }, {
        value: '2',
        label: '培训学习'
      }, {
        value: '3',
        label: '考试通知'
      }, {
        value: '4',
        label: '开会通知'
      }, {
        value: '5',
        label: '处罚通知'
      }, {
        value: '6',
        label: '监管信息'
      }],
      tableData: [],
      dialogList: {
        current: 1,
        size: 10
      },
      gridData: [],
      dialogTableVisible: false,
      options1: [{
        value: '',
        label: '全部类型'
      }, {
        value: '0',
        label: '未读'
      }, {
        value: '1',
        label: '已读'
      }],
      form: {

      },
      formTotal: 0,
      loading: false,
      centerDialogVisible: false, // 通知标题弹框
      dataButnList: {},
      dialogBody: {}
    }
  },
  created() {
    this.getTenantList()
  },
  activated() {
    this.getTenantList()
  },
  methods: {
    getTenantList() {
      this.body.current = this.current
      this.body.size = this.size
      this.loading = true
      getList(this.body).then((res) => {
        this.loading = false
        this.tableData = res.data.data.records
        this.total = res.data.data.total
      })
    },
    // 标题文字查看
    noticeLook(row) {
      this.centerDialogVisible = true
      console.log(row.noticeId)
      const data = {
        noticeId: row.noticeId
      }
      getnoticeId(data).then((res) => {
        const data = res.data.data
        if (data.noticeType == '1') {
          data.noticetype = '政策法规'
        } else if (data.noticeType == '2') {
          data.noticetype = '培训学习'
        } else if (data.noticeType == '3') {
          data.noticetype = '考试通知'
        } else if (data.noticeType == '4') {
          data.noticetype = '开会通知'
        } else if (data.noticeType == '5') {
          data.noticetype = '处罚通知'
        } else if (data.noticeType == '6') {
          data.noticetype = '监管信息'
        }
        // 处理时间时分秒
        data.createtime = row.createTime.split(' ')[0]
        this.dataButnList = data
      })
    },
    // 通知明细
    handRest(row) {
      this.dialogTableVisible = true
      this.dialogList.noticeId = row.noticeId
      this.getRestApi()
      this.dialogBody = row
      console.log(row)
    },
    // 通知明细api
    getRestApi() {
      getNoticeList(this.dialogList).then((res) => {
        console.log(res)
        this.gridData = res.data.data.records
        this.formTotal = res.data.data.total
      })
    },
    // 通知明细搜索
    handleFindRes() {
      this.dialogList.current = '1'
      this.getRestApi()
    },
    // 通知明细重置
    handleResetRes() {
      this.dialogList = {
        noticeId:this.dialogList.noticeId,
        current: 1,
        size: 10
      }
      this.getRestApi()
    },
    // 通知明细分页
    formhandleSizeChange(value) {
      this.dialogList.size = value
      this.getRestApi()
    },
    // 通知明细页码
    formhandleCurrentChange(value) {
      this.dialogList.current = value
      this.getRestApi()
    },
    // 搜索
    handleFind() {
      // console.log(this.date)
      if (this.date.length > 0) {
        this.body.startTime = this.date[0]+" 00:00:00";
        this.body.endTime = this.date[1]+" 23:59:59";
      }
      this.current = "1"
      this.getTenantList()
    },
    // 重置
    handleReset() {
      this.date = []
      this.body = {}
      this.getTenantList()
    },
    handleEdit(row) {
      console.log(row)
      this.$router.push({ path: '/notice/index', query: { noticeId: row.noticeId }})
    },
    // 删除
    handleDelete(row) {
      this.$confirm('是否确认删除', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          let data = {
            noticeId:row.noticeId,
            noticeTitle:row.noticeTitle
          }
          getnoticeDelete(data).then((res) => {
            this.$message({
              type: 'success',
              message: '操作成功'
            })
            this.getTenantList()
          })
        })
    },
    // 分页
    handleSizeChange(val) {
      this.size = val
      this.getTenantList()
    },
    handleCurrentChange(val) {
      this.current = val
      this.getTenantList()
    }
  }
}
</script>

<style scpoed>
     .textSpan {
        font-size: 12px;
        font-weight: 600;
        color: rgb(87, 86, 86);
        text-align: right;
    }
    .butnDing .el-dialog{
      background: rgba(248, 248, 248, 0);
      border: 0 !important;
      text-align: center;
      margin: 0 atuo !important;
      box-shadow: 0 0px 0px #ccc !important;
      margin-top:0 !important;
    }
   .butnDing .el-dialog__headerbtn .el-dialog__close{
      color: #000 !important;
      position: relative;
      left: 80px;
      font-size: 30px;
    }
   /deep/ .el-dialog--center .el-dialog__body{
      margin: 0 auto;
    }

</style>
