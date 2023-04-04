<template>
<div>
  <span @click="back()" style="margin:15px 0" class="el-icon-arrow-left">我的订单</span>
  <div style="margin:15px 0;text-align: center">
    <el-radio-group v-model="radio" size="small" @input="getOrders(radio)">
      <el-radio-button label="0" >全部订单</el-radio-button>
      <el-radio-button label="1">待付款</el-radio-button>
      <el-radio-button label="3">待签收</el-radio-button>
      <el-radio-button label="4">已完成</el-radio-button>
      <el-radio-button label="5">已取消</el-radio-button>
    </el-radio-group>
  </div>
  <div @click="ToOrders(item)" style="border-radius: 15px;box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);" v-for="item in ordersInfo" :key="item.id">
      <div style="margin: 10px 10px;display: flex" >
        <div >
          <el-image
            style="width: 80px; height: 80px;border-radius: 15px;margin-top: 10px"
            :src="imgPath+item.orderProduct[0].img"
            fit="cover"></el-image>
        </div>
        <div style="margin-left: 10px;margin-top: 20px;"  class="text">
          <div v-for="i in item.orderProduct" :key="i.id">
            {{i.name}} x {{i.number}}
          </div>
        </div>
        <div style="margin-top: 15px;position: absolute;right: 26px;text-align:center;">
          <div style="margin-bottom: 10px;font-size: 18px">
            {{status[item.status-1]}}
          </div>
          实付款<br/><span style="color: #ff0000;">￥{{item.amount}}</span>
        </div>
<!--        <el-button @click="" style="border-radius: 10px;">查看详情</el-button>-->
      </div>
  </div>
  <div>
  </div>
</div>
</template>

<script>
export default {
  name: "Orders",
  data(){
    return{
      radio:this.$store.state.radio,
      status:['待付款','待派送','待签收','已完成','已取消'],
      imgPath:'/api/img/download?name=',
      userId:JSON.parse(localStorage.getItem('Info')).id,
      ordersInfo:''
    }
  },
  watch:{
    radio(now){
      this.$store.dispatch('radio',now)
      this.getOrders(now)
    }
  },
  created() {
    this.getOrders(this.radio)
  },
  methods:{
    ToOrders(info){
      this.$router.push({path:"/ordersPage",query:{info:info}})
    },
    getOrders(status){
      if(this.userId!==null){
        this.$axios.get("/api/orders/user/info",{
          params:{
            userId:this.userId,
            status:status
          }
        }).then(res=>{
          this.ordersInfo=res.data.data
        })
      }
    },
    back(){
      this.$store.dispatch('radio',0)
      this.$router.push('/user')
    }
  }

}
</script>

<style scoped>
.text{
  /*解决文字溢出*/
  overflow:hidden;
  display:-webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp:2
}
</style>
