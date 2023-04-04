<template>
  <div style="border: 1px solid #ee3c3c;width: 380px;margin: 10% auto">
    <el-form style="width: 300px;margin-top: 15px" :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="账号:" prop="user">
        <el-input v-model="ruleForm.user" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item  label="密码:" prop="pass">
        <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
      </el-form-item>
      <div style="height: 30px;">
        <input class="link" name="remember" type="checkbox" id="cookie" v-model="checked"><label for="cookie">记住密码</label>
      </div>

      <el-form-item>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>

export default {

  name:"login",
  data() {
    var validateUser = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入账号!'));
      } else {
        callback();
      }

    };
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码!'));
      } else {
        callback();
      }
    };
    return {
      user:{
        id:'',
        name:''
      },
      checked:'',
      ruleForm: {
        user: '',
        pass: '',
      },
      rules: {
        user: [
          { validator: validateUser, trigger: 'blur' }
        ],
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
      }
    };
  },
  created() {
    let a=this.$cookies.get("localhost_login")
    if (a==null)
      this.checked=false
    else {
      this.ruleForm.user=a.name
      this.ruleForm.pass=a.password
      this.checked=true
    }
  },
  methods: {
    submitForm(formName) {
      let cook=this.checked
      let data = {
        'username':this.ruleForm.user,
        'password':this.ruleForm.pass
      }
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post("/api/employee/login",data)
            .then(res => {
              if(res.data.data!=null){
                if(cook)
                  this.$cookies.set("localhost_login",data,60 )
                this.$store.dispatch("ElementName",res.data.data)
                this.user.id=res.data.data.id;
                this.user.name=res.data.data.name;
                localStorage.setItem("ElementInfo",JSON.stringify(this.user));
                sessionStorage.setItem("ElementToken",this.user.id)
                this.$router.push("/Employee");
              }else{
                alert(res.data.msg)
              }
            })
        } else {
          console.log('1 error submit!!');
          console.log(this.f);
          return false;
        }
      });
    },
  }
}
</script>

<style>
.link{
  margin-left:60px;
  margin-bottom: 20px;
  font-size: 10px;
  /*float: left;*/
  /*display: inline-block;*/
}

</style>
