<template>
  <div class="row">
    <div class="col-12">
      <div>
        <h3 style="display: inline">志愿活动情况</h3>
        <div style="float: right">
          <el-button
            @click="
              chartVisible = true;
              openBar(id);
            "
            >查看</el-button
          >
          <el-dialog title="志愿工时统计" :visible.sync="chartVisible">
            <div ref="barbox" style="width: 600px; height: 400px"></div>
          </el-dialog>
        </div>
      </div>
      <el-table :data="volTable" height="400px" border>
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column prop="name" label="活动名称"> </el-table-column>
        <el-table-column prop="time" label="活动时间"> </el-table-column>
        <el-table-column prop="manhour" label="活动时长"> </el-table-column>
      </el-table>
      <div class="block">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageno + 1"
          :page-sizes="[5, 10, 20]"
          :page-size="size"
          layout="total, sizes, ->, pager, next, jumper"
          :total="totalItems"
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>
<script>
import echarts from "echarts";
export default {
  data() {
    return {
      id: "",
      chartVisible: false,
      barXAxis: [],
      barData: [],

      volTable: [],
      pageno: 0,
      size: 10,
      totalItems: 0,
    };
  },
  created() {
    this.id = this.$store.state.auth.user.id;
    this.getYearArray();
    this.findStuVolById();
  },
  methods: {
    // 获取年份列表
    getYearArray() {
      let nowYear = new Date().getFullYear();
      for (let i = 0; i < 5; i++, nowYear--) {
        let json = `${nowYear - 1}-${nowYear}`;
        this.barXAxis.push(json);
      }
    },

    handleSizeChange(size) {
      this.size = size;
      this.findStuVolById();
    },
    handleCurrentChange(pageno) {
      this.pageno = pageno - 1;
      this.findStuVolById();
    },

    findStuVolById() {
      this.$ajax
        .get("/volunteer/details", {
          params: {
            id: this.id,
            pageno: this.pageno,
            size: this.size,
          },
        })
        .then((res) => {
          this.volTable = res.data.volActs.content;
          this.totalItems = res.data.volActs.totalElements;
          this.barData = res.data.barData;
          this.openBar();
        });
    },
    // 画柱状图
    drawBar() {
      this.charts = echarts.init(this.$refs.barbox);
      let option = {
        title: {
          text: this.barTitle,
          left: "center",
        },
        tooltip: {
          trigger: "item",
        },
        xAxis: {
          type: "category",
          data: this.barXAxis,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            data: this.barData,
            type: "bar",
          },
        ],
      };

      if (option && typeof option === "object") {
        this.charts.setOption(option);
      }
    },
    openBar() {
      this.$nextTick(() => {
        this.drawBar();
      });
    },
  },
};
</script>
<style>
</style>