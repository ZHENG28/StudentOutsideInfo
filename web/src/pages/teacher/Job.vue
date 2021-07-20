<template>
  <div class="row">
    <div class="col-12">
      <div>
        <h3 style="display: inline">学生任职情况</h3>
        <el-select
          v-model="year"
          placeholder="请选择"
          class="select-style"
          @change="selectYear"
        >
          <el-option
            v-for="y in years"
            :key="y.value"
            :label="y.label"
            :value="y.value"
          >
          </el-option>
        </el-select>
        <div style="float: right">
          <el-button
            @click="
              chartVisible = true;
              loadPieData();
            "
            >查看{{ year }}年度任职人次分布情况</el-button
          >
          <el-dialog
            :title="year + '年度任职人次分布情况'"
            :visible.sync="chartVisible"
          >
            <div ref="piebox" style="width: 600px; height: 400px"></div>
          </el-dialog>
        </div>
      </div>
      <el-table
        :data="
          tableData.filter(
            (data) =>
              !search ||
              data.student.name.toLowerCase().includes(search.toLowerCase())
          )
        "
        height="480px"
        border
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column prop="student.name" label="姓名" width="150">
        </el-table-column>
        <el-table-column prop="time" label="任职年度" width="250">
        </el-table-column>
        <el-table-column prop="work" label="任职情况"> </el-table-column>
        <el-table-column label="搜索" width="200">
          <!-- eslint-disable-next-line -->
          <template slot="header" slot-scope="scope">
            <el-input
              v-model="search"
              size="mini"
              placeholder="输入姓名进行搜索"
            />
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
      // 分页查询
      pageno: 0,
      size: 10,
      totalItems: 0,

      // 年度
      years: [],
      year: "",

      // 图表显示
      chartVisible: false,
      charts: "",
      opinion: [],
      clazzData: [],
      gradeData: [],
    };
  },
  created() {
    this.getYearArray();
    this.findAllByYear(this.year);
  },
  methods: {
    // 获取年份列表
    getYearArray() {
      let nowYear = new Date().getFullYear();
      for (let i = 0; i < 10; i++, nowYear--) {
        let json = {
          value: `${nowYear - 1}-${nowYear}`,
          label: `${nowYear - 1}-${nowYear}年度`,
        };
        this.years.push(json);
      }
      this.year = this.years[0].value;
    },
    // 选择其他年份
    selectYear(year) {
      this.findAllByYear(year);
    },

    // 分页查询
    handleSizeChange(size) {
      this.size = size;
      this.findAllByYear(this.year);
    },
    handleCurrentChange(pageno) {
      this.pageno = pageno - 1;
      this.findAllByYear(this.year);
    },
    // 加载表格数据
    findAllByYear(year) {
      this.$ajax
        .get("/teacher/office/findAllByYear", {
          params: {
            year: year,
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
      this.$ajax
        .get("/teacher/office/loadPieData", {
          params: {
            time: this.year,
          },
        })
        .then((res) => {
          this.opinion = res.data.opinion;
          this.clazzData = res.data.clazzData;
          this.gradeData = res.data.gradeData;
          this.openPie();
        });
    },
    // 画饼图
    drawPie() {
      this.charts = echarts.init(this.$refs.piebox);
      this.charts.setOption({
        title: {
          text: "各班级与年级之间的任职总人数对比",
          subtext: "(以人次为单位)",
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
          position: "outside",
          orient: "vertical",
          x: "right",
          data: this.opinion,
        },
        series: [
          {
            type: "pie",
            radius: "45%",
            legendHoverLink: true,
            center: ["50%", "60%"],
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
            center: ["50%", "60%"],
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
      this.$nextTick(() => {
        this.drawPie();
      });
    },
  },
};
</script>
<style scoped>
.select-style {
  padding-left: 20px;
}
</style>