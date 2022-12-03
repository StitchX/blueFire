<template>
  <div class="app-container">
    <div>
      <el-row style="display: flex;flex-direction:row;flex-wrap:wrap">
        <el-col :span="7" :lg="9" :xs="24" :sm="12" :md="12">
          <notice :data="Content" :titel="ruleForm.noticeTitle" :type="noticeType" :date="systemTime" :show="ruleForm.readFlag" :height="480"/>
        </el-col>
        <el-col :span="15" :lg="15" :xs="24" :sm="12" :md="12" class="scrollBar">
          <div class="grid-content bg-purple1">
            <span class="grid-text">发布通知</span>
            <el-divider />
            <el-form ref="ruleForm" :model="ruleForm" label-width="75px" class="demo-ruleForm">
              <el-form-item label="通知标题:" prop="desc">
                <el-input v-model="ruleForm.noticeTitle" type="textarea" show-word-limit :rows="3" maxlength="100" placeholder="给目标起个名字" style="width:60%" />
              </el-form-item>
              <el-form-item label="通知类型:" prop="noticeType">
                <el-select v-model="ruleForm.noticeType" style="width:60%" placeholder="请选择活动区域">
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                    @click.native="noticeType = item.label"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="通知范围:" prop="type">
                <el-checkbox-group v-model="ruleForm.noticeScope">
                  <el-checkbox :label="1">全部商家</el-checkbox>
                  <el-checkbox :label="2">学校周边50米</el-checkbox>
                  <el-checkbox :label="3" @change="showhangye = !showhangye">区域、行业选择</el-checkbox>
                  <el-checkbox :label="4" @change="onPrecise">精准选择</el-checkbox>
                </el-checkbox-group>
              </el-form-item>

              <el-form-item>
                <div v-if="showhangye" class="showList">
                  <el-form-item label="选择区域:">
                    <el-checkbox-group v-model="ruleForm.supervisionIds">
                      <!-- <el-checkbox-group v-model="checkList"> -->
                      <el-checkbox v-for="item in areaList" :key="item.supervisionId" :label="item.supervisionId" :checked="item.flag">{{ item.supervisionName }}</el-checkbox>
                    </el-checkbox-group>
                  </el-form-item>
                  <el-form-item label="选择行业:">
                    <el-cascader
                      v-model="ruleForm.categoryIds"
                      :options="optionsTree"
                      size="mini"
                      style="width:100%"
                      :props="props"
                      @change="handleNodeClick"
                    >
                      <!-- <el-cascader
                                    v-model="treeCodeList"
                                    :options="optionsTree"
                                    size="mini"
                                    style="width:100%"
                                     @change="handleNodeClick"
                                    :props="props"
                                    > -->
                    </el-cascader>
                  </el-form-item>
                </div>
                <div v-if="showjingzhun" class="showList">
                  <div> <i class="el-icon-info iconClass" />  精准选择 <span class="iconClass">{{ accurateList.length }}</span>个商户</div>
                  <div class="divStyle">
                    <div v-for="(item,index) in accurateList" v-show="index < 7" :key="item.merchantId" class="spanStyle">
                      <span>{{ item.storeName.substr(0, 5) }} </span>
                      <span v-if="item.storeName.length > 6">...</span>
                      /
                    </div>
                    <el-button size="small" type="text" @click="table = true">修改</el-button>
                  </div>
                </div>
              </el-form-item>

              <el-form-item label="是否必读:">
                <el-radio-group v-model="ruleForm.readFlag">
                  <el-radio :label="1">是</el-radio>
                  <el-radio :label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="通知内容:">
                <div ref="editor" style="text-align:left;z-index:999" />
              </el-form-item>

              <el-form-item label="附件:">
                <el-upload
                  class="upload-demo"
                  action="gov/oss/up"
                  :on-success="handleUp"
                  name="multipartFile"
                 :before-upload="beforeUpload"
                  :before-remove="beforeRemove"
                  multiple
                  :limit="5"
                  :on-exceed="handleExceed"
                  :on-remove="handRemove"
                  :file-list="fileList"
                >
                  <el-button size="small" icon="el-icon-paperclip" type="text">点击上传</el-button>
                </el-upload>

                <!-- <div v-html="Content">{{Content}}</div> -->
              </el-form-item>
              <el-form-item label="发送时间:">
                <el-radio-group v-model="ruleForm.sendType">
                  <el-radio :label="1" @change="onshowdate(1)">立即发送</el-radio>
                  <el-radio :label="2" @change="onshowdate(2) ">定时发送</el-radio>
                </el-radio-group>
                <div v-if="showdate">
                  <el-date-picker
                    v-model="ruleForm.sendTime"
                    size="mini"
                    type="datetime"
                    format="yyyy-MM-dd HH:mm:ss"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择日期时间"
                    @blur="onshowdate(3)"
                  />
                </div>

              </el-form-item>
              <!-- <div>{{Content}}</div> -->
              
            </el-form>
          </div>
        </el-col>
      </el-row>
      <div style="height:40px"></div>
      <div class="bottom">
        <div style="float:right">
            <el-button type="primary" :loading="loadingButon" @click="submitForm('ruleForm')">保存发送</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
        </div>
      </div>
      <el-drawer
        title="精准选择"
        :visible.sync="table"
        :wrapper-closable="false"
        direction="rtl"
        size="70%"
      >
        <span class="textSpan">店铺名/法人姓名/手机号</span>
        <el-input
          v-model="body.otherCondition"
          placeholder="请输入店铺名/法人姓名/手机号"
          size="mini"
          style="width:35%;marginBottom:20px"
          clearable
        />
        <el-button
          size="mini"
          type="primary"
          class="button"
          @click="handleSearch"
        >查询</el-button>
        <el-table
          ref="tableTreeData"
          :data="tableTreeData"
          style="width: 100%;margin-bottom: 20px;"
          :row-key="(row)=>{return row.merchantId}"
          border
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
          @selection-change="handleSelectionChange"
        >
          <el-table-column
            type="selection"
            :reserve-selection="true"
            width="55"
          />
          <el-table-column
            prop="storeName"
            label="店铺名称"
          />
          <el-table-column
            prop="merchant"
            label="法人姓名"
          />
          <el-table-column
            prop="operatorPhone"
            label="手机号"
          />
          <el-table-column
            prop="supervisionName"
            label="监管所"
          />
          <!-- <el-table-column label="图标" align="center">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon" />
        </template>
      </el-table-column> -->
        </el-table>
        <div class="pagination">
          <el-pagination
            current-page.sync="body.current"
            :current-page="body.current"
            :page-sizes="[ 5,10, 20, 30, 50]"
            :page-size="body.size"
            layout="total, sizes, prev, pager, next, jumper"
            background
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
        <div style="margin-top:20px;float:right" class="dialog-footer">
          <el-button type="primary" @click="table = false">确 定</el-button>
          <el-button @click="table = false">取 消</el-button>
        </div>
      </el-drawer>
    </div>
  </div>
</template>

<script>
import notice from './notice'
import { treeList, ossUp, noticeInfo, quyuList, noticeAdd } from './../../api/notice'
import { getnoticeId } from './../../api/inform'
import E from 'wangeditor'
// import Dept from '../admin/dept.vue'
export default {
  components: { notice },
  data() {
    return {
      url: require('./../../assets/head.png'),
      ruleForm: {
        noticeScope: [],
        readFlag: 1,
        noticeType: 1,
        adjunctFile: [],
        supervisionIds: [],
        sendType: 1
      },
      options: [{
        value: 1,
        label: '政策法规'
      }, {
        value: 2,
        label: '培训学习'
      }, {
        value: 3,
        label: '考试通知'
      }, {
        value: 4,
        label: '开会通知'
      }, {
        value: 5,
        label: '处罚通知'
      }, {
        value: 6,
        label: '监管信息'
      }],
      checkList: [],
      Title: '',
      fileList: [],
      Content: '',
      table: false,
      body: {
        otherCondition: '',
        current: 1,
        size: 10
      },
      total: 0,
      value1: '',
      tableTreeData: [],
      props: {
        multiple: true,
        checkStrictly: true,
        emitPath: false,
        value: 'categoryId',
        label: 'categoryName',
        children: 'childenCategoryInfo'
      },
      treeCodeList: [],
      optionsTree: [],
      showhangye: false,
      showjingzhun: false,
      showdate: false,
      noticeType: '', // 通知类型给儿子
      systemTime: '', // 日期给儿子
      areaList: [], // 区域数据
      accurateList: [], // 精准数据多选内容
      editor:"",
      loadingButon:false
    }
  },
  created() {
    // 选择行业树形结构
    this.treePost()
    // 精准选择
    this.noticeInfoList()
    // 区域接口
    this.areaListAPI()
    // 默认值  -- 通知类型
    // this.ruleForm.noticeType = 1
    this.noticeType = '政策法规'
    // 默认值 -- 时间
    // this.ruleForm.sendType = 1
    this.onDatefifler()
    // 默认必读
    // this.ruleForm.readFlag = 1
  },
  activated() {
    this.showhangye = false,
    this.Content = ''
    this.showjingzhun = false,
    this.showdate = false,
    this.ruleForm = {
      noticeScope: [],
      readFlag: 1,
      noticeType: 1,
      adjunctFile: [],
      supervisionIds: [],
      sendType: 1
    }
    this.fileList=[]
    this.editor.txt.clear()
  },
  mounted: function() {
    var That = this
    var editor
    editor = new E(this.$refs.editor)
    this.editor =editor
    //   editor.customConfig = {
    //     onchange:function(html){
    //       That.Content = html
    //       console.log(html)
    //     },
    //     // uploadImgServer: '/gov/oss/up', // 上传图片到服务器
    //     // uploadFileName : 'multipartFile', //后端使用这个字段获取图片信息
    //     // uploadImgMaxLength : 1 , // 限制一次最多上传 1 张图片
    //   }
    editor.config.onchange = function(html) {
      // 第二步，监控变化，同步更新到 textarea
      // $text1.val(html)
      That.Content = html
    }
    //   editor.config.uploadImgAccept = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp']
    editor.config.height = 300,
    editor.config.placeholder = '请输入通知内容'
    editor.config.menus = [
      'bold',
      'italic',
      'underline',
      'strikeThrough',
      'justify',
      // 'emoticon',
      'image',
      'undo',
    //   'insertcode',
    //   "fullscreen"
    ]
editor.config.showFullScreen = false
    // 配置 server 接口地址
    editor.config.uploadImgServer = '/gov/oss/up'
    editor.config.uploadFileName = 'multipartFile'
    // editor.config.uploadImgAccept = []
    editor.config.uploadImgMaxSize = 10 * 1024 * 1024
    // editor.config.zindex = 9990000;
    editor.config.uploadImgShowBase64 = true
    editor.config.debug = true
    // editor.config.menus = map(wangEditor.config.menus, function(item, key) {
    //      if (item === 'insertcode') {
    //          return null;
    //      }
    //      if (item === 'fullscreen') {
    //          return null;
    //      }
    //      return item;
    //  });
    editor.config.uploadImgHooks = {
    // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
    // （但是，服务器端返回的必须是一个 JSON 格式字符串！！！否则会报错）
      customInsert(insertImg, result, editor) {
        // 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
        // insertImg 是插入图片的函数，editor 是编辑器对象，result 是服务器端返回的结果
        // 举例：假如上传图片成功后，服务器端返回的是 {url:'....'} 这种格式，即可这样插入图片：
        result.errno = 0
        // console.log(url)
        // console.log(result)
        insertImg(result.data.ossFilePath)
        // result 必须是一个 JSON 格式字符串！！！否则报错
      }
    }
    editor.create()
  },
  // activated(){
  //     this.urlId()

  // },
  methods: {

    // 区域接口
    areaListAPI(value) {
      quyuList().then((res) => {
        this.areaList = res.data.data
      })
    },
    // 选择行业树形结构
    treePost() {
      treeList().then((res) => {
        console.log(res.data.data)
        // this.optionsTree = res.data.data
        this.optionsTree = this.getTreeData(res.data.data)
      })
    },
    // 解决 每个树形结构里面最后的空数组
    getTreeData(data) {
      // 循环遍历json数据
      for (var i = 0; i < data.length; i++) {
        if (data[i].childenCategoryInfo.length < 1) {
          // childenCategoryInfo若为空数组，则将childenCategoryInfo设为undefined
          data[i].childenCategoryInfo = undefined
        } else {
          // childenCategoryInfo若不为空数组，则继续 递归调用 本方法
          this.getTreeData(data[i].childenCategoryInfo)
        }
      }
      return data
    },
    // 精准选择
    noticeInfoList() {
      console.log(this.body)
      const body = {
        otherCondition: this.body.otherCondition,
        pageDTO: {
          current: this.body.current,
          size: this.body.size
        }
      }
      noticeInfo(body).then((res) => {
        this.tableTreeData = res.data.data.records
        this.total = res.data.data.total
      })
    },

    handleNodeClick(value) {
      console.log(this.treeCodeList)
      console.log(value)
    },
    // 提交
    submitForm() {
      // 内容
      this.ruleForm.content = this.Content
      console.log(this.fileList)
      console.log(this.ruleForm)
        // this.$router.push({ path: '/notice/inform' })
      this.loadingButon = true
      noticeAdd(this.ruleForm).then((res) => {
        this.$message({
          type: 'success',
          message: '添加成功，到通知列表查看。'
        })
          this.loadingButon = false;
        this.$router.push({ path: '/notice/inform' })
      }).catch(err => {
          this.loadingButon = false;
        });
    },
    // 重置
    resetForm(){
       this.showhangye = false,
        this.Content = ''
        this.showjingzhun = false,
        this.showdate = false,
        this.ruleForm = {
          noticeScope: [],
          readFlag: 1,
          noticeType: 1,
          adjunctFile: [],
          supervisionIds: [],
          sendType: 1
        }
        this.fileList=[]
        this.editor.txt.clear()
    },
    handleChange(value) {
      console.log(value)
    },
    handleSizeChange(value) {
      this.body.size = value
      this.noticeInfoList()
    },
    handleCurrentChange(value) {
      this.body.current = value
      this.noticeInfoList()
    },
    beforeUpload(file){
      const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1);
 
  // const whiteList = ["excel", "doc", "docx", "ppt","pptx", "txt","pdf","mp4"];
  // console.log(fileSuffix)
  // if (whiteList.indexOf(fileSuffix) === -1) {
  //   this.$message.error("上传文件只能是 excel、pdf、doc、docx、ppt、pptx、mp4格式", );
  //   return false;
  // }
 
  const isLt2M = file.size / 1024 / 1024 < 50;
 
  if (fileSuffix=="mp4" && !isLt2M) {
    this.$message.error("上传文件大小不能超过 50MB");
    return false;
  }
    },
    //   文件上传
    handleUp(res, file, fileList) {
      // console.log(res)
      // console.log(file)
      // console.log(fileList)
      if (res.status != 200) {
        this.$message.error(res.message)
        fileList.pop()
      } else {
        console.log(res.data)
        // fileList.map((res)=>{
        //     // return res.
        // })
        this.ruleForm.adjunctFile.push(res.data)
      }
    },
    handRemove(file, fileList) {
      const data = fileList.map((res) => {
        console.log(res.response.data)
        return res.response.data
      })
      console.log(data)
      this.ruleForm.adjunctFile = data
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 5 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    beforeRemove(file, fileList) {
      // return this.$confirm(`确定移除 ${file.name}？`)
    },
    // 搜索aa
    handleSearch() {
      this.body.current = 1
      this.noticeInfoList()
    },
    // 精准选择多选
    handleSelectionChange(value) {
      this.accurateList = value
      this.ruleForm.merchantIds = value.map((res) => {
        return res.merchantId
      })
      console.log(value)
    },
    onDatefifler() {
      const nowDate = new Date()
      const date = {
        year: nowDate.getFullYear(),
        month: nowDate.getMonth() + 1,
        date: nowDate.getDate()
      }
      this.systemTime = date.year + '-' + date.month + '-' + date.date
    },
    // 判断时间
    onshowdate(value) {
      // 立刻发送
      if (value == 1) {
        this.showdate = false
        this.onDatefifler()
      } else if (value == 2) {
        // 定时发送
        this.showdate = !this.showdate
      } else if (value == 3) {
        // 日期选择后
        this.systemTime = this.ruleForm.sendTime.split(' ')[0]
      }
    },
    // 判断精准选择点击后显示和清空数据
    onPrecise() {
      this.showjingzhun = !this.showjingzhun
      this.table = this.showjingzhun
      this.accurateList = []
      this.ruleForm.merchantIds = []
      // 清除多选的勾勾
      this.$refs.tableTreeData.clearSelection()
    }
  }
}
</script>

<style scoped>

    .grid-content{
        width: 100%;
        border-left:2px solid #E9E9E9;
        padding-left: 30px;
    }
    .grid-text{
        color:#000000;
        font-weight: 550;
        font-size: 18px;
    }
    .el-divider--horizontal{
      margin: 10px 0
    }
    /deep/ .el-form-item--medium .el-form-item__content {
        z-index: 9 !important;
    }
    /deep/ .el-scrollbar__wrap {
     z-index: 9999999 !important;
    }

    .showList{
        background:#F5F5F5;
        width:100%;
        position: relative;
        top: -20px;
        padding: 10px;
        margin-bottom: 10px;
    }
    .scrollBar{
        height: 600px;
        overflow-x :scroll;
    }
    .iconClass{
        color:#1C92DC;
    }

    .scrollBar::-webkit-scrollbar {
    /*滚动条整体样式*/
    width : 11px;  /*高宽分别对应横竖滚动条的尺寸*/
    height: 1px;
    }
    .scrollBar::-webkit-scrollbar-thumb {
    /*滚动条里面小方块*/
    border-radius: 10px;
    box-shadow   : inset 0 0 5px rgba(0, 0, 0, 0.2);
    background   : #a3a2a2;
    }
    .scrollBar::-webkit-scrollbar-track {
    /*滚动条里面轨道*/
    box-shadow   : inset 0 0 5px rgba(0, 0, 0, 0.2);
    border-radius: 10px;
    background   : #ededed;
    }
    .textSpan {
        font-size: 12px;
        font-weight: 600;
        color: rgb(87, 86, 86);
        text-align: right;
    }
   /deep/ .el-drawer__body{
        padding: 0px 20px;
    }
    .divStyle{
        display: flex;
    }
    .spanStyle{
        font-size:12px;
    }
    /deep/  .el-drawer.rtl{
 
    overflow: scroll
    }
    .bottom{
      position: absolute;bottom: 0;right: 0;
      width: 100%;
      padding: 10px;
      background: #ffffff;
    }

  /* .grid-content1 {
     box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
     position: relative;
     top: 0;
     left: 0;
     width: 390px;
     height: 600px;
     overflow-x :scroll;

  }
  ::-webkit-scrollbar{
      width: 0 !important;
  }
  ::-webkit-scrollbar{
      width: 0 !important;height: 0;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  .el-divider--horizontal{
      margin: 10px 0
  }
  .info{
      padding: 10px;
      color: #292929;
      font-weight: 550;
      letter-spacing: .05em;
      font-size: 16px;
  }
  .des_text{
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      padding: 0px 10px;
      color: #8C8C8C;
      font-size: 14px;
  }
  .des_conet{
      padding:10px;
      font-size:14px;
      letter-spacing: .07em;
      line-height:  1.5em;
  } */
</style>
