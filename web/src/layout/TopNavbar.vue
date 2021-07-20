<template>
  <div>
    <nav
      class="navbar navbar-expand-lg navbar-absolute"
      :class="{ 'bg-white': showMenu, 'navbar-transparent': !showMenu }"
    >
      <div class="container-fluid">
        <el-menu
          class="el-menu-demo"
          mode="horizontal"
          background-color=""
          router
          @select="refresh"
        >
          <!--           
            background-color="#97e7ff"
          text-color="#fff"
          active-text-color="#000" -->
          <!-- text-color="#07bbf1" -->
          <el-menu-item index="/home"> 首页 </el-menu-item>
          <!-- 教师端 -->
          <el-menu-item index="/teacher/job" v-if="showTeacher">
            学生任职情况
          </el-menu-item>
          <el-menu-item index="/teacher/volunteer" v-if="showTeacher">
            志愿服务情况
          </el-menu-item>

          <!-- 管理员端 -->
          <el-menu-item index="/admin/addOffice" v-if="showMgr">
            上传学生任职情况
          </el-menu-item>
          <el-menu-item index="/admin/addVol" v-if="showMgr">
            上传学生志愿服务情况
          </el-menu-item>
          <el-menu-item index="/admin/StuList" v-if="showMgr">
            管理所有学生信息
          </el-menu-item>

          <!-- 学生端 -->
          <el-menu-item index="/student/office" v-if="showStudent">
            查询个人任职信息
          </el-menu-item>
          <el-menu-item index="/student/vol" v-if="showStudent">
            查询个人志愿服务信息
          </el-menu-item>
        </el-menu>

        <collapse-transition>
          <div class="collapse navbar-collapse show" v-show="showMenu">
            <ul class="navbar-nav ml-auto">
              <base-dropdown
                tag="li"
                title-tag="a"
                class="nav-item"
                menu-classes="dropdown-navbar"
                style="cursor: pointer"
              >
                <a
                  slot="title"
                  href="#"
                  data-toggle="dropdown"
                  aria-expanded="true"
                >
                  <div class="photo">
                    <img src="img/default-avatar.png" />
                  </div>
                </a>
                <li class="nav-link">
                  <a
                    @click="dialogFormVisible = true"
                    class="nav-item dropdown-item"
                    >修改个人信息</a
                  >
                </li>
                <div class="dropdown-divider"></div>
                <li class="nav-link">
                  <a href @click.prevent="logOut" class="nav-item dropdown-item"
                    >登出系统</a
                  >
                </li>
              </base-dropdown>
            </ul>
          </div>
        </collapse-transition>
      </div>
    </nav>
    <el-dialog title="修改个人信息" :visible.sync="dialogFormVisible">
      <el-form :model="userInfo" :rules="FormRules" ref="userInfo">
        <el-form-item
          label="账号"
          :label-width="formLabelWidth"
          prop="username"
        >
          <el-input
            v-model="userInfo.username"
            style="width: 250px"
            readonly="readonly"
          ></el-input>
        </el-form-item>
        <el-form-item label="姓名" :label-width="formLabelWidth" prop="name">
          <el-input v-model="userInfo.name" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item
          label="密码"
          :label-width="formLabelWidth"
          prop="password"
        >
          <el-input
            type="password"
            v-model="userInfo.password"
            style="width: 250px"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateInfo()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { CollapseTransition } from "vue2-transitions";
export default {
  components: {
    CollapseTransition,
  },
  data() {
    return {
      showMenu: false,
      dialogFormVisible: false,
      formLabelWidth: "200px",
      userInfo: {
        id: "",
        role: "",
        username: "",
        password: "",
        name: "",
      },
      FormRules: {
        username: [
          { required: true, message: "账号不能为空，请填写", trigger: "blur" },
        ],
        name: [
          { required: true, message: "姓名不能为空，请填写", trigger: "blur" },
        ],
        password: [
          { required: true, message: "密码不能为空，请填写", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    let user = this.currentUser;
    this.userInfo = user;
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    showMgr() {
      if (this.currentUser && this.currentUser.role) {
        return this.currentUser.role == "ROLE_MANAGER";
      }
      return false;
    },
    showTeacher() {
      if (this.currentUser && this.currentUser.role) {
        return this.currentUser.role == "ROLE_TEACHER";
      }
      return false;
    },
    showStudent() {
      if (this.currentUser && this.currentUser.role) {
        return this.currentUser.role == "ROLE_STUDENT";
      }
      return false;
    },
  },
  methods: {
    refresh(key) {
      if (key == "/home") {
        this.$router.push("/home");
        this.$router.go(0);
      }
    },

    updateInfo() {
      this.$ajax
        .post("/sys/updateInfo", {
          id: this.userInfo.id,
          role: this.userInfo.role,
          username: this.userInfo.username,
          name: this.userInfo.name,
          password: this.userInfo.password,
        })
        .then(
          (res) => {
            if (res.data.message == "OK") {
              this.$message.success("修改成功");
            } else {
              this.$message.error("修改失败");
            }
            this.dialogFormVisible = false;
          },
          (error) => {
            console.log("error:" + error.message);
            this.$message.info("数据出错");
          }
        )
        .catch(function (error) {
          this.$message.info("数据出错");
          console.log(error);
        });
    },

    logOut() {
      this.$store.dispatch("auth/logout");
      this.$router.push("/");
    },
  },
};
</script>
<style>
</style>
