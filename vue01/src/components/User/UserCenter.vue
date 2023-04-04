<template>
  <div>
    <el-dialog
      title="修改个人信息"
      :visible.sync="updateInfo"
      width="90%"
    >
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="用户昵称" prop="name">
          <el-input v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="ruleForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex" >
          <el-radio-group v-model="ruleForm.sex">
            <el-radio label="0">女</el-radio>
            <el-radio label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item >
          <el-button type="primary" @click="submitFormUpd('ruleForm')">修改信息</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <div style="margin-top: 30px;margin-left: 20px;display: flex;">
      <div @click="updateInfo=true">
        <el-avatar  :size="80">{{userInfo.name}}</el-avatar>
      </div>
      <div style="margin:20px 20px">
        用户名:{{userInfo.name}}<br>
        手机号:{{userInfo.phone}}
      </div>
    </div>
    <div>
      <el-divider></el-divider>
      <el-button style="width: 100%;margin-bottom: 10px" @click="address()">我的地址</el-button>
      <el-button style="width: 100%;margin: 0 0" @click="orders()">我的订单</el-button>
      <div style="height: 10px"></div>
      <el-button style="width: 100%;margin: 0 0" @click="loginOut()">退出登录</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "UserMsg",
  data(){
    return{
      userInfo:'',
      updateInfo:false,
      ruleForm: {
        sex:'',
        name: '',
        phone:'',
      },
      rules: {
        name: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ],
      }
    }
  },
  created() {
    this.getUserInfo()
  },
  methods:{
    getUserInfo(){
      this.$axios.get("/api/user/info",{
        params:{
          phone:JSON.parse(localStorage.getItem("Info")).phone
        }
      }).then(res=>{
        this.userInfo=res.data.data
        this.ruleForm.sex=this.userInfo.sex.toString()
        this.ruleForm.name=this.userInfo.name
        this.ruleForm.phone=this.userInfo.phone
      })
    },
    submitFormUpd(){
      let data=this.ruleForm
      data.id=this.userInfo.id
      this.$axios.put("/api/user",data).then(res=>{
        if(res.data.code===1){
          this.$message({
            message: res.data.data,
            type: 'success',
            duration: 1000
          })
          this.getUserInfo()
          this.updateInfo=false
        }
      })
    },
    address(){
      this.$router.push({path:'/address',query:{userId:this.userInfo.id}})
    },
    orders(){
      this.$router.push("/orders")
    },
    loginOut(){
      this.$confirm("即将退出登录。是否继续?", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('Info')
        sessionStorage.removeItem('token')
        this.$router.push('/user/login')
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

</style>
<style>
.el-message-box {
  width: 300px;
}
</style>
