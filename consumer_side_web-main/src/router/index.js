import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from "@/views/Home"

Vue.use(VueRouter)

const routes = [
  {
    path: '/info',
    name: 'Info',
    component: () => import('@/views/info.vue')
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/check',
    name: 'Check',
    component: () => import('@/views/check.vue')
  },
  {
    path: '/source',
    name: 'Source',
    component: () => import('@/views/source.vue')
  },
  {
    path: '/riskLevel',
    name: 'RiskLevel',
    component: () => import('@/views/riskLevel.vue')

  },
  {
    path: '/complaint',
    name: 'complaint',
    component: () => import('@/views/complaint.vue')
  },
  {
    path: '/mc',
    name: 'mc',
    component: () => import('@/views/mcList.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
