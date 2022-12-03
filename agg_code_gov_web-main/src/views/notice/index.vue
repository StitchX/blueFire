<template>
  <div>
    <div v-if="show">
      <add />
    </div>
    <div v-else>
      <update />
    </div>
  </div>
</template>

<script>
import add from './add'
import update from './update'

export default {
  components: { add, update },

  data() {
    return {
      show: true
    }
  },
  created() {
    this.urlId()
  },
  activated() {
    this.urlId()
  },
  methods: {
    // 获取修改信息
    urlId() {
      const arry = this.$route.query.noticeId
      if (arry) {
        this.show = false
      } else {
        this.show = true
      }
    }
  }
}
</script>

<style  scoped>

</style>
