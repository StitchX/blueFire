<template>
  <van-nav-bar
    :title="title"
    :left-text="leftText"
    :right-text="rightText"
    :left-arrow="leftArrow"
    @click-left="onClickLeft"
    @click-right="onClickRight"
    :fixed="isTop"
    placeholder
    :z-index="10"
    :safe-area-inset-top="topSafe"
  >
    <!-- <template #right>
      <van-icon name="search" size="18" />
    </template> -->
  </van-nav-bar>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: "",
    },
    leftText: {
      type: String,
    },
    rightText: {
      type: String,
    },
    leftArrow: {
      type: Boolean,
      default: true,
    },
    isTop: {
      type: Boolean,
      default: false,
    },
    topSafe: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    onClickLeft() {
      this.$emit("clickLeft");
    },
    onClickRight() {
      this.$emit("clickRight");
    },
  },
};
</script>

<style>
</style>