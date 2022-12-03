<template>
  <div class="scan-documents">
      <van-uploader v-model="fileList" multiple max-count="1" :after-read="afterRead" />
  </div>
</template>

<script>
import Exif from 'exif-js'
import { ref } from 'vue';
export default {
  name: 'scanDocuments',
  data() {
    return {
       fileList: [
      ],
      // 图片信息
      files: {
        name: "",
        type: ""
      }
    }
  },
  methods: {
     onIndustryCategory(){
            this.$api.postSort().then((res)=>{
                this.columnsCategory = res.data.data
                console.log(this.columns)
            })
        },
         afterRead(file) {
        // this.files.name = file.file.name // 获取文件名
        // this.files.type = file.file.type // 获取类型
        // this.imgPreview(file.file)

        let formData = onConfirm
      formData.append('api', file.file)
    //   上传图片
        this.$api.upImage(formData).then((res)=>{
            console.log(res.data)
            let dataImg = res.data.data
              let pic = {
             pic: dataImg
         }
        //  图片识别文字
                 this.$api.upPic(pic).then((res)=>{
                     let data = res.data.data.data
                        // 社会信用代码
                        this.dataForm.code = data.code
                        // 详细地址
                        this.dataForm.dizhi = data.dizhi
                        // 负责人/法人
                        this.dataForm.faren = data.faren
                        // 企业名称
                        this.dataForm.name = data.name
                        console.log(data)
                    })
         })
       
    
        },
  }
}
</script>
