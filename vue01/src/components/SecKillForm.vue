<template>
  <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <el-upload style="margin-bottom: 20px"
      class="avatar-uploader"
      action="/api/img/upload"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload">
      <img v-if="imageUrl" :src="imageUrl" class="avatar">
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
    <el-form-item label="商品名" prop="name">
      <el-input v-model="ruleForm.name"></el-input>
    </el-form-item>
    <el-form-item label="商品描述">
      <el-input autosize type="textarea" v-model="ruleForm.text"></el-input>
    </el-form-item>
    <el-form-item label="商品颜色" prop="colorList">
      <div>
        <el-tag
          :key="tag"
          v-for="tag in dynamicTags"
          closable
          :disable-transitions="false"
          @close="handleClose(tag)">
          {{tag}}
        </el-tag>
        <el-input
          class="input-new-tag"
          v-if="inputVisible"
          v-model="inputValue"
          ref="saveTagInput"
          size="small"
          @keyup.enter.native="handleInputConfirm"
          @blur="handleInputConfirm"
        >
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 添加</el-button>
      </div>
    </el-form-item>

    <el-form-item label="商品尺寸" prop="sizeList">
      <el-checkbox-group v-model="ruleForm.sizeList">
        <el-checkbox v-for="size in sizes" :label="size" :key="size">{{size}}</el-checkbox>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item label="商品库存" prop="stock">
      <el-input v-model="ruleForm.stock"></el-input>
    </el-form-item>
    <el-form-item label="商品价格" prop="price">
      <el-input v-model="ruleForm.price"></el-input>
    </el-form-item>
    <el-form-item :style="ruleForm.seckill===true?'display:none':''" label="商品折扣" prop="discount">
      <el-radio-group v-model="ruleForm.discount">
        <el-radio v-for="i in $store.state.discount" :key="i.name" :label="i.value">{{i.name}}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item :style="ruleForm.seckill===true?'display:none':''" label="开始时间" prop="time">
      <el-select  v-model="ruleForm.time" placeholder="请选择开始时间">
        <el-option label="立刻开始" value="0"></el-option>
        <el-option label="一分钟后" value="60"></el-option>
        <el-option label="五分钟后" value="300"></el-option>
        <el-option label="十分钟后" value="600"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item :style="ruleForm.seckill===true?'':'display:none'" label="商品类型" prop="type" >
      <el-select v-model="ruleForm.type" placeholder="请选择商品类型">
        <el-option v-for="item in productType" :key="item.id" :label="item.type" :value="item.id" :style="item.id==='1'?'display:none':''"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item :style="userInfo.id===''?'display:none':''" label="取消秒杀">
      <el-switch v-model="ruleForm.seckill"></el-switch>
    </el-form-item>
    <el-form-item >
      <el-button :style="userInfo.id===''?'':'display:none'" type="primary" @click="submitFormAdd('ruleForm')">添加商品</el-button>
      <el-button :style="userInfo.id===''?'display:none':''" type="primary" @click="submitFormUpd('ruleForm')">修改商品</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name:'ProductForm',
  props:{
    userInfo:''
  },
  watch:{
    userInfo(now){
      this.ruleForm=now
      this.dynamicTags=now.colorList.split(',')
      if(typeof (now.sizeList)===typeof (now.colorList))
        this.ruleForm.sizeList=now.sizeList.split(',')
      if(now.img!=="0")
        this.imageUrl="/api/img/download?name="+now.img
      else
        this.imageUrl=""
    }
  },

  data() {
    return {
      dynamicTags: this.userInfo.colorList.split(','),
      inputVisible: false,
      inputValue: '',
      sizes:this.$store.state.sizes,
      imageUrl: '',
      productType:{},
      ruleForm: {
        seckill:false,
        price:this.userInfo.price,
        discount:this.userInfo.discount,
        name: this.userInfo.name,
        stock:this.userInfo.stock,
        type:this.userInfo.type,
        text:this.userInfo.text,
        colorList:this.userInfo.colorList,
        sizeList:this.userInfo.sizeList.split(',')
      },
      rules: {
        colorList: [
          { required: true, message: '请添加商品颜色', trigger: 'blur' }
        ],
        sizeList: [
          { required: true, message: '请选择商品尺寸', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入商品名', trigger: 'blur' }
        ],
        price: [
          { required: true, message: '请选择商品单价', trigger: 'blur' }
        ],
        stock: [
          { required: true, message: '请输入商品库存', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.$axios.get('/api/productType/list').then((res)=>{
      this.productType=res.data.data
    })
    if(this.userInfo.img!=="0")
      this.imageUrl="/api/img/download?name="+this.userInfo.img
  },
  methods: {
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
      this.ruleForm.colorList=this.dynamicTags.toString()
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.dynamicTags.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = '';
      this.ruleForm.colorList=this.dynamicTags.toString()
    },

    submitFormAdd(formName) {
      let data = this.ruleForm
      data.typeId=1
      data.sizeList=this.ruleForm.sizeList.toString()
      data.employeeId=JSON.parse(localStorage.getItem("ElementInfo")).id
      let axios=this.$axios
      this.$refs[formName].validate((valid) => {
        if (valid) {
            axios.post('/api/secKill/add',data).then((res)=>{
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
      data.sizeList=this.ruleForm.sizeList.toString()
      data.employeeId=JSON.parse(localStorage.getItem("ElementInfo")).id
      data.typeId=1
      if(this.ruleForm.type>0){
        data.typeId=this.ruleForm.type
        data.discount=1
        data.time=0
      }
      let axios=this.$axios
      let url='/api/secKill/update'
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post(url,data).then((res)=>{
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
    handleAvatarSuccess(res, file) {
      this.imageUrl = "/api/img/download?name="+res.data;
      this.ruleForm.img=res.data
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 1;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 1MB!');
      }
      return isJPG && isLt2M;
    }
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
}
.avatar {
  width: 150px;
  height: 150px;
  display: block;
}
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
