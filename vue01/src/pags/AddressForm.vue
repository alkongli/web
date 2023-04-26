<template>
  <div>
    <span @click="back()" style="margin:15px 0" class="el-icon-arrow-left">{{formInfo.id===''?'添加':'更新'}}地址</span>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="收货人" prop="consignee">
        <el-input v-model="ruleForm.consignee"></el-input>
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
      <el-form-item label="收货地址" prop="address">
        <el-input v-model="ruleForm.address"></el-input>
      </el-form-item>
      <el-form-item >
        <el-button :style="formInfo.id===''?'':'display:none'" type="primary" @click="submitFormAdd('ruleForm')">添加地址</el-button>
        <el-button :style="formInfo.id===''?'display:none':''" type="primary" @click="submitFormUpd('ruleForm')">更新地址</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name:'AddressForm',
  data() {
    return {
      formInfo:this.$route.query.formInfo,
      ruleForm: {
        sex:this.$route.query.formInfo.sex.toString(),
        consignee: this.$route.query.formInfo.consignee,
        phone:this.$route.query.formInfo.phone,
        address:this.$route.query.formInfo.address
      },
      rules: {
        consignee: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' }
        ],
      }
    };
  },
  methods: {
    submitFormAdd(formName) {
      let data = this.ruleForm
      data.userId=JSON.parse(localStorage.getItem("Info")).id
      let axios=this.$axios
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post('/api/address/save',data).then((res)=>{
            if(res.data.code===1) {
              this.$router.go(-1)
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
    back(){
      this.$router.go(-1)
    },
    submitFormUpd(formName) {
      let data = this.ruleForm
      data.id=this.$route.query.formInfo.id
      let axios=this.$axios
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.put("/api/address",data).then((res)=>{
            if(res.data.code===1) {
              this.$router.go(-1)
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
