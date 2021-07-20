<template>
  <div class="row">
    <div class="col-12">
      <div>
        <h3>任职情况</h3>
      </div>
      <el-table :data="officeTable" height="400px" border>
        <el-table-column type="index" label="序号" width="50"></el-table-column>
        <el-table-column prop="time" label="任职年度" width="250">
        </el-table-column>
        <el-table-column prop="work" label="任职情况"> </el-table-column>
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
export default {
  data() {
    return {
      officeInfoVisible: false,
      officeInfoTitle: "",
      officeTable: [],
      id: "",
      pageno: 0,
      size: 10,
      totalItems: 0,
    };
  },
  created() {
    this.id = this.$store.state.auth.user.id;
    this.findStuOfficeById();
  },
  methods: {
    handleSizeChange(size) {
      this.size = size;
      this.findStuOfficeById();
    },
    handleCurrentChange(pageno) {
      this.pageno = pageno - 1;
      this.findStuOfficeById();
    },
    findStuOfficeById() {
      this.$ajax
        .get("/office/details", {
          params: {
            id: this.id,
            pageno: this.pageno,
            size: this.size,
          },
        })
        .then((res) => {
          this.officeTable = res.data.office.content;
          this.totalItems = res.data.office.totalElements;
        });
    },
  },
};
</script>
<style>
</style>