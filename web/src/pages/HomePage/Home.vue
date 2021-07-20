<template>
  <div>
    <top-navbar></top-navbar>
    <div class="test-clock-container">
      <flip-clock style="margin-top: 100px" />
    </div>
    <div class="name-box">
      <h1 id="username">{{ this.name }} {{ this.identity }}，欢迎您！</h1>
    </div>
    <div
      id="fish-container"
      class="fishcontainer"
      style="width: 1500px; height: 500px"
    ></div>
  </div>
</template>
<script>
import FlipClock from "kuan-vue-flip-clock";
import TopNavbar from "@/layout/TopNavbar.vue";
import "./wave/wavescript";
export default {
  components: { FlipClock, TopNavbar },
  data() {
    return {
      identity: "",
      name: "",
    };
  },
  computed: {
    currentUser() {
      let user = this.$store.state.auth.user;
      this.name = user.name;
      switch (user.role) {
        case "ROLE_MANAGER":
          this.identity = "管理员";
          break;
        case "ROLE_TEACHER":
          this.identity = "教师";
          break;
        case "ROLE_STUDENT":
          this.identity = "同学";
          break;
      }
      return user;
    },
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push("/");
    }
    this.i18n = this.$i18n;
  },
};
</script>
<style>
@import url("./wave/wavestyle.css");
#username {
  position: absolute;
  top: 320px;

  font-family: fangsong;
  font-size: 95px;
  font-weight: bolder;
  color: #25354e;
}
#wave {
  position: absolute;
}
</style>
<style lang="scss">
// flip clock style
.test-clock-container {
  font: normal 12px "Helvetica Neue", Helvetica, sans-serif;
  user-select: none;
  text-shadow: 0 1px 0 rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
}
.flip {
  width: 100px !important;
  height: 160px !important;
  font-size: 130px !important;
  line-height: 165px !important;
}
.flip .item .inn {
  color: #25354e !important;
  background-color: #fff !important;
}
.flip .item .up::after {
  top: 77px !important;
  height: 2px;
}

.name-box {
  display: flex;
  justify-content: center;
}
</style>