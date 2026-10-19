import Vue from 'vue';
import VueRouter from 'vue-router';

import Login from "../Login.vue";
import Main from "../Main.vue";
import Memberlndex from '../view/member/Memberlndex.vue';
import Recordlndex from '../view/record/Recordlndex.vue';
import AIChatVue from '../view/aichat/AIChat.vue';
Vue.use(VueRouter)

// 统一变量名 router
const router = new VueRouter({
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/main',
      component: Main,
	  children:[{
	  path: '/member',
	  component: Memberlndex
	},
	{
	  path: '/record',
	  component: Recordlndex
	},
	{
	  path: '/aichat',
	  component: AIChatVue
	}
		  
	  ]
    },
	
	
  ]
});

// 导出必须和定义的变量名一致 router
export default router;