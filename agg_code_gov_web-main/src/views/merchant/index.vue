<template>
  <div class="container">
    <div class="middle">
      <div class="flex">
        <div class="w_input">
          <span class="textSpan"> 店铺名称</span>
          <el-input
            v-model="body.storeName"
            placeholder="请输入店铺名称"
            size="mini"
            style="width:65%"
            clearable
            @change="handleSearch"
          />
        </div>
        <div class="w_input">
          <span class="textSpan">手机号</span>
          <el-input
            v-model="body.operatorPhone"
            oninput="value=value.replace(/[^0-9.]/g,'')"
            size="mini"
            style="width:65%"
            maxlength="11"
            show-word-limit
            @change="handleSearch"
            clearable
          />
        </div>
        <div class="w_input">
          <span class="textSpan"> 法人姓名</span>
          <el-input
            v-model="body.operatorName"
            placeholder="请输入法人姓名"
            size="mini"
            maxlength="11"
            minlength="11"
            style="width:65%"
            clearable
            @change="handleSearch"
          />
        </div>
      </div>
      <div class="flex">
        <div class="w_input">
          <span class="textSpan"> 监管所</span>
          <el-select
            v-model="body.supervisionId"
            size="mini"
            style="width:65%"
            clearable
            placeholder="请选择监管所"
            @change="handleSearch"
            @clear="handleSearch"
          >
            <el-option
              v-for="item in supervisionLists"
              :key="item.supervisionId"
              :label="item.supervisionName"
              :value="item.supervisionId"
            />
          </el-select>
        </div>
        <div class="w_input">
          <span class="textSpan"> 行业分类</span>
          <el-select
            v-model="body.industryType"
            size="mini"
            style="width:65%"
            clearable
            placeholder="请选择行业分类"
            @change="handleSearch"
          >
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.value"
              :value="item.id"
            />
          </el-select>
        </div>
        <div class="w_input" v-show="isSpread == true">
          <span class="textSpan"> 经营场所</span>
          <el-input
            v-model="body.address"
            placeholder="请输入经营场所"
            size="mini"
            style="width:65%"
            clearable
            @change="handleSearch"
          />
        </div>
        <div v-show="isSpread == false" class="w_input">
          <el-button size="mini" type="text" class="button" @click="spread">
            展开<i class="el-icon-caret-bottom"
          /></el-button>
          <el-button size="mini" class="button" @click="resetBody"
            >重置</el-button
          >
          <el-button
            size="mini"
            type="primary"
            class="button"
            @click="handleSearch"
            >查询</el-button
          >
        </div>
      </div>
      <div v-show="isSpread == true" class="flex">
        <div class="w_input">
          <span class="textSpan"> 收款银行</span>
          <el-select
            v-model="body.bankType"
            size="mini"
            style="width:65%"
            clearable
            placeholder="请选择收款银行"
            @change="handleSearch"
          >
            <el-option
              v-for="item in bankLists"
              :key="item.bankCode"
              :label="item.bankName"
              :value="item.bankCode"
            />
          </el-select>
        </div>
        <div class="w_input">
          <span class="textSpan"> 注册时间</span>
          <el-date-picker
            v-model="createTime"
            type="daterange"
            size="mini"
            style="width:65%"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="selectTime"
            value-format="yyyy-MM-dd"
          />
        </div>
        <div class="w_input">
          <el-button size="mini" type="text" class="button" @click="spread">
            收回<i class="el-icon-caret-top"
          /></el-button>
          <el-button size="mini" class="button" @click="resetBody"
            >重置</el-button
          >
          <el-button
            size="mini"
            type="primary"
            class="button"
            @click="handleSearch"
            >查询</el-button
          >
        </div>
      </div>
    </div>
    <div class="footer">
      <span>总共商户数：{{ total }}家</span>
      <!-- <el-button
        style="float:right;margin:0px 10px 10px 10px"
        size="mini"
        type="primary"
        :loading="exportButtonLoading"
        @click="exportOut"
      >
        <svg-icon icon-class="daochu" style="width:13px;height:13px;" />
        导出</el-button
      > -->
      <!-- 列表 table -->
      <div class="table-div" style="padding-top:10px">
        <el-table
          ref="multipleTable"
          v-loading="loading"
          :data="tableData"
          style="width: 100%"
          tooltip-effect="dark"
          :header-cell-style="{ background: '#F5F6F8' }"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <!-- <el-table-column
            label="序号"
            width="60"
            align="center"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column> -->
          <el-table-column
            label="放心码ID"
            min-width="80"
            align="center"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{ scope.row.merchantId }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="店铺名称"
            min-width="200"
            align="center"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{ scope.row.storeName }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="法人姓名"
            min-width="120"
            align="center"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{ scope.row.representName }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="手机号"
            min-width="200"
            align="center"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{ scope.row.operatorPhone }}</span>
            </template>
          </el-table-column>

          <el-table-column 
          label="监管所" 
          align="center" 
          min-width="180"
          show-overflow-tooltip>
            <template slot-scope="scope">
              <span>{{ scope.row.supervisionName }}</span>
            </template>
          </el-table-column>

          <el-table-column
            label="经营场所"
            align="center"
            min-width="160"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{ scope.row.address }}</span>
            </template>
          </el-table-column>

          <el-table-column
            label="注册时间"
            align="center"
            min-width="200"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <!-- <span>{{ scope.row.createTime.slice(0, 10) }}</span> -->
               <span>{{ scope.row.createTime }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="收款银行"
            align="center"
            min-width="120"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span>{{ scope.row.bankName }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          current-page.sync="body.current"
          :current-page="body.page"
          :page-sizes="[10, 20, 30, 50]"
          :page-size="body.size"
          layout="total, sizes, prev, pager, next, jumper"
          background
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>
<script>
import { merchList, superList, bankList } from "@/api/merchant.js";
import { getToken } from "@/utils/auth";
import axios from "axios";
export default {
  components: {},
  data() {
    return {
      userNumber: "500008",
      userList: [],
      body: {
        page: 1,
        size: 10,
        storeName: "",
        operatorName: "", //法人姓名
        operatorPhone: "",
        bankType: "", // 银行类型
        supervisionId: "", // 监管所ID
        address: "", // 街道/市场
        startTime: "",
        endTime: "",
        industryType: "" //行业分类
      },
      tableData: [],
      supervisionLists: [],
      bankLists: [],
      options: [
        {
          id: 1,
          value: "餐饮"
        },
        {
          id: 2,
          value: "流通"
        }
      ],
      total: 0,
      loading: "", //列表loading
      isSpread: true,
      createTime: "",
      recordIds: [],
      exportButtonLoading: false //导出按钮
    };
  },
  mounted() {
    this.getTenantList();
    this.bankSearch();
    this.supervisionSearch();
  },
  methods: {
    getTenantList() {
      this.loading = true;
      const body = JSON.parse(JSON.stringify(this.body));
      merchList(body).then(res => {
        this.loading = false;
        console.log(res.data.data.records, "tableData");
        this.tableData = res.data.data.records;
        this.total = res.data.data.total;
        // this.recordIds=this.tableData.map(res=>{
        //   console.log(res.merchantId,"rowid")
        //   return res.merchantId
        // })
        console.log(this.recordIds, "11111");
      });
    },
    // 搜索
    handleSearch() {
      this.body.page = 1;
      if (this.createTime == null) {
        // 发送时间为空时清空 body 里的发送时间
        this.body.startTime = "";
        this.body.endTime = "";
      }
      this.getTenantList();
    },
    //银行
    bankSearch() {
      bankList().then(res => {
        this.loading = false;
        const bankLists = res.data.data;
        console.log(res.data.data, "银行");
        this.bankLists = bankLists;
      });
    },
    //监管所
    supervisionSearch() {
      superList().then(res => {
        this.loading = false;
        const supervisionLists = res.data.data;
        this.supervisionLists = supervisionLists;
      });
    },
    // 导出
    exportOut() {
      this.exportButtonLoading = true;
      const recordIds = this.recordIds;
      const params = new URLSearchParams(this.body);
      params.append("recordIds", recordIds);
      axios
        .get("/gov/merchant/export?" + params.toString(), {
          responseType: "blob", // 或者responseType: 'arraybuffer'
          xsrfHeaderName: "Authorization",
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + getToken()
          }
        })
        .then(res => {
          this.exportButtonLoading = false;
          const blob = new Blob([res.data], {
            type: "application/vnd.ms-excel"
          });
          const fileName = res.headers["content-disposition"]
            .split("filename=")
            .pop();
          // 文件名解码
          const temp = decodeURI(fileName);
          const objectUrl = URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.style.display = "none";
          link.href = objectUrl;
          link.download = temp;
          document.body.appendChild(link);
          link.click();
          this.$message({
            type: "success",
            message: "文件导出成功"
          });
        })
        .catch(err => {
          this.exportButtonLoading = false;
          // this.$message({
          //   type: "error",
          //   message: err
          // });
        });
    },
    // 选择时间
    selectTime() {
      console.log(this.createTime, "this.createTime");
      if (this.createTime !== null) {
        this.body.startTime = this.createTime[0] + " " + "00:00:00";
        this.body.endTime = this.createTime[1] + " " + "23:59:59";
      }
      this.handleSearch();
    },
    // 重置
    resetBody() {
      this.body = {};
      this.createTime = "";
      this.handleSearch();
    },
    //多选
    handleSelectionChange(row) {
      console.log(row, "row");
      this.recordIds = row.map(res => {
        return res.merchantId;
      });
      console.log(this.recordIds, "this.recordIds");
    },
    // 分页
    handleSizeChange(val) {
      this.body.size = val;
      this.getTenantList();
    },
    handleCurrentChange(val) {
      this.body.page = val;
      this.getTenantList();
    },
    // 展开
    spread() {
      if (this.isSpread == true) {
        this.isSpread = false;
      } else {
        this.isSpread = true;
      }
    }
  }
};
</script>

<style scoped rel="stylesheet/scss" lang="scss">
.container {
  background: #ececec8e;
  padding: 10px;
}
.number {
  font-size: 32px;
  font-weight: 550;
  padding: 15px 15px 0 30px;
}
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
.w_input1 {
  width: 66.66%;
}
.textSpan {
  display: inline-block;
  width: 18%;
  font-size: 12px;
  font-weight: 600;
  color: rgb(87, 86, 86);
  text-align: right;
}
.button {
  float: right;
  margin-left: 20px;
}
.button:first-child {
  margin-right: 30px;
}
.footer {
  background: #fff;
  width: 100%;
  margin-top: 10px;
  padding: 16px;
}
</style>
