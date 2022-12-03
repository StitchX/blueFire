<template>
  <div class="container">
    <top />
    <div class="flexd" >
      <div style="width:50%;marginRight:10px">
        <middleLeft />
      </div>
      <div style="width:50%;">
        <middleRight />
      </div>
    </div>
    <div class="flexd" >
      <div style="width:50%;marginRight:10px">
        <footerLeft />
      </div>
      <div style="width:50%;">
         <footerRight />
      </div>
      
     
    </div>
  </div>
</template>

<script>
import top from "./top";
import middleLeft from "./middleLeft";
import middleRight from "./middleRight";
import footerLeft from "./footerLeft";
import footerRight from "./footerRight";
export default {
  components: {
    top,
    middleLeft,
    middleRight,
    footerRight,
    footerLeft
  },
  data() {
    return {};
  },
  created() {},
  methods: {}
};
</script>
<style scoped>
.container {
  background: #ececec8e;
  padding: 10px;
}
.flexd {
  display: flex;
  flex-direction: row;
  /* height: 20rem; */
  margin-top: 10px;
  margin-right: 10px;
  /* margin-bottom: 10px; */
}
</style>
