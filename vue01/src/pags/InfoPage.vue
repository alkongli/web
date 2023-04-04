<template>
<div style="text-align: center">
  <div style="font-size: 25px;margin-top: 20px">
    恭喜您下单成功
  </div>
  <el-button style="margin-top: 50px" @click="ToOrder" type="success" plain>点击跳转支付(<span style="color: red;font-size: 20px">{{time}}s</span>后自动跳转)</el-button>
</div>
</template>

<script>
export default {
  name: "InfoPage",
  data(){
    return{
      time:5,
    }
  },
  watch:{
    time:{
      handler(now){
        if(now>0){
          setTimeout(()=>{
            this.time--;
          },1000)
        }else {
          this.ToOrder()
        }
      },
      immediate:true
    }

  },
  methods:{
    ToOrder(){
      this.$store.dispatch('radio',1)
      this.$router.push('/orders')
    },

  }
}
</script>

<style scoped>

</style>
