<template>
  <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <el-form-item label="姓名" prop="name">
      <el-input v-model="ruleForm.name"></el-input>
    </el-form-item>
    <el-form-item label="账号名" prop="username">
      <el-input v-model="ruleForm.username"></el-input>
    </el-form-item>
    <el-form-item :style="userInfo.id===''?'':'display:none'" label="密码" prop="password">
      <el-input v-model="ruleForm.password"></el-input>
    </el-form-item>
    <el-form-item label="性别" prop="sex" >
      <el-radio-group v-model="ruleForm.sex">
        <el-radio label="0">女</el-radio>
        <el-radio label="1">男</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="手机号" prop="phone">
      <el-input v-model="ruleForm.phone"></el-input>
    </el-form-item>
    <el-form-item :style="userInfo.id===''?'display:none':''" label="密码初始化" prop="status">
      <el-radio-group v-model="psd">
        <el-radio label="123456">初始化</el-radio>
        <el-radio label="1">不初始化</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item :style="userInfo.id===''?'display:none':''" label="账号状态" prop="status">
      <el-radio-group v-model="ruleForm.status">
        <el-radio label="0">正常</el-radio>
        <el-radio label="1">封号</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item >
      <el-button :style="userInfo.id===''?'':'display:none'" type="primary" @click="submitFormAdd('ruleForm')">添加用户</el-button>
      <el-button :style="userInfo.id===''?'display:none':''" type="primary" @click="submitFormUpd('ruleForm')">更新用户</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name:'UserForm',
  props:{
    userInfo:''
  },
  watch:{
    userInfo(now){
      now.status=now.status.toString()
      now.sex=now.sex.toString()
      this.ruleForm=now
    }
  },
  data() {
    return {
      psd:'1',
      ruleForm: {
        sex:this.userInfo.sex.toString(),
        name: this.userInfo.name,
        username:this.userInfo.username,
        password:this.userInfo.password,
        status:this.userInfo.status.toString(),
        phone:this.userInfo.phone
      },
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入账号名', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    submitFormAdd(formName) {
      let data = this.ruleForm
      let axios=this.$axios
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post('/api/employee/save',data).then((res)=>{
            if(res.data.code===1) {
              this.$emit("close",false)
              this.$message({
                message: "添加成功",
                type: 'success',
                duration: 2000
              })
            } else {
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
    submitFormUpd(formName) {
      let data = this.ruleForm
      data.id=this.userInfo.id
      let axios=this.$axios
      if(this.psd==="123456")
        data.password=this.psd
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.put("/api/employee",data).then((res)=>{
            if(res.data.code===1) {
              this.$emit("close",false)
              this.$message({
                message: "修改成功",
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
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
  }
}
</script>
