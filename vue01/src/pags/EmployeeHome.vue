<template>
  <div>
    <el-dialog
      :visible.sync="Visible"
      width="50%"
      title="修改用户密码"
    >
      <div style="text-align:center">
        <!--需要弹出的内容部分-->
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="密码" prop="pass">
            <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
    <div class="top" >
      <div style="height: 10px;"></div>
      <span style="color: white;font-size: 30px;margin-left: 150px">快时尚限时折扣网站后台管理系统</span>
      <div style="display: flex;  align-items: Center;position: absolute;right: 30px">
        <div @click="Visible=true">
          <el-avatar size="large">{{nameInfo}}</el-avatar>
        </div>
        <el-button @click="loginOut" size="small" type="primary" round style="margin-left: 20px">退出登录</el-button>
      </div>

    </div>
    <div style=" display: flex;">
      <div >
        <!--  <Menu style="width: 180px;"></Menu>-->
        <div style="width: 180px;">
          <el-menu style="height: 800px;" background-color="#545c64"
                   text-color="#fff"
                   active-text-color="#409EFF"   :default-active="defaultAct" class="el-menu-vertical-demo" >
            <el-menu-item style="margin-top: 20px" :style="id==='1'?'':'display:none'" index="1" @click="click('Employee')">
              <span  slot="title">员工管理</span>
            </el-menu-item>
            <el-menu-item index="2" @click="click('User')">
              <span slot="title">用户管理</span>
            </el-menu-item>
            <el-menu-item index="3" @click="click('ProductType')">
              <span slot="title">商品类型</span>
            </el-menu-item>
            <el-menu-item index="4" @click="click('Product')">
              <span slot="title">商品管理</span>
            </el-menu-item>
            <el-menu-item index="5" @click="click('SecKill')">
              <span slot="title">秒杀管理</span>
            </el-menu-item>
            <el-menu-item index="6" @click="click('Order')">
              <span slot="title">订单管理</span>
            </el-menu-item>
          </el-menu>
        </div>
      </div>
      <component style="margin-top: 10px" :is="name" />
    </div>
  </div>
</template>

<script>
import User from "../components/User";
import Employee from "../components/Employee";
import ProductType from "../components/ProductType";
import Product from "../components/Product";
import SecKill from "../components/SecKill";
import Order from "../components/Order";
export default {
  name: "Home",
  components:{User,Employee,ProductType,Product,SecKill,Order},
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      name:'Employee',
      Visible:false,
      id:JSON.parse(localStorage.getItem('ElementInfo')).id,
      nameInfo:JSON.parse(localStorage.getItem('ElementInfo')).name,
      defaultAct:'1',
      ruleForm: {
        pass: '',
        checkPass: '',
      },
      rules: {
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ],
      }
    }
  },
  created() {
    if(this.id!=='1'){
      this.name='User'
      this.defaultAct='2'
    }
  },
  methods:{
    click( name){
      this.name=name
    },
    submitForm(formName) {
      let data={}
      data.id=this.id
      data.password=this.ruleForm.pass
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.put("/api/employee",data).then((res)=>{
            if(res.data.code===1) {
              this.$message({
                message: "修改成功,请重新登录",
                type: 'success',
                duration: 2000
              })
              this.$router.push('/employee/login')
            }else {
              this.$message({
                message: res.data.msg,
                type: 'error',
                duration: 2000
              })
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    loginOut(){
      this.$confirm("即将退出登录。是否继续?", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        sessionStorage.removeItem('ElementToken')
        this.$router.push('/employee/login')
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
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 150px;
  min-height: 550px;
}
.top{
  height:70px;
  background-color: #545c64;
  margin-top: 10px;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  display: flex;
  align-items: Center;
}
</style>
