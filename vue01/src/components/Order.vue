<template>
  <div style="width: 100%">
    <el-form :inline="true"  @keyup.enter.native="">
      <el-form-item>
        <el-input v-model="text" placeholder="订单号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="getByName">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column label="订单号" width="200">
        <template slot-scope="scope">{{ scope.row.id }}
        </template>
      </el-table-column>

      <el-table-column label="订单状态" width="100">
        <template slot-scope="scope">{{ status[scope.row.status-1]}}
        </template>
      </el-table-column>
      <el-table-column label="下单用户id" width="100">
        <template slot-scope="scope">{{scope.row.userId}}</template>
      </el-table-column>
      <el-table-column label="地址id" width="100">
        <template slot-scope="scope">{{scope.row.addressId}}</template>
      </el-table-column>
      <el-table-column
        label="下单时间"
        width="180">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.orderTime }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="结账时间"
        width="180">
        <template slot-scope="scope">
          <span style="margin-left: 10px">{{ scope.row.checkoutTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" width="100">
        <template slot-scope="scope">{{scope.row.remark}}</template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            :style="scope.row.status===2?'':'display:none'"
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">派送</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" >
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[4, 10, 20, 40]"
        :page-size="10"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageInfo.total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  inject: ['reload'],
  name: "Order",
  data() {
    return {
      status:['待付款','待派送','待签收','已完成','已取消'],
      userInfo:'',
      text:'',
      currentPage: 1,
      tableData:[],
      pageInfo:{
        total:0,
        page:1,
        pageSize:10
      },
    }
  },
  created() {
    this.GetPage()
  },
  methods:{
    GetPage(){
      this.$axios.get("/api/orders/page",{
        params:{
          page:this.pageInfo.page,
          pageSize:this.pageInfo.pageSize
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
      this.$axios.get("/api/orders/page",{
        params:{
          page:this.pageInfo.page,
          pageSize:this.pageInfo.pageSize,
          id:this.text
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
      this.userInfo.status=3;
      let date=this.userInfo
      this.$axios.put("/api/orders",date).then((res)=>{
        if(res.data.code===1) {
          this.$message({
            message: "派送成功",
            type: 'success',
            duration: 2000
          })
        }else {
          this.$message({
            message: res.data.msg,
            type: 'error',
            duration: 2000
          })
        }
      })

    }
  }
}
</script>

<style scoped>

</style>
