import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import store from "../store";

import Home from '../views/Home.vue';
import Main from '../views/Main.vue';
import StudyCreate from '../views/StudyCreate.vue';
import StudyDetail from '../views/StudyDetail.vue';
import Contact from '../views/Contact.vue';
import Guide from '../views/Guide.vue';
import UserDetail from '../views/UserDetail.vue';
import Signup from "../views/Signup.vue";
import MeetingRoom from "../views/MeetingRoom.vue";

Vue.use(VueRouter);

const rejectAuthUser = (to: any, from: any, next: (arg0: string) => void) => {
  if (store.state.isLogin === true) {
    alert("로그인됨");
    next("/");
  } else {
    next("");
  }
}
// beforeEnter: rejectAuthUser, 해당 라우터에 이부분 써주기

const onlyAuthUser = (to: any, from: any, next: (arg0: string | undefined) => void) => {
  if (store.state.isLogin === false) {
    alert("로그인됨") // 아직 로그인 안 된 유저여서 막아야됨
    next("/")
  } else {
    next("");
  }
}

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/main',
    name: 'Main',
    component: Main
  },
  {
    path: '/study/create',
    name: 'StudyCreate',
    component: StudyCreate
  },
  {
    path: '/study/detail',
    name: 'StudyDetail',
    component: StudyDetail
  },
  {
    path: '/contact',
    name: 'Contact',
    component: Contact
  },
  {
    path: '/guide',
    name: 'Guide',
    component: Guide
  },
  {
    path: '/user/detail',
    name: 'UserDetail',
    component: UserDetail
  },
  {
    path: "/user/signup",
    name: "Signup",
    component: Signup
  },
  {
    path: "/meetingroom",
    name: "MeetingRoom",
    component: MeetingRoom
  },
];

const router = new VueRouter({
  routes
});

export default router;
