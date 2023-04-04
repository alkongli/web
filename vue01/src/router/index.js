import Vue from 'vue'
import Router from 'vue-router'
import EmployeeHome from "../pags/EmployeeHome";
import EmployeeLogin from "../pags/EmployeeLoginPage";
import listPage from "../pags/ListPage";
import UserLogin from "../pags/UserLogin";
import UserHome from "../pags/UserHome";
import ProductInfo from "../pags/ProductInfo";
import Address from "../pags/Address";
import AddressForm from "../pags/AddressForm";
import Orders from "../pags/Orders";
import OrdersPage from "../pags/OrdersPage";
import InfoPage from "../pags/InfoPage";
import SecKillPage from "../pags/SecKillPage";
Vue.use(Router)

export default new Router({
  mode:'history',
  routes: [
    {
      path: '/employee',
      name: 'EmployeeHome',
      component: EmployeeHome
    },
    {
      path: '/user/login',
      name: 'UserLogin',
      component: UserLogin

    },
    {
      path: '/employee/login',
      name: 'EmployeeLogin',
      component: EmployeeLogin
    },
    {
      path:'/',
      component: UserHome
    },
    {
      path:'/user',
      name:'UserHome',
      component: UserHome
    },
    {
      path:'/productInfo',
      name:'ProductInfo',
      component: ProductInfo
    },
    {
      path: '/listPage',
      name: 'listPage',
      component: listPage
    },
    {
      path: '/address',
      name: 'Address',
      component: Address
    },
    {
      path: '/addressForm',
      name: 'AddressForm',
      component: AddressForm
    },
    {
      path: '/orders',
      name: 'Orders',
      component: Orders
    },
    {
      path: '/ordersPage',
      name: 'OrdersPage',
      component: OrdersPage
    },
    {
      path: '/infoPage',
      name: 'InfoPage',
      component: InfoPage
    },
    {
      path: '/secKillPage',
      name: 'SecKillPage',
      component: SecKillPage
    },
  ]
})
