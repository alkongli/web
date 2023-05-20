<template>
  <div style="width: 100%">
    <el-form :inline="true"  @keyup.enter.native="">
      <el-form-item>
        <el-input v-model="text" placeholder="商品名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getByName">查询</el-button>
        <el-button  type="primary" @click="userAdd">新增</el-button>
      </el-form-item>
    </el-form>
    <el-dialog
      :visible.sync="Visible"
      width="50%"
      :title="title"
    >
      <div style="text-align:center">
        <!--需要弹出的内容部分-->
        <SecKillForm :user-info="userInfo" v-on:close="close"></SecKillForm>
      </div>
    </el-dialog>
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column label="商品名" width="100">
        <template slot-scope="scope">{{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="商品图片" width="120">
        <template slot-scope="scope">
          <div class="demo-image">
          <el-image
            style="width: 100px; height: 100px"
            :src="imgPath+scope.row.img"
            fit="cover"></el-image>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="商品类型" width="100">
        <template slot-scope="scope">{{ scope.row.type }}
        </template>
      </el-table-column>

      <el-table-column type="expand" label="商品描述" width="100">
        <template slot-scope="scope">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item style="margin: 20px 20px">
              <span> {{ scope.row.text }}</span>
            </el-form-item>
          </el-form>

        </template>
      </el-table-column>

      <el-table-column label="商品库存" width="100">
        <template slot-scope="scope">{{ scope.row.stock }}
        </template>
      </el-table-column>
      <el-table-column label="商品原价(元)" width="120">
        <template slot-scope="scope">{{ scope.row.price }}
        </template>
      </el-table-column>
      <el-table-column label="商品折扣价(元)" width="120">
        <template slot-scope="scope">{{scope.row.price*scope.row.discount}}
        </template>
      </el-table-column>
      <el-table-column label="商品颜色列表" width="120">
        <template slot-scope="scope">{{ scope.row.colorList }}
        </template>
      </el-table-column>
      <el-table-column label="商品尺寸列表" width="120">
        <template slot-scope="scope">{{ scope.row.sizeList }}
        </template>
      </el-table-column>
      <el-table-column label="商品状态" width="120">
        <template slot-scope="scope"><span :style="scope.row.status===0?'':'color:red'">{{scope.row.status===0?"秒杀中":"已下架"}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="创建日期"
        width="180">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="更新日期"
        width="180">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="更新员工"
        width="120">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.employeeId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" >
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5, 10, 20, 40]"
        :page-size="5"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageInfo.total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import SecKillForm from "./SecKillForm";
export default {
  inject: ['reload'],
  name: "SecKill",
  components:{SecKillForm},
  data() {
    return {
      imgPath:'/api/img/download?name=',
      userInfo:'',
      title:'',
      Visible: false,
      text:'',
      currentPage: 1,
      tableData:[],
      pageInfo:{
        total:0,
        page:1,
        pageSize:5
      },
    }
  },
  created() {
    this.GetPage()
  },
  methods:{
    GetPage(){
      this.$axios.get("/api/product/page",{
        params:{
          page:this.pageInfo.page,
          pageSize:this.pageInfo.pageSize,
          secKill:1
        }
      }).then((res)=>{
        this.pageInfo.total=res.data.data.total
        this.tableData=res.data.data.records

      })
    },
    close(flag){
      this.Visible=flag
      this.GetPage()
    },
    getByName(){
      this.$axios.get("/api/product/page",{
        params:{
          page:this.pageInfo.page,
          pageSize:this.pageInfo.pageSize,
          secKill:1,
          name:this.text
        }
      }).then((res)=>{
        if(res.data.code===1) {
          this.pageInfo.total=res.data.data.total
          this.tableData=res.data.data.records
          this.$message({
            message: "搜索成功",
            type: 'success',
            duration: 1000
          })
        }
      })
    },
    userAdd(){
      this.title="添加商品"
      this.userInfo={"id":"","img":"0","colorList":"黑色","sizeList":"XS,S,M,L,XL,XXL"}
      this.Visible = true
    },
    handleSizeChange(val) {
      this.pageInfo.pageSize=val
      this.GetPage()
    },
    handleCurrentChange(val) {
      this.pageInfo.page=val
      this.GetPage()
    },
    handleEdit(index, row) {
      this.userInfo=row
      this.title="修改商品"
      this.Visible = true

    },
  }
}
</script>

<style scoped>

</style>
