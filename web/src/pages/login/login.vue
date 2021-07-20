<!-- 用户登录界面 -->
<template>
  <div class="body">
    <div
      ref="container"
      class="loginContainer"
      v-bind:class="{ 'right-panel-active': isActive }"
    >
      <!-- 管理员入口 -->
      <div class="container__form container--signup">
        <el-form action="#" class="form" ref="mgrForm">
          <div class="login">
            <h2>管理员登录</h2>
            <div class="login_box">
              <!-- required就是不能为空 必须在css效果中有很大的作用 -->
              <!-- 可以简写为required -->
              <input type="text" required v-model="user.username" /><label
                >账户</label
              >
            </div>
            <div class="login_box">
              <input
                type="password"
                required="required"
                v-model="user.password"
              /><label>密码</label>
            </div>
          </div>
          <span style="color: red">{{ errMsg }}</span>
          <button class="button" @click.prevent="login('ROLE_MANAGER')">
            登录系统
          </button>
        </el-form>
      </div>

      <!-- 用户入口 -->
      <div class="container__form container--signin">
        <el-form action="#" class="form" ref="userForm">
          <div class="login">
            <h2>欢迎登录</h2>
            <div class="role" style="font-size: 18px">
              身份：
              <label style="margin: 0 20px 0 10px">
                <input type="radio" v-model="user.role" value="ROLE_TEACHER" />
                教师
              </label>
              <label>
                <input type="radio" v-model="user.role" value="ROLE_STUDENT" />
                学生
              </label>
            </div>
            <div class="login_box">
              <input type="text" required v-model="user.username" /><label
                >账户</label
              >
            </div>
            <div class="login_box">
              <input
                type="password"
                required="required"
                v-model="user.password"
              /><label>密码</label>
            </div>
          </div>
          <span style="color: red">{{ errMsg }}</span>
          <button class="button" @click.prevent="login(user.role)">
            点击登录
          </button>
        </el-form>
      </div>

      <!-- 叠加部分 -->
      <div class="container__overlay">
        <div class="overlay">
          <div class="overlay__panel overlay--left">
            <button
              class="button"
              ref="user"
              @click="
                isActive = false;
                clearFormFields();
              "
            >
              用户登录入口
            </button>
          </div>
          <div class="overlay__panel overlay--right">
            <button
              class="button"
              ref="manager"
              @click="
                isActive = true;
                clearFormFields();
              "
            >
              管理员登录入口
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import User from "@/models/user";
export default {
  name: "Login",
  data() {
    return {
      // 用于叠加部分更替效果
      isActive: true,
      // 登录部分
      user: new User("", "", ""),
      loading: false,
      errMsg: "",
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  created() {
    if (this.loggedIn) {
      this.$router.push("/home");
    }
  },
  methods: {
    clearFormFields() {
      this.user = new User("", "", "");
      this.errMsg = "";
    },
    validFormData() {
      this.errMsg = "";
      // 验证是否为空
      if (this.user.role == "") {
        // 如果管理员传入0，则输出为true
        this.errMsg = "请选择身份";
        return false;
      }
      if (this.user.username == "" || this.user.password == "") {
        this.errMsg = "用户名或密码不能为空";
        return false;
      }
      return true;
    },
    //用户登录请求后台处理
    login(role) {
      this.user.role = role;
      if (!this.validFormData()) {
        this.loading = false;
        return;
      }

      this.$store.dispatch("auth/login", this.user).then((res) => {
        if (res.id != null) {
          this.$router.push("/home");
          this.$router.go(0);
        } else {
          this.loading = false;
          this.errMsg = res;
        }
      });
    },
  },
};
</script>

<style scoped>
@import url("./formstyle.css");
@import url("./pagestyle.css");
.body {
  /*  字体样式  */
  font-size: 16px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
}
</style>