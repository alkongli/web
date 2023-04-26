<template>
<div>
  <div class="block">
    <el-date-picker
      v-model="dayValue"
      type="date"
      placeholder="修改展示日期"
      :picker-options="pickerOptions">
    </el-date-picker>
  </div>

  <el-card shadow="always" style="position: absolute;top:20%;left: 16%">
    <div style="text-align: center;width: 100%;margin-bottom: 10px;font-size: 20px">近七日商品种类销售情况</div>
    <div id="card1" style="height:500px;width: 600px"></div>
  </el-card>

  <el-card shadow="always" style="position: absolute;top:20%;left: 60%">
    <div style="text-align: center;width: 100%;margin-bottom: 10px;font-size: 20px">近三日平台销售情况</div>
    <div id="card2" style="height: 500px;width: 600px"></div>
  </el-card>

</div>
</template>

<script>
export default {
  name: "Monitoring",
  data() {
    return {
      dayValue:this.getNowDay(),
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      productType:'',
    }
  },
  watch:{
    dayValue(now){
      let year=now.getFullYear()+"-" //获取年

      let month=now.getMonth()+1+"-" //获取月

      let day=now.getDate()+""      //获取日

      if(now.getMonth()+1<10){          //判定月
        month = "0"+(now.getMonth()+1)+"-";
      }

      this.getData(this.getPassDay(7,year+month+day),this.getPassDay(3,year+month+day))

    }
  },
  created() {
    this.$axios.get('/api/productType/list').then((res)=>{
      this.productType=res.data.data
    })
    this.getData(this.getPassDay(7),this.getPassDay(3))

  },
  methods:{
    getData(day1,day2){
      let data={
        day1:day1,
        day2:day2
      }
      this.$axios.post('/api/orders/getOrderMonitor',data).then((res)=>{
        if(res.data.code===1){
          this.getLoadChar(day1,res.data.typeArray,day2,res.data.amountArray)
        }
        else {

        }
      })
    },
    getNowDay(){ //定义方法 time

      let time = new Date();  //实例化日期对象

      let year=time.getFullYear()+"-" //获取年

      let month=time.getMonth()+1+"-" //获取月

      let day=time.getDate()+""      //获取日

      if(time.getMonth()+1<10){          //判定月
        month = "0"+(time.getMonth()+1)+"-";
      }

      return year+month+day;
    },

    getPassDay(n,date){
      let res=[]
      for (let i=n;i>0;i--){
        res.push(i)
      }

      for (let i =0;i<res.length;i++) {
        let time
        if(date==null)
          time= new Date();  //实例化日期对象
        else
          time=new Date(date)

        time.setDate(time.getDate()-res[i]) //

        let year=time.getFullYear()+"-" //获取年

        let month=time.getMonth()+1+"-" //获取月

        let day=time.getDate()+""      //获取日

        if(time.getMonth()+1<10){          //判定月
          month = "0"+(time.getMonth()+1)+"-";
        }
        res[i]=year+month+day
      }
      return res;
    },

    getLoadChar(xData1,chartData1,legends2,chartData2){
      //近七天商品种类销售统计
      let legends1=[]
      for (let i = 0; i < this.productType.length; i++) {
        legends1.push(this.productType[i].type)
      }
      let c1 = this.$echarts.init(document.getElementById('card1'))
      this.loadLine(c1,xData1,legends1,chartData1)

      //近三天平台订单金额统计
      const xData2 = ['订单总金额','订单实际收款','订单未支付金额','订单撤销金额']
      let c2 = this.$echarts.init(document.getElementById('card2'))
      this.loadBar(c2,xData2,legends2,chartData2,'10')

    },

    loadLine(chart,xData,legends,data,rightIndexs = []){
      chart && chart.clear()
      const colors = ['#59ff00','#ff00b7','#ffd500','#00ffff','#01CF87','#1890FF','#FFBE69','#8568FE','#EF4747','#249BAD']
      let series = legends.map((item,index) => {
        return {
          name: item,
          type: 'line',
          smooth: true,
          yAxisIndex: rightIndexs.includes(index) ? 1 : 0,
          symbol: 'circle',
          symbolSize: 5,
          itemStyle:{
            normal:{
              color: colors[index],
              label : {
                show: false,
                position: 'top',
                color: colors[0]
              }
            },
          },
          data: data[index]
        }
      })
      let option = {
        legend: {
          data: legends,
          left:'center',
          top:'0',
          textStyle: {
            color: '#070707',
          },
          itemWidth: 15,
          itemHeight: 10,
        },
        tooltip : {
          trigger: 'axis',
          axisPointer:{
            lineStyle:{
              width:0
            }
          },
        },
        grid: {
          top: '40px',
          left: '0px',
          right: '2px',
          bottom: '0px',
          containLabel: true
        },
        xAxis : [
          {
            type : 'category',
            boundaryGap : true,
            axisLine: {
              show: true,
              lineStyle: {
                color: '#070707',
              }

            },
            axisLabel: {
              interval:0,
              rotate:15,
              textStyle: {
                color: '#070707',
              }
            },
            axisTick: {
              show: false
            },
            splitLine:{
              show: false,
              lineStyle:{
                color: '#070707',
              }
            },
            data : xData
          }
        ],
        yAxis : [
          {
            type : 'value',
            axisLine: {
              lineStyle: {
                color: '#070707',
              }
            },
            axisLabel: {
              formatter: function(param){
                return param > 1000 ? param / 1000 + 'k' : param
              },
              textStyle: {
                color: '#070707',
              }
            },
            axisTick: {
              show: false
            },
            splitLine:{
              show: true,
              lineStyle:{
                color: '#070707',
              }
            }
          },
          {
            type : 'value',
            axisLine: {
              show: rightIndexs.length > 0,
              lineStyle: {
                color: '#070707',
              }
            },
            axisLabel: {
              formatter: '{value}',
              textStyle: {
                color: '#070707',

              }
            },
            axisTick: {
              show: false
            },
            splitLine:{
              show: rightIndexs.length > 0,
              lineStyle:{
                color: '#070707',
              }
            }
          },
        ],
        series
      }
      chart.setOption(option)
},

    loadBar(chart,xData,legends,data,width,inverse = false){
      chart && chart.clear();
      const colors = ['#1890FF','#01CF87','#FFBE69','#8568FE','#EF4747']
      var legend = legends.map(item => {
        return {
          name: item,
          icon: 'rect'
        }
      })
      let series = legends.map((item,index) => {
        return {
          name: item,
          type: 'bar',
          barGap: inverse ? '50%' : '0',
          itemStyle:{
            normal:{
              color: colors[index],
              label : {
                show: false,
                position: 'top',
                color: colors[0]
              }
            },
          },
          barWidth: width,
          data: data[index]
        }
      })
      let option = {
        legend: {
          data: legend,
          left: inverse ? 'right' : 'left',
          top:'0',
          textStyle: {
            color: '#070707',
          },
          itemWidth: 15,
          itemHeight: 10,
        },
        tooltip : {
          trigger: 'axis',
          axisPointer:{
            lineStyle:{
              width:0
            }
          },
        },
        grid: {
          top: inverse ? '20px' : '30px',
          left: '0px',
          right: inverse ? '10px' : '2px',
          bottom: '0px',
          containLabel: true
        },
        xAxis : [
          {
            type : inverse ? 'value' : 'category',
            boundaryGap : true,
            axisLine: {
              show: !inverse,
              lineStyle: {
                color: '#070707',
              }

            },
            axisLabel: {
              interval:0,
              rotate:15,
              show: !inverse,
              textStyle: {
                color: '#070707',
                fontSize: 11
              }
            },
            axisTick: {
              show: false
            },
            splitLine:{
              show: !inverse,
              lineStyle:{
                color: '#070707',
              }
            },
            data : inverse ? '' : xData
          }
        ],
        yAxis : [
          {
            type : inverse ? 'category' : 'value',
            axisLine: {
              lineStyle: {
                color: '#070707',
              }
            },
            axisLabel: {
              formatter: '{value}',
              textStyle: {
                color: '#070707',
              }
            },
            axisTick: {
              show: false
            },
            splitLine:{
              show: !inverse,
              lineStyle:{
                color: '#070707',
              }
            },
            data : inverse ? xData : ''
          },
        ],
        series
      }
      chart.setOption(option)
    },
  }
}
</script>

<style scoped>

</style>
