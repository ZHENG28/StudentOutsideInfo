<template>
  <div class="row">
    <div
      class="col-md-8 testBuild"
      style="width: 100% !important; height: 500px"
    >
      <card>
        <div style="margin-bottom: 20px">
          <h5 slot="header" class="title" style="display: contents">
            上传学生任职情况
          </h5>
          <el-button style="margin-left: 15px" @click="addDomain"
            >新增记录</el-button
          >
          <el-button type="primary" @click="submitForm()">提交</el-button>
        </div>

        <el-form ref="volForm">
          <el-form-item
            v-for="(domain, index) in domains"
            :label="'第' + (index + 1) + '条'"
            :key="domain.key"
            :prop="'domains.' + index + '.value'"
            class="volFormItem"
          >
            <label>
              学生姓名：
              <el-input v-model="domain.name"></el-input>
            </label>
            <label>
              任职时间：
              <el-date-picker
                v-model="domain.begin"
                type="year"
                style="width: 193px"
                format="yyyy"
                value-format="yyyy"
                :picker-options="pickerOptions"
                @change="selectedBegin(index)"
              >
              </el-date-picker>
              -
              <el-date-picker
                v-model="domain.end"
                type="year"
                format="yyyy"
                value-format="yyyy"
                readonly="readonly"
                style="width: 192px"
              >
              </el-date-picker>
            </label>
            <br />
            <label style="padding-left: 50px">
              任职情况：
              <el-input v-model="domain.work" style="width: 915px"></el-input>
            </label>
            <el-button
              @click.prevent="removeDomain(domain)"
              style="margin-left: 35px"
              type="danger"
              >删除</el-button
            >
          </el-form-item>
        </el-form>
      </card>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      domains: [
        {
          name: "",
          begin: "",
          end: "",
          work: "",
          key: Date.now(),
        },
      ],
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
  methods: {
    selectedBegin(index) {
      let item = this.domains[index];
      item.end = parseInt(item.begin) + 1 + "";
    },
    submitForm() {
      let arr = [];
      let nameList = "";
      this.domains.forEach((e) => {
        arr.push({
          work: e.work,
          time: e.begin + "-" + e.end,
        });
        nameList += e.name + ",";
      });
      this.$ajax
        .post("/admin/office/addOffice", {
          domains: JSON.stringify(arr),
          stuName: nameList,
        })
        .then((res) => {
          console.log(res);
        });
      this.$message.success("添加成功");
    },
    addDomain() {
      this.domains.push({
        name: "",
        begin: "",
        end: "",
        work: "",
        key: Date.now(),
      });
    },
    removeDomain(item) {
      var index = this.domains.indexOf(item);
      if (index !== -1) {
        this.domains.splice(index, 1);
      }
    },
  },
};
</script>
<style>
.volFormItem .el-form-item__content .el-input {
  width: 400px;
}
.volFormItem .el-form-item__content label {
  margin-left: 35px;
}
.volFormItem label {
  font-size: 15px;
}
</style>
