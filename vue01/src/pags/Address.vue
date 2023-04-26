<template>
  <div>
    <span @click="back()" style="margin:15px 0" class="el-icon-arrow-left">我的地址</span>
    <div style="border-radius: 15px;box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);">
      <div style="margin-left: 20px;" v-for="item in addressInfo" :key="item.id">
        <div style="height: 10px"></div>
        <span style="font-size: 18px">{{item.consignee}}</span>
        <span style="margin-left: 20px">{{item.phone}}</span><br/>
        <div class="el-icon-location-information" style="margin-top: 5px">{{item.address}}</div>
        <div style="margin-top: 5px">
          <el-radio v-model="radio" :label="item.id">默认</el-radio>
          <i @click="deleteAddress(item.id)" style="margin-left: 20%" class="el-icon-delete"></i>
          <i @click="updateAddress(item)" style="margin-left: 20%" class="el-icon-edit"></i>
        </div>
        <div style="height: 10px"></div>
      </div>
    </div>

    <el-button class="footer" @click="addAddress()">
      添加收货地址
    </el-button>
  </div>
</template>

<script>
export default {
  name: "Address",
  data(){
    return{
      radio:'',
      userId:this.$route.query.userId,
      addressInfo:'',
      formInfo:''
    }
  },
  watch:{
    radio(now,before){
      if(before!==''){
        let data={}
        data.id=now
        data.userId=this.userId
        data.isDefault=1

        this.$axios.put("/api/address/default",data).then(res=>{
          if(res.data.code===1) {
            this.$message({
              message: res.data.data,
              type: 'success',
              duration: 2000
            })
          }
        })
      }
    }
  },
  created() {
    this.getAddress()

    if(this.radio==="")
        this.radio=0
  },
  methods:{
    getAddress(){
      if(this.userId!==null){
        this.$axios.get("/api/address/list",{
          params:{
            userId:this.userId
          }
        }).then(res=>{
          this.addressInfo=res.data.data
          for(let i=0;i<this.addressInfo.length;i++){
            if(this.addressInfo[i].isDefault===1){
              this.radio=this.addressInfo[i].id
              break
            }
          }
        })
      }
    },
    back(){
      this.$router.go(-1)
    },
    deleteAddress(id){
      this.$confirm("即将删除改地址。是否继续?", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete("/api/address",{
          params:{
            id:id
          }
        }).then(res=>{
          if(res.data.code===1) {
            this.$message({
              message: res.data.data,
              type: 'success',
              duration: 2000
            })
          }
          this.getAddress()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消',
          duration:1000
        });
      });
    },
    updateAddress(info){
      this.$router.push({path:"/addressForm",query:{formInfo:info}})
    },
    addAddress(){
      let info={'id':'','sex':'','address':'','phone':''}
      this.$router.push({path:"/addressForm",query:{formInfo:info}})
    }
  },
}
</script>

<style scoped>
.footer{
  height: 60px;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
}
</style>
<style>
.el-message-box {
  width: 300px;
}
</style>
