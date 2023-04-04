<template >
<div>
  <span @click="back()" style="margin:15px 0" class="el-icon-arrow-left">订单详情</span>
  <div>
    <div @click="ToProductInfo(i.productId)" class="code" v-for="i in info.orderProduct" :key="i.id">
      <div style="margin: 10px 10px;display: flex">
        <el-image
          style="width: 80px; height: 80px;border-radius: 15px;margin-top: 10px"
          :src="imgPath+i.img"
          fit="cover"></el-image>
        <div style="margin: 10px 20px">
          <span style="font-size: 18px">{{i.name}}</span><br/>
          <span style="height: 5px"></span><br/>
          <span style="color: #ff0000;">￥{{i.price}}</span>
        </div>
      </div>
      <div>
        <div class="order"><span style="margin-right: 20px;color: #524f4f">颜色:</span>{{i.color}}</div>
        <div class="order"><span style="margin-right: 20px;color: #524f4f">尺寸:</span>{{i.size}}</div>
        <div class="order"><span style="margin-right: 20px;color: #524f4f">数量:</span>{{i.number}}</div>
      </div>
      <div style="height: 5px"></div>
    </div>
    <div style="margin-top: 10px" class="code">
      <div style="height: 5px"></div>
      <div class="order"><span style="margin-right: 20px;color: #524f4f">订单编号:</span>{{info.id}}</div>
      <div class="order"><span style="margin-right: 20px;color: #524f4f">下单时间:</span>{{info.orderTime}}</div>
      <div class="order"><span style="margin-right: 20px;color: #524f4f">支付时间:</span>{{info.checkoutTime}}</div>
      <div style="height: 5px"></div>
    </div>
    <div style="margin-top: 10px" class="code">
      <div style="height: 5px"></div>
      <div class="order" style="font-size: 18px">配送信息</div>
      <div class="order"><span style="margin-right: 20px;color: #524f4f">收货人:</span>{{info.consignee}}</div>
      <div class="order"><span style="margin-right: 20px;color: #524f4f">联系电话:</span>{{info.phone}}</div>
      <div class="order"><span style="margin-right: 20px;color: #524f4f">收货地址:</span>{{info.address}}</div>
      <div class="order"><span style="margin-right: 20px;color: #524f4f">备注信息:</span>{{info.remark}}
        <el-input :style="info.status===1?'margin-top: 10px':'display:none'" placeholder="添加备注" v-model="text" clearable></el-input>
      </div>
      <div style="height: 5px"></div>
    </div>
    <div style="margin-top: 10px;text-align: center" class="code">
      <div style="height: 5px"></div>
      <div class="order">
        <span style="margin-right: 20px;color: #524f4f">实付款:</span>
        <span style="font-size: 20px;color: red">￥{{info.amount}}</span>
      </div>
      <div class="order" style="font-size: 26px">{{status[info.status-1]}}</div>
      <div class="order">
        <el-button @click="changeStatus(5,'取消订单')" :style="info.status>3?'display:none':''" size="small" round>取消订单</el-button>
        <el-button @click="changeStatus(2,'付款')" :style="info.status===1?'':'display:none'" size="small" round>付款</el-button>
        <el-button @click="changeStatus(4,'签收')" :style="info.status===3?'':'display:none'" size="small" round>签收</el-button>
      </div>
      <div style="height: 5px"></div>
    </div>
  </div>
</div>
</template>

<script>
export default {
  name: "OrdersPage",
  data(){
    return{
      status:['待付款','待派送','待签收','已完成','已取消'],
      imgPath:'/api/img/download?name=',
      info:this.$route.query.info,
      text:''
    }
  },
  created() {
    if(this.info.id===undefined){
      this.$router.go(-1)
    }
  },
  methods:{
    back(){
      this.$router.go(-1)
    },
    ToProductInfo(id){

      this.$router.push({path:"/productInfo",query:{id:id}})
    },
    changeStatus(status,name){
      let  message='此操作将'+name+', 是否继续?'
      let data={}
      data.status=status
      data.id=this.info.id
      if(status===2)
        data.remark=this.text
      if(status===5)
        data.orderProduct=this.info.orderProduct
      this.$confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.put("/api/orders", data).then((res)=>{
          if(res.data.code===1){
            this.$message({
              message:name+"成功",
              type: 'success',
              duration:1000
            });
          }
        })
        this.info.status=status
        if(status===2)
          this.info.remark=this.text
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消',
          duration:1000
        });
      });
    }
  }
}
</script>

<style scoped>

.code{
  border-radius: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}
.order{
  margin: 10px 10px;
}
</style>
<style>
.el-message-box {
  width: 300px;
}
</style>
