<template>
  <div>
    <div><span style="font-size: 25px">购物车</span>({{shoppingInfo.length}})
      <span @click="changeAddress" class="el-icon-location-outline address">{{nowAddress.address}}
      <span class="el-icon-arrow-right" style="margin-left: 20px"></span>
      </span>
      <span @click="deletePage=!deletePage" class="el-icon-delete" :style="deletePage===true?'color:red':''" style="font-size: 25px;position: absolute;right: 8px;top:14px"></span>
    </div>
    <el-dialog
      title="选择地址"
      :visible.sync="drawerAddress"
      width="90%"
    ><AddressSelect :address-list="addressList" v-on:close="close"></AddressSelect>
    </el-dialog>
    <el-checkbox-group v-model="checked" @change="handleCheckedCitiesChange">
    <div style="margin-top: 15px;" class="code" v-for="i in shoppingInfo" :key="i.id">
      <div class="order" >
        <el-checkbox size="medium" :label="i"><span></span></el-checkbox>
      </div>
      <div style="display:inline-block;vertical-align: middle;">
        <div style="margin: 10px 10px;">
          <div style="display:inline-block;">
            <el-image
              style="width: 100px; height: 100px;border-radius: 15px;margin-top: 10px"
              :src="imgPath+i.productEntity.img"
              fit="cover"></el-image>
          </div>
          <div style="display:inline-block;margin: 10px 20px">
            <div style="font-size: 20px;" class="text" >{{i.productEntity.name}}</div><br/>
            <span style="height: 5px"></span><br/>
            <span style="font-size: 20px;color: #ff0000;">￥{{i.productEntity.price}}</span>
          </div>
        </div>
          <span style="margin-right: 20px;margin-bottom:10px;color: #524f4f">颜色:</span>
          <el-radio-group v-model="color[i.num]" size="small">
            <el-radio-button v-for="i in i.productEntity.colorList.split(',')" :key="i" :label="i" border>{{i}}</el-radio-button>
          </el-radio-group>
        <br/>
          <span style="margin-right: 20px;margin-bottom:10px;color: #524f4f">尺寸:</span>
        <el-radio-group v-model="size[i.num]" size="small">
          <el-radio-button v-for="i in i.productEntity.sizeList.split(',')" :key="i" :label="i" border>{{i}}</el-radio-button>
        </el-radio-group>
        <br/>
          <span style="margin-right: 20px;margin-bottom:5px;color: #524f4f">数量:</span>
        <el-input-number @change="allAmount" size="small" v-model="num[i.num]" :min="1" :max="i.productEntity.stock" label="描述文字"></el-input-number>

        <div style="height: 5px"></div>
      </div>
    </div>
    </el-checkbox-group>

    <div class="footer" style="background-color: white;">
      <div style="margin: 10px 10px">
        <el-checkbox v-model="checkAll" @change="handleCheckAllChange" >全选</el-checkbox>
        <div :style="deletePage===true?'display:none':''" style="display: inline-block;position: absolute;right: 0;bottom: 2px">
          <span>合计:</span>
          <span style="margin-right: 10px;color: red">￥{{amount}}</span>
          <el-button round style="background-color:#ff4800;color: white;" @click="submit()">结算</el-button>
        </div>
        <div :style="deletePage===true?'':'display:none'" style="display: inline-block;position: absolute;right: 0;bottom: 2px">
          <el-button round style="background-color:#ff4800;color: white;" @click="submitDel()">删除</el-button>
        </div>
      </div>
    </div>
    <div style="height: 100px"></div>
  </div>
</template>

<script>
import AddressSelect from "./AddressSelect";
export default {
  name: "Shopping",
  components:{AddressSelect},
  data(){
    return{
      checkAll:false,
      checked:[],
      imgPath:'/api/img/download?name=',
      shoppingInfo:[],
      allInfo:'',
      num:[],
      color:[],
      size:[],
      amount:0,
      selectProduct:'',
      addressList:[],
      nowAddress:'',
      drawerAddress:false,
      deletePage:false
    }
  },
  created() {
    this.getShoppingInfo()
  },
  methods:{
    close(flag,item){
      this.nowAddress=item
      this.drawerAddress=flag
    },
    changeAddress(){
      this.drawerAddress=true
    },
    allAmount(){
      this.amount=0
      for(let i=0;i<this.selectProduct.length;i++){
        this.amount=this.amount+this.num[this.selectProduct[i].num]*this.selectProduct[i].productEntity.price
      }
    },
    getShoppingInfo(){
      this.$axios.get('/api/shoppingCart/list',{
        params:{
          userId:JSON.parse(localStorage.getItem('Info')).id
        }
      }).then(res=>{
        if(res.data.code===1){
          this.$axios.get("/api/address/list",{
            params:{
              userId:JSON.parse(localStorage.getItem('Info')).id
            }
          }).then(res=>{
            if(res.data.code===1){
              this.addressList=res.data.data
              for(let i=0; i<this.addressList.length;i++){
                if(this.addressList[i].isDefault===1){
                  this.nowAddress=this.addressList[i]
                  break
                }
              }
            }
          })
          this.allInfo=res.data.data
          for(let i=0;i<this.allInfo.length;i++){
            if(this.allInfo[i].productEntity!=null){
              this.shoppingInfo.push(this.allInfo[i])
            }
          }
          for(let j=0;j<this.shoppingInfo.length;j++){
            this.shoppingInfo[j].num=j
            this.num.push(1)
            this.color.push('')
            this.size.push('')
          }
        }
      })
    },
    handleCheckAllChange(val) {
      this.checked = val ? this.shoppingInfo : [];
      if(val===true)
        this.selectProduct=this.shoppingInfo
      else
        this.selectProduct=[]
      this.allAmount()
    },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.shoppingInfo.length;
      this.selectProduct=value
      this.amount=0
      for(let i=0;i<value.length;i++){
        this.amount=this.amount+this.num[value[i].num]*value[i].productEntity.price
      }
    },
    submit(){
      if(this.selectProduct==='')
        this.$message('您还没有选择商品哦!');
      else{
        this.$confirm("即将为您下单。是否继续?", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids=[]
          let cont=1
          let name=''
          for(let i=0;i<this.selectProduct.length;i++){
            ids.push(this.selectProduct[i].id)
            if(this.size[this.selectProduct[i].num]===''||this.color[this.selectProduct[i].num]===''){
              cont=0
              name=this.selectProduct[i].productEntity.name
            }
          }
          if(cont===1){
            let orders={}
            orders.userId=JSON.parse(localStorage.getItem('Info')).id
            orders.addressId=this.nowAddress.id
            orders.amount=this.amount
            orders.orderProduct=[]
            for(let i=0;i<this.selectProduct.length;i++){
              let orderProduct={}
              orderProduct.typeId=this.selectProduct[i].productEntity.typeId
              orderProduct.productId=this.selectProduct[i].productEntity.id
              orderProduct.name=this.selectProduct[i].productEntity.name
              orderProduct.img=this.selectProduct[i].productEntity.img
              orderProduct.price=this.selectProduct[i].productEntity.price
              orderProduct.number=this.num[this.selectProduct[i].num]
              orderProduct.size=this.size[this.selectProduct[i].num]
              orderProduct.color=this.color[this.selectProduct[i].num]
              orders.orderProduct.push(orderProduct)
            }
            this.$axios.post("/api/orders/save",orders).then(res2=>{
              if (res2.data.code === 1) {
                this.$axios.delete('/api/shoppingCart/delete',{data:ids})
                this.$router.push('/infoPage')
              }else {
                this.$message({
                  type: 'error',
                  message: res2.data.msg,
                  duration:1000
                });
              }
            })
          }else {
            this.$message({
              type: 'error',
              message: '商品：'+name+"规格没有选!",
              duration:1000
            });
          }

          }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消',
            duration:1000
          });
        });
      }
    },
    submitDel(){
      if(this.selectProduct==='')
        this.$message('您还没有选择要删除商品哦!');
      else{
        this.$confirm("即将将商品移出购物车。是否继续?", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids=[]
          for(let i=0;i<this.selectProduct.length;i++)
            ids.push(this.selectProduct[i].id)
          this.$axios.delete('/api/shoppingCart/delete',{data:ids}).then(res=>{
            if(res.data.code===1){
              this.allInfo=''
              this.shoppingInfo=[]
              this.selectProduct=''
              this.getShoppingInfo()
              this.amount=0
              this.$message({
                type: 'success',
                message: res.data.data,
                duration:1000
              });
            }
          })
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
}
</script>

<style scoped>
.text{
  width: 120px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
.address{
  border: 1px solid #9e9d9d;
  border-radius: 10px;
  padding: 3px 3px;
  margin-left: 5px;
  color: #9a9ea5;
}
.code{
  border-radius: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}
.order{
  margin: 2px 2px;
  display: inline-block;
  vertical-align: middle;/*垂直居中*/
}
.footer{
  position: fixed;
  bottom: 60px;
  left: 0;
  width: 100%;
  border-top: 1px solid #eaeaea;
  display: flex;
}
</style>
<style>
.el-checkbox__inner{
  width: 20px;
  height: 20px;
}
.el-checkbox__inner::after{
  height: 10px;
  width: 5px;
  left: 6px;
}
.el-message-box {
  width: 300px;
}
.el-checkbox-group{
  font-size: 16px;
}
</style>
