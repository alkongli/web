<template>
  <el-container>
    <el-main>
      <div v-for="item in productList" :key="item.id" style="height:140px;border-radius: 15px;box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);">
        <div style="margin: 10px 10px;" class="wrapper" >
          <div style="margin-left: 10px;margin-top: 20px" class="box1" @click="getInfo(item.id,item.typeId)">{{item.name}}</div>
          <div class="box2" @click="getInfo(item.id,item.typeId)">
            <el-image
              style="width: 120px; height: 120px;border-radius: 15px;margin-top: 10px"
              :src="imgPath+item.img"
              fit="cover"></el-image>
          </div>
          <div class="box3" :style="item.typeId==='1'?'display:none':''" @click="addShopping(item.id)"
               style="background-color: red;border-radius: 10px;color: white">加入购物车</div>
          <div class="box3" :style="item.typeId==='1'?'':'display:none'"
               style="border-radius: 10px;color: red;font-size: 25px">
            <span v-for="i in $store.state.discount" :key="i.name" v-if="item.discount===i.value">{{i.name}}</span>
          </div>
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "SearchResult",
  props:{
    productList:[]
  },
  data() {
    return {
      imgPath:'/api/img/download?name=',
    }
  },
  methods:{
    getInfo(id,typeId){
      if(typeId!=='1')
        this.$router.push({path:"/productInfo",query:{id:id}})
      else
        this.$router.push({path:"/secKillPage",query:{id:id}})
    },
    addShopping(productId){
      let data={}
      data.userId=JSON.parse(localStorage.getItem('Info')).id
      data.productId=productId
      this.$axios.post("/api/shoppingCart/save",data).then(res=>{
        if(res.data.code===1)
          this.$message({
            type: 'success',
            message: res.data.data,
            duration:1000
          });
      })
    },
  }
}
</script>

<style scoped>
.wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 80px 40px;
}

.box1 {
  grid-row: 1;
  grid-column: 2 / 4;
}

.box2 {
  grid-row: 1 / 3;
  grid-column: 1;
}

.box3 {
  margin-left: 5px;
  grid-row: 2;
  grid-column:2/4;
  display: flex; /**/
  justify-content: center; /*水平居中*/
  align-items: Center; /*垂直居中*/
}
</style>
