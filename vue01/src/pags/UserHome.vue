<template>
<div>
  <component :is="name" />
  <div style="height: 100px"></div>
  <div class="footer">
    <div class="title" :style="name==='UserHeader'?'color: #ff0000':''" @click="click('UserHeader')">首页</div>
    <div class="title" :style="name==='Shopping'?'color: #ff0000':''" @click="click('Shopping')">购物车</div>
    <div class="title" :style="name==='UserCenter'?'color: #ff0000':''" @click="click('UserCenter')">个人中心</div>
  </div>
</div>
</template>

<script>
import UserCenter from "../components/User/UserCenter";
import UserHeader from "../components/User/UserHeader";
import Shopping from "../components/User/Shopping";
export default {
  name: "UserHome",
  components:{UserHeader,UserCenter,Shopping},
  created() {
    if(this.$store.state.table==='')
      this.click("UserHeader")
  },
  data() {
    return {
      name:this.$store.state.table,
    }
  },
  methods:{
    click( name){
      this.name=name
      this.$store.dispatch('table',name)
    }
  }
}
</script>

<style scoped>
.footer{
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: #eaeaea;
  display: flex;
}
.title{
  margin: 20px 0;
  width: 33.33%;
  color: black;
  display: flex; /**/
  justify-content: center; /*水平居中*/
  align-items: Center; /*垂直居中*/
}
</style>
