<template>
  <div style="margin-top: 40%">
    <el-form style="width: 300px;margin-top: 15px;text-align: center" :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="手机号:" prop="phone">
        <el-input v-model="ruleForm.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item style="" label="验证码:" prop="code">
        <div style="display: flex">
          <el-input  v-model="ruleForm.code" autocomplete="off"></el-input>
          <el-button :disabled="time>0" type="primary" @click="getMsg('ruleForm')">
            <span :style="type===false?'':'display:none'">获取验证码</span>
            <span :style="type===false?'display:none':''">{{time}}s后可重发</span>
          </el-button>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>

export default {
  name:"login",
  watch:{
    time(now){
      if(now>0){
        setTimeout(()=>{
          this.time--
        },1000)
      }else {
        this.type=false
      }
    }
  },
  data() {
    var validateUser = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入手机号!'));
      } else {
        callback();
      }

    };
    var validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入验证码!'));
      } else {
        callback();
      }
    };
    return {
      time:0,
      type:false,
      user:{
        id:'',
        phone:''
      },
      ruleForm: {
        phone: '',
        code: '',
      },
      rules: {
        phone: [
          { validator: validateUser, trigger: 'blur' }
        ],
        code: [
          { validator: validateCode, trigger: 'blur' }
        ],
      }
    };
  },
  methods: {
    getMsg(formName){
      let data = {
        'phone':this.ruleForm.phone
      }
      if(this.ruleForm.phone.length!==11){
        this.$message({
          message: "请输入正确手机号",
          type: 'error',
          duration: 1000
        })
      }else{
        this.$axios.post("/api/user/sendMsg",data).then(res=>{
          if(res.data.data!=null){
            this.$message({
              message: "获取成功",
              type: 'success',
              duration: 1000
            })
            this.time=60
            this.type=true
          }
        })
      }
    },
    submitForm(formName) {
      let data = {
        'phone':this.ruleForm.phone,
        'code':this.ruleForm.code
      }
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("/api/user/login",data)
            .then(res => {
              if(res.data.code===1){
                this.$store.dispatch("username",res.data.data)
                this.user.id=res.data.data.id;
                this.user.phone=res.data.data.phone;
                localStorage.setItem("Info",JSON.stringify(this.user))
                sessionStorage.setItem("token",this.user.id)
                this.$message({
                  message: "登录成功",
                  type: 'success',
                  duration: 1000
                })
                this.$router.push("/user");
              }else{
                this.$message({
                  message: res.data.msg,
                  type: 'error',
                  duration: 1000
                })
              }
            })
        } else {
          return false;
        }
      });
    },
  }
}
</script>
