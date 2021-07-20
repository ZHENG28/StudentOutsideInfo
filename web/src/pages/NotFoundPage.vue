<template>
  <div class="contact-us full-screen">
    <router-link
      :to="{ path: '/home' }"
      class="navbar-brand text-danger"
      style="font-weight: 500; margin-left: 30px; margin-top: 15px"
      >RETURN TO HOMEPAGE</router-link
    >
    <div class="wrapper wrapper-full-page section content containercenter">
      <div class="chlidcenter">
        <div class="container">
          <div class="row">
            <div
              class="col-md-8 col-md-offset-2 text-center"
              style="width: -webkit-fill-available; margin-top: 110px"
            >
              <h2 class="title text-danger" style="font-size: 80px">
                404 Not Found
              </h2>
              <h2
                class="title text-primary"
                style="font-size: 50px; margin-top: 75px"
              >
                Oops! It seems that this page does not exist.
              </h2>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {};
</script>
<style scoped>
.containercenter {
  display: flex;
  justify-content: center;
}
.chlidcenter {
  display: flex;
  vertical-align: middle;
}
</style>
