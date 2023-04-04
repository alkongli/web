<template>
  <div style="width: 100%">
    <el-form :inline="true"  @keyup.enter.native="">
      <el-form-item>
        <el-input v-model="text" placeholder="账号名" clearable></el-input>
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
        <EmployeeForm :user-info="userInfo" v-on:close="close"></EmployeeForm>
      </div>
    </el-dialog>
    <el-table
      :data="tableData"
      border
      style="width: 100%"
    >
      <el-table-column label="姓名" width="100">
        <template slot-scope="scope">{{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="账号名" width="100">
        <template slot-scope="scope">{{ scope.row.username }}
        </template>
      </el-table-column>
      <el-table-column label="手机号" width="180">
        <template slot-scope="scope">{{ scope.row.phone }}
        </template>
      </el-table-column>
      <el-table-column label="性别" width="100">
        <template slot-scope="scope">{{scope.row.sex=='1'?'男':'女'}}</template>
      </el-table-column>
      <el-table-column label="账号状态" width="100">
        <template slot-scope="scope">{{scope.row.status=='0'?'正常':'封号'}}</template>
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
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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
import EmployeeForm from "./EmployeeForm";
export default {
  inject: ['reload'],
  name: "Employee",
  components:{EmployeeForm},
  data() {
    return {
      userInfo:'',
      title:'',
      Visible: false,
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
      this.$axios.get("/api/employee/page",{
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
      this.$axios.get("/api/employee/page",{
        params:{
          page:this.pageInfo.page,
          pageSize:this.pageInfo.pageSize,
          username:this.text
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
      this.title="添加用户"
      this.userInfo={"id":"","name":"","username":"","password": "123456","sex":"","phone":"", "status": "0"}
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
      this.title="修改用户"
      this.Visible = true

    },
    handleDelete(index, row) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete("/api/employee", {
          params:{
            id:row.id
          }
        }).then((res)=>{
          if(res.data.code===1){
            this.$message({
              message:res.data.data,
              type: 'success',
              duration:2000
            });
          }
        })
        this.GetPage()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    }
  }
}
</script>

<style scoped>

</style>
