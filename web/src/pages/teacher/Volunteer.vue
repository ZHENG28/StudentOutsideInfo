<template>
  <div class="row">
    <div class="col-12">
      <div>
        <h3 style="display: inline">志愿服务情况</h3>
        <div style="float: right">
          <el-button
            @click="
              chartVisible = true;
              loadPieData();
              loadStackBarData('2019');
            "
            >查看总志愿工时分布情况</el-button
          >
          <el-dialog
            title="总志愿工时分布情况"
            :visible.sync="chartVisible"
            width="1300px"
          >
            <div ref="piebox" style="width: 600px; height: 500px"></div>
            <el-select
              v-model="stackBarTitle"
              placeholder="请选择性别"
              @change="changeData"
              style="position: relative; left: 267px; z-index: 99"
            >
              <el-option
                v-for="grade in gradeArr"
                :key="grade.value"
                :label="grade.value + '级'"
                :value="grade.value"
              ></el-option>
            </el-select>
            <div ref="stackbarbox" style="width: 1200px; height: 500px"></div>
          </el-dialog>
        </div>
      </div>
      <el-table
        :data="
          tableData.filter(
            (data) =>
              !search ||
              data.name.toLowerCase().includes(search.toLowerCase()) ||
              data.id == search.toLowerCase()
          )
        "
        height="480px"
        border
        style="width: 100%"
      >
        <el-table-column prop="id" label="学号"> </el-table-column>
        <el-table-column prop="name" label="姓名"> </el-table-column>
        <el-table-column
          prop="grade"
          label="年级"
          :filters="gradeArr"
          :filter-method="filterGrade"
          filter-placement="bottom-end"
        >
        </el-table-column>
        <el-table-column
          prop="banji"
          label="班级"
          :filters="banjiArr"
          :filter-method="filterBanji"
          filter-placement="bottom-end"
        >
        </el-table-column>
        <el-table-column label="搜索">
          <!-- eslint-disable-next-line -->
          <template slot="header" slot-scope="scope">
            <el-input
              v-model="search"
              size="mini"
              placeholder="输入姓名或学号进行搜索"
            />
          </template>
          <template slot-scope="scope">
            <el-button
              @click="
                volInfoVisible = true;
                barTitle = scope.row.name + '近五年的志愿工时统计';
                volInfoTitle = scope.row.name + ' 的志愿服务信息';
                findStuVolById(scope.row);
                openBar(scope.row.id);
              "
              >查看</el-button
            >
            <el-dialog
              :title="volInfoTitle"
              :visible.sync="volInfoVisible"
              :modal-append-to-body="false"
              id="cust-bar"
            >
              <div ref="barbox" style="width: 500px; height: 300px"></div>
              <el-table :data="volTable" height="400px" border>
                <el-table-column prop="id" label="活动编号"> </el-table-column>
                <el-table-column prop="name" label="活动名称">
                </el-table-column>
                <el-table-column prop="time" label="活动时间">
                </el-table-column>
                <el-table-column prop="manhour" label="活动时长">
                </el-table-column>
              </el-table>
            </el-dialog>
          </template>
        </el-table-column>
      </el-table>
      <div class="block">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageno + 1"
          :page-sizes="[5, 10, 20, 50]"
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
      // 表格数据
      tableData: [],
      search: "",
      gradeArr: [],
      banjiArr: [],
      // 分页查询
      pageno: 0,
      size: 10,
      totalItems: 0,

      // 总图表显示
      chartVisible: false,
      pie: "",
      opinion: [],
      clazzData: [],
      gradeData: [],

      stackBar: "",
      stackBarTitle: "2019级",
      stackbarSeries: [],
      stackLabel: {},

      // 个人图表显示
      volInfoVisible: false,
      bar: "",
      barTitle: "",
      volInfoTitle: "",
      barXAxis: [],
      barData: [],
      volTable: [],
    };
  },
  created() {
    this.getYearArray();
    this.loadData();
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

    // 筛选器
    filterGrade(value, row) {
      return value == row.grade;
    },
    filterBanji(value, row) {
      return value == row.banji;
    },
    // 分页查询
    handleSizeChange(size) {
      this.size = size;
      this.findAllStu();
    },
    handleCurrentChange(pageno) {
      this.pageno = pageno - 1;
      this.findAllStu();
    },
    loadData() {
      this.findAllStu();
      this.$ajax.post("/student/findAllGradeAndBanji", {}).then((res) => {
        this.gradeArr = res.data.gradeArr;
        this.banjiArr = res.data.banjiArr;
      });
    },
    // 加载表格数据
    findAllStu() {
      this.$ajax
        .get("/volunteer/findAllStu", {
          params: {
            pageno: this.pageno,
            size: this.size,
          },
        })
        .then((response) => {
          this.tableData = response.data.content;
          this.totalItems = response.data.totalElements;
        });
    },

    // 加载饼图数据
    loadPieData() {
      this.$ajax.get("/teacher/volunteer/loadPieData", {}).then((res) => {
        this.opinion = res.data.opinion;
        this.clazzData = res.data.clazzData;
        this.gradeData = res.data.gradeData;
        this.openPie();
      });
    },
    // 画饼图
    drawPie() {
      this.pie = echarts.init(this.$refs.piebox);
      this.pie.setOption({
        title: {
          text: "各班级与各年级之间的总志愿工时",
          subtext: "(以小时为单位)",
          left: "center",
        },
        tooltip: {
          trigger: "item",
          formatter: function (params) {
            return (
              params.marker +
              params.name +
              ": " +
              params.value +
              " (" +
              params.percent +
              "%)"
            );
          },
        },
        legend: {
          show: true,
          orient: "vertical",
          left: "right",
          data: this.opinion,
        },
        series: [
          {
            type: "pie",
            radius: "45%",
            legendHoverLink: true,
            center: ["50%", "50%"],
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
            label: {
              position: "inner",
              fontSize: 14,
            },
            labelLine: {
              show: false,
            },
            data: this.clazzData,
          },
          {
            type: "pie",
            radius: ["55%", "70%"],
            legendHoverLink: true,
            center: ["50%", "50%"],
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
            labelLine: {
              show: true,
            },
            data: this.gradeData,
          },
        ],
      });
    },
    openPie() {
      let myChart = this.pie;
      if (myChart != null && myChart != "" && myChart != undefined) {
        myChart.dispose();
      }
      this.$nextTick(() => {
        this.drawPie();
      });
    },

    changeData(data) {
      this.loadStackBarData(data);
    },
    // 加载堆叠柱状图数据
    loadStackBarData(grade) {
      this.$ajax
        .get("/teacher/volunteer/loadStackBarData", {
          params: {
            grade: grade,
          },
        })
        .then((res) => {
          this.stackLabel = res.data.stackLabel;
          this.stackbarSeries = res.data.stackbarSeries;
          this.openStackBar();
        });
    },
    // 画堆叠柱状图
    drawStackBar() {
      this.stackBar = echarts.init(this.$refs.stackbarbox);
      this.stackBar.setOption({
        title: {
          text: "各班级的总志愿工时对比",
          subtext: "(以小时为单位)",
          left: "center",
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            crossStyle: {
              color: "#999",
            },
          },
        },
        legend: {
          data: this.stackLabel,
          icon: "react", // 设置legend的图标样式
          top: "bottom", // 设置legend的位置
          itemGap: 40, // 设置legend之间的间
        },
        xAxis: [
          {
            type: "category",
            data: this.barXAxis,
            axisTick: { show: false },
          },
        ],
        yAxis: [
          {
            type: "value",
          },
          {
            type: "value",
            min: 0,
            max: 1,
            interval: 0.1,
            axisLabel: {
              formatter: "{value}%",
            },
          },
        ],
        series: this.stackbarSeries,
      });
    },
    openStackBar() {
      let myChart = this.stackBar;
      if (myChart != null && myChart != "" && myChart != undefined) {
        myChart.dispose();
      }
      this.$nextTick(() => {
        this.drawStackBar();
      });
    },

    // 查找个人志愿活动情况
    findStuVolById(row) {
      this.$ajax
        .get("/volunteer/details", {
          params: {
            id: row.id,
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
      this.bar = echarts.init(this.$refs.barbox);
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
        this.bar.setOption(option);
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
.el-dialog__body {
  display: flex;
}
.el-dialog {
  margin: 5vh auto 0px !important;
}
</style>