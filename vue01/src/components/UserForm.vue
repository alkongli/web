<template>
  <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <el-form-item label="账号" prop="name">
      <el-input v-model="ruleForm.name"></el-input>
    </el-form-item>
    <el-form-item label="性别"prop="sex" >
      <el-radio-group v-model="ruleForm.sex">
        <el-radio label="0">女</el-radio>
        <el-radio label="1">男</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="手机号" prop="phone">
      <el-input v-model="ruleForm.phone"></el-input>
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
      ruleForm: {
        sex:this.userInfo.sex.toString(),
        name: this.userInfo.name,
        status:this.userInfo.status.toString(),
        phone:this.userInfo.phone
      },
      rules: {
        name: [
          { required: true, message: '请输入账号', trigger: 'blur' }
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
          axios.post('/api/user/save',data).then((res)=>{
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
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.put("/api/user",data).then((res)=>{
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
