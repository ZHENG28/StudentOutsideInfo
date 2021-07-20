// 引入页面布局
import SystemLayout from "@/layout/SystemLayout.vue";
// GeneralViews
import NotFound from "@/pages/NotFoundPage.vue";

const Login = () => import(/* webpackChunkName: "login" */"@/pages/login/login.vue");
const Home = () => import(/* webpackChunkName: "home" */"@/pages/HomePage/Home.vue");

// teacher
const Job = () => import(/* webpackChunkName: "job" */ "@/pages/teacher/Job.vue");
const Volunteer = () => import(/* webpackChunkName: "volunteer" */ "@/pages/teacher/Volunteer.vue");

// admin
const AddOffice = () => import(/* webpackChunkName: "addOffice" */ "@/pages/admin/AddOffice.vue");
const AddVol = () => import(/* webpackChunkName: "addVol" */ "@/pages/admin/AddVol.vue");
const StuList = () => import(/* webpackChunkName: "stuList" */ "@/pages/admin/StuList.vue");
const StuDetails = () => import(/* webpackChunkName: "stuDetails" */ "@/pages/admin/StuDetails.vue");


// student
const StuOffice = () => import(/* webpackChunkName: "stuOffice" */ "@/pages/student/StuOffice.vue");
const StuVol = () => import(/* webpackChunkName: "stuVol" */ "@/pages/student/StuVol.vue");

const routes = [
  {
    path: "/",
    name: "登录",
    component: Login
  },
  {
    path: "/home",
    name: "首页",
    component: Home,
  },
  {
    path: "/",
    component: SystemLayout,
    redirect: "/home",
    children: [
      {
        path: "/admin/addOffice",
        name: "上传学生任职情况",
        component: AddOffice
      },
      {
        path: "/admin/addVol",
        name: "上传学生志愿服务情况",
        component: AddVol
      },
      {
        path: "/admin/StuList",
        name: "管理所有学生信息",
        component: StuList
      },
    ]
  },
  {
    path: "/student/details/:stuId",
    component: StuDetails
  },
  {
    path: "/", // 页面布局
    component: SystemLayout,
    redirect: "/home",
    children: [
      {
        path: "/teacher/job",
        name: "学生任职情况",
        component: Job
      },
      {
        path: "/teacher/volunteer",
        name: "志愿服务情况",
        component: Volunteer
      },
    ]
  },
  {
    path: "/",
    component: SystemLayout,
    redirect: "/home",
    children: [
      {
        path: "/student/office",
        name: "查询个人任职信息",
        component: StuOffice
      },
      {
        path: "/student/vol",
        name: "查询个人志愿服务信息",
        component: StuVol
      },
    ]
  },
  { path: "*", component: NotFound },
];

/**
 * Asynchronously load view (Webpack Lazy loading compatible)
 * The specified component must be inside the Views folder
 * @param  {string} name  the filename (basename) of the view to load.
function view(name) {
   var res= require('../components/Dashboard/Views/' + name + '.vue');
   return res;
};**/

export default routes;