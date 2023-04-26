<template>
  <div>
    <span @click="back()" style="margin:15px 0" class="el-icon-arrow-left">商品信息</span>
    <div>
      <div class="demo-image__preview" style="text-align: center">
        <el-image
          style="width: 60%; height: 60%;"
          :src="srcList[0]"
          :preview-src-list="srcList">
        </el-image>
      </div>
      <div style="margin-top: 10px;background-color: red" class="code">
          <div style="height: 5px"></div>
          <span class="order" style="color: white;"><span style="font-size: 30px">￥{{productInfo.price}}</span></span>
          <div style="height: 5px"></div>
        <div class="code" style="background-color: white">
          <div style="height: 5px"></div>
          <div class="order" style="margin-right: 20px;color: #524f4f;margin-left: 77%">库存:{{productInfo.stock}}</div>
          <div class="order" style="font-size: 20px;">{{productInfo.name}}<span style="margin-left: 20px">{{productInfo.text}}</span></div>
          <div style="height: 15px"></div>
          <div class="order" style="color: #8c939d;display: flex;text-align: center">
            <li class="el-icon-check" style="width: 33%">推荐</li>
            <li class="el-icon-bottom-right" style="width: 33%">降价提醒</li>
            <li class="el-icon-share" style="width: 33%">分享</li>
          </div>
          <div style="height: 5px"></div>
        </div>
      </div>

      <div class="code" style="margin-top: 10px">
        <div style="height: 5px"></div>
        <div class="order" style="font-size: 20px;margin-bottom: 20px">商品规格</div>
        <div class="order" style="color: #8c939d;">颜色:
          <el-radio-group v-model="color" size="small">
            <el-radio-button v-for="i in productInfo.colorList" :key="i" :label="i" border>{{i}}</el-radio-button>
          </el-radio-group>
        </div>
        <div class="order" style="color: #8c939d;">尺寸:
          <el-radio-group v-model="size" size="small">
            <el-radio-button v-for="i in productInfo.sizeList" :key="i" :label="i" border>{{i}}</el-radio-button>
          </el-radio-group>
        </div>
        <div style="height: 5px"></div>
      </div>

      <div class="code" style="margin-top: 10px"><div style="height: 5px"></div>
        <div class="order" style="font-size: 20px;margin-bottom: 20px">商品评价(100+)</div>
        <div class="order" >
          <div class="demo-basic--circle" style="display: inline-block">
            <el-avatar size="medium" icon="el-icon-user-solid"></el-avatar>
          </div>
          <div style="display: inline-block;margin-left: 10px">匿名用户<br/><span style="color: #8c939d;font-size: 13px">9天前</span></div>
          <div style="margin-top: 10px">
            外观材质：还不错，随便买的，没想到穿起来还不错，物美价廉。
          </div>
        </div>
        <div style="height: 5px"></div>
        <div class="order" >
          <div class="demo-basic--circle" style="display: inline-block">
            <el-avatar size="medium" icon="el-icon-user-solid"></el-avatar>
          </div>
          <div style="display: inline-block;margin-left: 10px">匿名用户<br/><span style="color: #8c939d;font-size: 13px">12天前</span></div>
          <div style="margin-top: 10px">
            东西收到了，没想到下单两天就到货了，质量很好，试穿了一下，东西很好，价格也不贵。
          </div>
        </div>
        <div style="height: 5px"></div>
      </div>
      <el-drawer
        title="购买商品"
        :visible.sync="drawer"
        :direction="direction"
        size="60%"
      >

        <div class="order" style="font-size: 20px"> 配送地址:<br/>
          <span @click="changeAddress()" style="margin-top: 10px;width: 100%" class="el-icon-location-information">{{nowAddress.address}}
            <span class="el-icon-arrow-right" style="position: absolute;right: 20px"></span>
          </span>
        </div>
        <el-divider></el-divider>
        <div class="order" style="margin-left: 10px">颜色:
          <el-radio-group v-model="color" size="small">
            <el-radio-button v-for="i in productInfo.colorList" :key="i" :label="i" border>{{i}}</el-radio-button>
          </el-radio-group>
        </div>
        <el-divider></el-divider>
        <div class="order" style="margin-left: 10px">尺寸:
          <el-radio-group v-model="size" size="small">
            <el-radio-button v-for="i in productInfo.sizeList" :key="i" :label="i" border>{{i}}</el-radio-button>
          </el-radio-group>
        </div>
        <el-divider></el-divider>
        <span style="width: 100%;margin-left: 10px">购买数量
            <el-input-number style="position: absolute;right: 20px" size="small" v-model="num" :min="1" :max="productInfo.stock" label="描述文字"></el-input-number>
        </span>
        <el-divider></el-divider>
        <div style="text-align: center">
          总价格:<span style="color: red;font-size: 20px">￥{{productInfo.price*num}}</span>
        </div>
        <el-button class="footer" style="justify-content: center;height: 40px" @click="orderAdd()">购买</el-button>
      </el-drawer>
      <el-dialog
        title="选择地址"
        :visible.sync="drawerAddress"
        width="90%"
      ><AddressSelect :address-list="addressList" v-on:close="close"></AddressSelect></el-dialog>
      <div style="height: 100px"></div>
      <div class="footer">
        <div class="title" ><span class="el-icon-headset"></span></div>
        <div class="title" ><span class="el-icon-star-off"></span></div>
        <div class="title" @click="addShopping">加入购物车</div>
        <div class="title" :style="productInfo.status===1?'display:none':''" @click="drawer=true">立即购买</div>
        <div class="title" :style="productInfo.status===1?'color:red':'display:none;'">已下架</div>
      </div>
    </div>
  </div>
</template>

<script>
import AddressSelect from "../components/User/AddressSelect";
export default {
  name: "ProductInfo",
  components:{AddressSelect},
  data(){
    return{
      color:'',
      size:'',
      productId:this.$route.query.id,
      productInfo:'',
      imgPath:'/api/img/download?name=',
      srcList: [],
      drawerAddress:false,
      drawer: false,
      direction: 'btt',
      num: 1,
      addressList:[],
      nowAddress:'',
    }
  },
  created() {
    if(this.productId===null)
      console.log(1)
    else{
      let url="/api/product/info/"+this.productId
      this.$axios.get(url).then(res=>{
        if(res.data.code===1){
          this.productInfo=res.data.data
          this.productInfo.colorList=this.productInfo.colorList.split(',')
          this.productInfo.sizeList=this.productInfo.sizeList.split(',')
          this.srcList.push(this.imgPath+this.productInfo.img)
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
        }
      })
    }
  },
  methods:{
    close(flag,item){
      this.nowAddress=item
      this.drawerAddress=flag
    },
    back(){
      this.$router.go(-1)
    },
    changeAddress(){
      this.drawerAddress=true
    },
    addShopping(){
      let data={}
      data.userId=JSON.parse(localStorage.getItem('Info')).id
      data.productId=this.productId
      this.$axios.post("/api/shoppingCart/save",data).then(res=>{
        if(res.data.code===1)
          this.$message({
            type: 'success',
            message: res.data.data,
            duration:1000
          });
      })
    },
    orderAdd(){
      this.$confirm("即将为您下单。是否继续?", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if(this.color===''||this.size===''){
          this.$message({
            type: 'error',
            message: "请勾选商品规格",
            duration:1000
          });
        }else{

          let orders={}
          orders.userId=JSON.parse(localStorage.getItem('Info')).id
          orders.addressId=this.nowAddress.id
          orders.amount=this.num*this.productInfo.price
          orders.orderProduct=[]

          let orderProduct={}
          orderProduct.typeId=this.productInfo.typeId
          orderProduct.productId=this.productInfo.id
          orderProduct.name=this.productInfo.name
          orderProduct.img=this.productInfo.img
          orderProduct.price=this.productInfo.price
          orderProduct.number=this.num
          orderProduct.size=this.size
          orderProduct.color=this.color
          orders.orderProduct.push(orderProduct)

          this.$axios.post("/api/orders/save",orders).then(res2=>{
            if (res2.data.code === 1) {
              this.$router.push('/infoPage')
            }else {
              this.$message({
                type: 'error',
                message: res2.data.msg,
                duration:1000
              });
            }
          })

        }
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
.order{
  margin: 10px 10px;
}
.code{
  border-radius: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}
.footer{
  height: 60px;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: #eaeaea;
  display: flex;
}
.title{
  height: 100%;
  width: 25%;
  color: black;
  display: flex; /**/
  justify-content: center; /*水平居中*/
  align-items: Center; /*垂直居中*/
}
</style>
<style>
.el-message-box {
  width: 300px;
}
</style>
