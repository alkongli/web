
import Vuex from 'vuex'
import Vue from "vue";

Vue.use(Vuex)
//响应组件中的动作
const actions={
  username(context,value){
    context.commit('Username',value)
  },
  table(context,value){
    context.commit('Table',value)
  },
  productList(context,value){
    context.commit('ProductList',value)
  },
  page(context,value){
    context.commit('Page',value)
  },
  radio(context,value){
    context.commit('Radio',value)
  },
}

//操作数据(state)
const mutations={
  Username(state,value){
    state.username=value
  },
  Table(state,value){
    state.table=value
  },
  ProductList(state,value){
    state.productList=value
  },
  Page(state,value){
    state.page=value
  },
  Radio(state,value){
    state.radio=value
  },
}

//存储数据
const state={
  username:'',
  table:'',
  productList:'',
  page:{},
  radio:0,
  sizes:['XS','S','M','L','XL','XXL','38','39','40','41','42'],
  discount:[
    {
      name:'五折',
      value:0.5
    },
    {
      name:'四折',
      value:0.4
    },
    {
      name:'三折',
      value:0.3
    },
    {
      name:'两折',
      value:0.2
    },
    {
      name:'一折',
      value:0.1
    },
  ]
}

export default new Vuex.Store({
  actions,
  mutations,
  state
})
