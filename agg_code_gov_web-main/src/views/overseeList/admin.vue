<template>

  <div class="app-container">
    <!-- 查询和其他操作 -->
    <div class="filter-container" style="margin: 10px 0 10px 0;" />
    <el-table v-loading="loading" :header-cell-style="{ background: '#FAFAFA',color: '#262626' }" :data="tableData" style="width: 100%">

      <el-table-column label="监管所" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.supervisionName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="监管员" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.supervisionManName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.phoneNum }}</span>
        </template>
      </el-table-column>

    </el-table>

    <!--分页-->
    <div class="pagination">
      <el-pagination current-page.sync="currentPage" :current-page="currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" background :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

  </div>
</template>

<script>
import { adminGetList } from '@/api/overseeList'

export default {

  data() {
    return {
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0 // 总数量
    }
  },
  created() {
    this.getDataList()
  },
  methods: {
    getDataList() {
      this.loading = true
      const params = {
        page: this.currentPage,
        size: this.pageSize
      }
      adminGetList(params).then(response => {
        this.loading = false
        this.tableData = response.data.data.records
        this.total = response.data.data.total
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
</style>
