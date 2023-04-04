<template>
  <div>
    <div style="margin-top: 15px;margin-bottom: 5px">
      <el-input  placeholder="请输入内容" v-model="input" class="input-with-select">
        <el-select style="width: 100px"  v-model="select" slot="prepend" placeholder="请选择">
          <el-option label="全部" value="0"></el-option>
          <el-option v-for="item in productType" :key="item.id" :label="item.type" :value="item.id"></el-option>
        </el-select>
        <el-button slot="append" icon="el-icon-search" @click="search()"></el-button>
      </el-input>
    </div>
    <div :style="searchList===true?'':'display:none'">
      <span @click="back()" style="margin:15px 0" class="el-icon-arrow-left">返回</span>
      <SearchResult  :product-list="productList"></SearchResult>
    </div>
    <ProductList :style="searchList===true?'display:none':''"></ProductList>


  </div>
</template>

<script>
import SearchResult from "./SearchResult";
import ProductList from "./ProductList";
export default {
  components:{SearchResult,ProductList},
  data() {
    return {
      searchList:false,
      input:'',
      select:'',
      imgPath:'/api/img/download?name=',
      productType:'',
      productList:[]
    }
  },
  created() {
    this.$axios.get('/api/productType/list').then((res)=>{
      this.productType=res.data.data
    })
    this.$axios.get("/api/product/typeId",{
      params:{
        typeId:1
      }
    }).then(res=>{
      this.productList=res.data.data
    })
  },
  methods:{
    search(){
      let typeId=this.select
      if(typeId==="0")
        typeId=null
      this.$axios.get("/api/product/list",{
        params:{
          typeId: typeId,
          name:this.input
        }
      }).then(res=>{
        this.productList=res.data.data
        this.searchList=true
      })
    },
    back(){
      this.$router.go(0)
    },
  }
};
</script>

<style>

</style>
