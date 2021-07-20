<template>
  <div class="row">
    <div class="col-12">
      <div>
        <h3 style="display: inline">学生所有信息</h3>
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
        <el-table-column prop="id" label="学号" width="250"> </el-table-column>
        <el-table-column prop="name" label="姓名" width="250">
        </el-table-column>
        <el-table-column prop="grade" label="年级" width="250">
        </el-table-column>
        <el-table-column prop="banji" label="班级" width="250">
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
              @click="toDetails(scope.row.id)"
              style="margin-left: 15px"
              >查看详细信息</el-button
            >
            <el-button
              @click="
                loadFormData(scope.row);
                stuFormVisible = true;
              "
              >修改</el-button
            >
            <el-dialog title="修改学生基本信息" :visible.sync="stuFormVisible">
              <el-form
                :model="stuForm"
                :rules="stuFormRules"
                inline
                ref="stuForm"
                label-position="left"
              >
                <el-form-item label="学号" prop="id">
                  <el-input
                    v-model="stuForm.id"
                    style="width: 250px"
                  ></el-input>
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                  <el-input
                    v-model="stuForm.name"
                    style="width: 250px"
                  ></el-input>
                </el-form-item>
                <el-form-item label="年级" prop="grade">
                  <el-input
                    v-model="stuForm.grade"
                    style="width: 250px"
                  ></el-input>
                </el-form-item>
                <el-form-item label="班级" prop="banji">
                  <el-input
                    v-model="stuForm.banji"
                    style="width: 250px"
                  ></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="stuFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateStu()">确 定</el-button>
              </div>
            </el-dialog>
            <el-button
              type="danger"
              @click="delStu(scope.row.id)"
              style="margin-left: 15px"
              >删除</el-button
            >
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

      stuFormVisible: false,
      stuForm: {
        id: "",
        name: "",
        grade: "",
        banji: "",
      },
      stuFormRules: {
        id: [
          { required: true, message: "学号不能为空，请填写", trigger: "blur" },
        ],
        name: [
          { required: true, message: "姓名不能为空，请填写", trigger: "blur" },
        ],
        grade: [
          { required: true, message: "年级不能为空，请填写", trigger: "blur" },
        ],
        banji: [
          { required: true, message: "班级不能为空，请填写", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.findAllStu();
  },
  methods: {
    toDetails(id) {
      this.$router.push({
        path: `/student/details/${id}`,
      });
    },

    // 分页查询（总）
    handleSizeChange(size) {
      this.size = size;
      this.findAllStu();
    },
    handleCurrentChange(pageno) {
      this.pageno = pageno - 1;
      this.findAllStu();
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

    loadFormData(row) {
      this.stuForm = {
        id: row.id,
        name: row.name,
        grade: row.grade,
        banji: row.banji,
      };
    },
    updateStu() {
      this.$refs["stuForm"].validate((valid) => {
        if (!valid) {
          console.log("error submit!!");
          return;
        } else {
          this.$ajax
            .post(
              "/admin/student/update",
              this.$qs.stringify({
                id: this.stuForm.id,
                name: this.stuForm.name,
                grade: this.stuForm.grade,
                banji: this.stuForm.banji,
              })
            )
            .then((response) => {
              this.stuFormVisible = false;
              if (response.data == "success") {
                this.$message.success("修改成功");
                this.findAllStu();
              } else if (response.data == "failed") {
                this.$message.error("修改失败");
              }
            })
            .catch(function (error) {
              this.$message.info("数据出错");
              console.log(error);
            });
        }
      });
    },
    delStu(id) {
      this.$confirm("此操作将永久删除信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$ajax
            .get("/admin/student/del", {
              params: {
                id: id,
                pageno: this.pageno,
                size: this.size,
              },
            })
            .then((response) => {
              this.tableData = response.data.content;
              this.totalItems = response.data.totalElements;
              this.$message.success("删除成功!");
            })
            .catch(() => {
              this.$message.error("删除失败");
            });
        })
        .catch(() => {
          this.$message.info("已取消删除");
        });
    },
  },
};
</script>
<style>
</style>