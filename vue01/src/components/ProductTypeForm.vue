<template>
  <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <el-form-item label="商品类型" prop="type">
      <el-input v-model="ruleForm.type"></el-input>
    </el-form-item>
    <el-form-item >
      <el-button :style="userInfo.id===''?'':'display:none'" type="primary" @click="submitFormAdd('ruleForm')">添加类型</el-button>
      <el-button :style="userInfo.id===''?'display:none':''" type="primary" @click="submitFormUpd('ruleForm')">更新类型</el-button>
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
      this.ruleForm=now
    }
  },
  data() {
    return {
      ruleForm: {
        type: this.userInfo.type,

      },
      rules: {
        type: [
          { required: true, message: '请输入商品类型', trigger: 'blur' }
        ],
      }
    };
  },
  methods: {
    submitFormAdd(formName) {
      let data = this.ruleForm
      let axios=this.$axios
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post('/api/productType/add',data).then((res)=>{
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
          axios.put("/api/productType",data).then((res)=>{
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
