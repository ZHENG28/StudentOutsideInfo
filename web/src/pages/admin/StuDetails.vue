<template>
  <div style="margin-left: 10px; margin-top: 20px">
    <div style="margin-bottom: 15px">
      <el-button
        type="primary"
        style="margin-left: 5px"
        @click="$router.push('/admin/StuList')"
        >返回学生列表</el-button
      >
      <span style="font-size: 30px; margin-left: 15px"
        >{{ name }}同学的详细信息</span
      >
    </div>
    <el-row :gutter="12">
      <el-col :span="12">
        <el-card shadow="hover" style="height: 590px" header="志愿服务信息">
          <el-table
            :data="
              volTable.filter(
                (data) =>
                  !volSearch ||
                  data.name.toLowerCase().includes(volSearch.toLowerCase())
              )
            "
            height="465px"
            border
          >
            <el-table-column
              type="index"
              label="序号"
              width="50"
            ></el-table-column>
            <el-table-column prop="name" label="活动名称"> </el-table-column>
            <el-table-column prop="time" label="活动时间"> </el-table-column>
            <el-table-column prop="manhour" label="活动时长"> </el-table-column>
            <el-table-column label="搜索">
              <!-- eslint-disable-next-line -->
              <template slot="header" slot-scope="scope">
                <el-input
                  v-model="volSearch"
                  size="mini"
                  placeholder="输入活动名称进行搜索"
                />
              </template>
              <!-- eslint-disable-next-line -->
              <template slot-scope="scope">
                <el-button type="danger" @click="delVol(scope.row.id)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <div class="block">
            <el-pagination
              @size-change="volSizeChange"
              @current-change="volPagenoChange"
              :current-page="volPageno + 1"
              :page-sizes="[5, 10, 20]"
              :page-size="volSize"
              layout="total, sizes, ->, pager, next, jumper"
              :total="volTotal"
            >
            </el-pagination>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" style="height: 590px" header="任职信息">
          <el-table
            :data="
              officeTable.filter(
                (data) =>
                  !officeSearch ||
                  data.work.toLowerCase().includes(officeSearch.toLowerCase())
              )
            "
            height="465px"
            border
          >
            <el-table-column
              type="index"
              label="序号"
              width="50"
            ></el-table-column>
            <el-table-column prop="time" label="任职年度" width="100">
            </el-table-column>
            <el-table-column prop="work" label="任职情况" width="300">
            </el-table-column>
            <el-table-column label="搜索">
              <!-- eslint-disable-next-line -->
              <template slot="header" slot-scope="scope">
                <el-input
                  v-model="officeSearch"
                  size="mini"
                  placeholder="输入任职情况进行搜索"
                />
              </template>
              <!-- eslint-disable-next-line -->
              <template slot-scope="scope">
                <el-button
                  @click="
                    loadFormData(scope.row);
                    officeFormVisible = true;
                  "
                  >修改</el-button
                >
                <el-dialog
                  title="修改任职情况"
                  :visible.sync="officeFormVisible"
                >
                  <el-form
                    :model="officeForm"
                    :rules="officeFormRules"
                    inline
                    ref="officeForm"
                    label-position="left"
                  >
                    <el-form-item label="任职时间" prop="begin">
                      <template>
                        <el-date-picker
                          v-model="officeForm.begin"
                          type="year"
                          style="width: 193px"
                          format="yyyy"
                          value-format="yyyy"
                          :picker-options="pickerOptions"
                          @change="selectedBegin()"
                        >
                        </el-date-picker>
                        -
                        <el-date-picker
                          v-model="officeForm.end"
                          type="year"
                          format="yyyy"
                          value-format="yyyy"
                          readonly="readonly"
                          style="width: 192px"
                        >
                        </el-date-picker>
                      </template>
                    </el-form-item>
                    <el-form-item label="任职情况" prop="work">
                      <el-input
                        v-model="officeForm.work"
                        style="width: 250px"
                      ></el-input>
                    </el-form-item>
                  </el-form>
                  <div slot="footer" class="dialog-footer">
                    <el-button @click="officeFormVisible = false"
                      >取 消</el-button
                    >
                    <el-button type="primary" @click="updateOffice()"
                      >确 定</el-button
                    >
                  </div>
                </el-dialog>
                <el-button type="danger" @click="delOffice(scope.row.id)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <div class="block">
            <el-pagination
              @size-change="officeSizeChange"
              @current-change="officePagenoChange"
              :current-page="officePageno + 1"
              :page-sizes="[5, 10, 20]"
              :page-size="officeSize"
              layout="total, sizes, ->, pager, next, jumper"
              :total="officeTotal"
            >
            </el-pagination>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      stuId: "",
      name: "",

      // 志愿活动
      volTable: [],
      volSearch: "",
      volPageno: 0,
      volSize: 10,
      volTotal: 0,

      // 任职情况
      officeTable: [],
      officeSearch: "",
      officePageno: 0,
      officeSize: 10,
      officeTotal: 0,
      officeFormVisible: false,
      officeForm: {
        id: "",
        work: "",
        begin: "",
        end: "",
      },
      officeFormRules: {
        work: [{ required: true, message: "请填写任职情况", trigger: "blur" }],
        begin: [{ required: true, message: "请填写任职时间", trigger: "blur" }],
      },
      pickerOptions: {
        disabledDate: (time) => {
          const curDate = new Date().getTime();
          const day = 14 * 24 * 3600 * 1000;
          const dateRegion = curDate + day;
          return time.getTime() > dateRegion;
        },
      },
    };
  },
  created() {
    this.stuId = this.$route.params.stuId;
    this.findNameById();

    this.findStuVolById();
    this.findStuOfficeById();
  },
  methods: {
    findNameById() {
      this.$ajax
        .post(
          "/admin/student/findNameById",
          this.$qs.stringify({
            id: this.stuId,
          })
        )
        .then((res) => {
          this.name = res.data.name;
        });
    },
    selectedBegin() {
      let item = this.officeForm;
      item.end = parseInt(item.begin) + 1 + "";
    },

    // 志愿活动
    volSizeChange(size) {
      this.volSize = size;
      this.findStuVolById();
    },
    volPagenoChange(pageno) {
      this.volPageno = pageno - 1;
      this.findStuVolById();
    },
    findStuVolById() {
      this.$ajax
        .get("/volunteer/details", {
          params: {
            id: this.stuId,
            pageno: this.volPageno,
            size: this.volSize,
          },
        })
        .then((res) => {
          this.volTable = res.data.volActs.content;
          this.volTotal = res.data.volActs.totalElements;
        });
    },
    delVol(id) {
      this.$confirm("此操作将永久删除信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$ajax
            .get("/admin/volunteer/del", {
              params: {
                volId: id,
                stuId: this.stuId,
                pageno: this.volPageno,
                size: this.volSize,
              },
            })
            .then((response) => {
              this.volTable = response.data.volActs.content;
              this.volTotal = response.data.volActs.totalElements;
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

    // 任职情况
    officeSizeChange(size) {
      this.officeSize = size;
      this.findStuOfficeById();
    },
    officePagenoChange(pageno) {
      this.officePageno = pageno - 1;
      this.findStuOfficeById();
    },
    findStuOfficeById() {
      this.$ajax
        .get("/office/details", {
          params: {
            id: this.stuId,
            pageno: this.officePageno,
            size: this.officeSize,
          },
        })
        .then((res) => {
          this.officeTable = res.data.office.content;
          this.officeTotal = res.data.office.totalElements;
        });
    },
    loadFormData(row) {
      let time = row.time.split("-");
      this.officeForm = {
        id: row.id,
        work: row.work,
        begin: time[0],
        end: time[1],
      };
    },
    updateOffice() {
      let time = this.officeForm.begin + "-" + this.officeForm.end;
      this.$refs["officeForm"].validate((valid) => {
        if (!valid) {
          console.log("error submit!!");
          return;
        } else {
          this.$ajax
            .post(
              "/admin/office/update",
              this.$qs.stringify({
                id: this.officeForm.id,
                work: this.officeForm.work,
                time: time,
              })
            )
            .then((response) => {
              this.officeFormVisible = false;
              if (response.data == "success") {
                this.$message.success("修改成功");
                this.findStuOfficeById();
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
    delOffice(id) {
      this.$confirm("此操作将永久删除信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$ajax
            .get("/admin/office/del", {
              params: {
                id: id,
                pageno: this.officePageno,
                size: this.officeSize,
              },
            })
            .then((response) => {
              this.officeTable = response.data.office.content;
              this.officeTotal = response.data.totalElements;
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
.el-row {
  margin: 0 !important;
}
</style>