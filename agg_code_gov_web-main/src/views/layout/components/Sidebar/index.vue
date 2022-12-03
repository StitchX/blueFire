<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="$route.path"
        :collapse="isCollapse"
        background-color="#0A5BAC"
        :text-color="variables.menuText"
        :collapse-transition="false"
        :unique-opened="true"
        class="menuText"
        mode="vertical"
      >
        <sidebar-item v-for="route in permission_routes" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'
import Logo from './Logo'

export default {
  components: { SidebarItem, Logo },
  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar'
    ]),
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    },
    showLogo() {
      return this.$store.state.setting.sidebarLogo
    }
  }
}
</script>

<style >
  .menuText{
    background: linear-gradient(#0A5BAC,#0A5BAC,#0A5BAC,#031525) !important;
  }
  .el-menu-demo{
    background: linear-gradient(#0A5BAC,#0A5BAC,#0A5BAC,#031525) !important;
  }
</style>