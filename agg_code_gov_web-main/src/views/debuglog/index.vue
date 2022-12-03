<template>

  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;">
    <div class="middle">
      <div class="flex">
        <div class="w_input">
          <span class="textSpan"> 操作人</span>
          <el-input
            placeholder="请输入操作人"
            v-model="query.name"
            size="mini"
            style="width:65%"
            clearable
            @change="handleSearch"
          >
          </el-input>
        </div>
        <div class="w_input">
          <span class="textSpan"> 操作时间</span>
          <el-date-picker
            v-model="createTime"
            type="daterange"
            size="mini"
            style="width:73%"
            range-separator="-"
             value-format="yyyy-MM-dd HH:mm:ss"
            :default-time="['00:00:00', '23:59:59']"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleSearch"
          >
          </el-date-picker>
        </div>
        <div class="w_input">
          <el-button size="mini" type="primary" class="button" @click="handleSearch">查询</el-button>
          <el-button size="mini" @click="delectSearch" class="button">重置</el-button>
        </div>
      </div>
    </div>
    </div>
    <el-table v-loading="loading" :header-cell-style="{ background: '#FAFAFA',color: '#262626' }" :data="tableData"  style="width: 100%">
      <el-table-column label="操作人" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作内容"  align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.content }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作时间" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.operateTime }}</span>
        </template>
      </el-table-column>

    </el-table>

    <!--分页-->
    <div class="pagination">
      <!-- <el-pagination
        :current-page.sync="currentPage"
        :page-size="pageSize"
        layout="total, prev, pager, next, jumper"
        :total="total"
        @current-change="handleCurrentChange"
      /> -->
      <el-pagination current-page.sync="currentPage" :current-page="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" background :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

  </div>
</template>

<script>
import { getList } from '@/api/debuglog'

export default {

  data() {
    return {
      tableData: [],
      loading: false,
      query:{
       
      },
      createTime:[],
      currentPage: 1,
      pageSize: 10,
      total: 0, // 总数量
    }
  },
  created() {
      this.getDataList()
  },
  methods:{
    getDataList(){
      this.loading = true
      let params = {
        current:this.currentPage,
        size:this.pageSize,
        startTime:this.query.startTime,
        endTime:this.query.endTime,
        name:this.query.name
      }
      getList(params).then(response => {
        this.loading = false
        this.tableData = response.data.data.records
        this.total = response.data.data.total
      })
    },
    handleSearch(){
      if(this.createTime != null){
        this.query.startTime = this.createTime[0]
        this.query.endTime = this.createTime[1]
      }else{
        this.query.startTime = ""
        this.query.endTime = ""
      }
      this.getDataList()
    },
    delectSearch(){
      this.query = {}
      this.createTime = []
      this.getDataList()
    },
    // 页码
    handleSizeChange(val){
      this.pageSize = val
      this.getDataList()
    },
    // 换页
    handleCurrentChange: function(val) {
      this.currentPage = val
      this.getDataList()
    },
  }
}
</script>

<style scoped>


/* s搜索 */
.middle {
  background: #fff;
  width: 100%;
  margin-top: 10px;
  padding: 16px 0px;
}
.flex {
  display: flex;
  flex-wrap: nowrap;
  padding: 10px;
}
.w_input {
  width: 33.33%;
}
.textSpan {
  display: inline-block;
  width: 18%;
  font-size: 12px;
  font-weight: 600;
  color: rgb(87, 86, 86);
  text-align: right;
}

/* 表格 */
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
</style>
