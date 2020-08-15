import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import store from "@/store";

import Home from '../views/Home.vue';
import Main from '../views/Main.vue';
// Study Group 관련
import StudyCreate from '../views/StudyCreate.vue';
import StudyDetail from '../views/StudyDetail.vue';
import StudyUpdate from '../views/StudyUpdate.vue';
import StudySearch from '../views/StudySearch.vue';
// Contact & Guide
import Contact from '../views/Contact.vue';
import Guide from '../views/Guide.vue';
// User 관련
import Signup from "../views/user/Signup.vue";
import userPage from "../views/user/userPage.vue";
import UserTimer from "../views/user/UserTimer.vue";
import OAuth2RedirectHandler from "../views/user/OAuth2RedirectHandler.vue";
import Login from "../views/user/Login.vue";

// MeetingRoom
import MeetingRoom from "../views/MeetingRoom.vue";
import ReadyMeeting from "../views/ReadyMeeting.vue";
import MySchedule from "../views/MySchedule.vue"


Vue.use(VueRouter);

const rejectAuthUser = (to: any, from: any, next: (arg0: string) => void) => {
  if (store.getters['auth/loginStatus'] === true) {
    alert("로그인됨");
    next("/");
  } else {
    next("");
  }
}
// beforeEnter: rejectAuthUser, 해당 라우터에 이부분 써주기

const onlyAuthUser = (to: any, from: any, next: (arg0: string | undefined) => void) => {
  if (store.getters['auth/loginStatus'] === false) {
    alert("로그인됨") // 아직 로그인 안 된 유저여서 막아야됨
    next("/")
  } else {
    next("");
  }
}

const routes: Array<RouteConfig> = [
  { 
    path: '/login',
    name: 'Login',
    component: Login
  },
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
    path: '/study/:id',
    name: 'StudyDetail',
    component: StudyDetail
  },
  {
    path: '/study/:id',
    name: 'StudyUpdate',
    component: StudyUpdate
  },
  {
    path: '/study/search',
    name: 'StudySearch',
    component: StudySearch,
    props: true
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
    component: userPage
  },
  {
    path: "/user/signup",
    name: "Signup",
    component: Signup
  },
  {
    path: "/oauth2",
    component: OAuth2RedirectHandler
  },
  {
    path: "/ReadyMeeting/:id",
    name: "ReadyMeeting",
    component: ReadyMeeting,
    props: true
  },
  {
    path: "/meetingroom/:id",
    name: "MeetingRoom",
    component: MeetingRoom,
    props: true
  },
  {
    path: "/user/myschedule",
    name: "MySchedule",
    component: MySchedule
  },
  {
    path: "/user/UserTimer",
    name: "UserTimer",
    component: UserTimer
  }
];

const router = new VueRouter({
  routes
});

export default router;
