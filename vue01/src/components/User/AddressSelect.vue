<template>
  <div>
    <div @click="useAddress(item)" class="code" style="margin-left: 20px;margin-bottom: 15px;" v-for="item in addressList" :key="item.id">
      <div style="height: 5px"></div>
      <div class="order"><span style="font-size: 18px">{{item.consignee}}</span>
          <span style="margin-left: 20px">{{item.phone}}</span></div>
      <div class="order el-icon-location-information" style="margin-top: 5px">{{item.address}}</div>
    </div>

  </div>
</template>

<script>
export default {
  name: "AddressSelect",
  props:{
    addressList:'',

  },
  methods:{
    useAddress(item){
      this.$emit("close",false,item)
    }
  }
}
</script>

<style scoped>
.code{
  border-radius: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}
.order{
  margin: 10px 10px;
}
</style>
