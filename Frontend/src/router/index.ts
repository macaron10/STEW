import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import store from "@/store";

import Main from '../views/Main.vue';
// Study Group 관련
import StudyCreate from '../views/StudyCreate.vue';
import StudyDetail from '../views/StudyDetail.vue';
import StudyUpdate from '../views/StudyUpdate.vue';
import StudySearch from '../views/StudySearch.vue';
// Contact & Guide
import Contact from '../views/Contact.vue';
import Guide from '../views/Guide.vue';
import About from "../views/About.vue";
// User 관련
import SignUp from "../views/user/SignUp.vue";
import userPage from "../views/user/userPage.vue";
import UserTimer from "../views/user/UserTimer.vue";
import OAuth2RedirectHandler from "../views/user/OAuth2RedirectHandler.vue";
import Login from "../views/user/Login.vue";

// MeetingRoom
import MeetingRoom from "../views/MeetingRoom.vue";
import ReadyMeeting from "../views/ReadyMeeting.vue";
import MySchedule from "../views/MySchedule.vue"


Vue.use(VueRouter);

// 로그인 한 경우, 로그인/회원가입 페이지 접근 못하도록
const rejectAuthUser = (to: any, from: any, next: any) => {
  if (store.getters['auth/loginStatus'] === true) {
    alert('접근할 수 없는 페이지 입니다.');
  } else {
    next();
  }
}

// 로그인 하지 않은 경우, 로그인 필요한 페이지 접근 제한
const onlyAuthUser = (to: any, from: any, next: any) => {
  if (store.getters['auth/loginStatus'] === false) {
    alert("로그인이 필요합니다.");
    next("/login");
  } else {
    next();
  }
}

// 가입하지 않은 스터디 페이지 접근 제한
const participated = (to: any, from: any, next: any) => {
  store.dispatch("sg/joinedGroup", {gpNo: to.params.id})
  .then(res => {
    // console.log(res);
    if(res.data.object) {
      next();
    } else {
      alert("가입하지 않은 스터디입니다.");
    }
  })
}

const routes: Array<RouteConfig> = [
  { 
    path: '/login',
    name: 'Login',
    component: Login,
    beforeEnter: rejectAuthUser,
  },
  {
    path: "/user/signup",
    name: "SignUp",
    component: SignUp,
    beforeEnter: rejectAuthUser,
  },
  {
    path: '/',
    name: 'Main',
    component: Main
  },
  {
    path: '/study/create',
    name: 'StudyCreate',
    component: StudyCreate,
    beforeEnter: onlyAuthUser,
  },
  {
    path: '/study/:id',
    name: 'StudyDetail',
    component: StudyDetail,
    beforeEnter: participated,
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
    component: About
  },
  {
    path: '/user/detail',
    name: 'UserDetail',
    component: userPage,
    beforeEnter: onlyAuthUser
  },
  {
    path: "/oauth2",
    component: OAuth2RedirectHandler
  },
  {
    path: "/ReadyMeeting/:id",
    name: "ReadyMeeting",
    component: ReadyMeeting,
    beforeEnter: participated,
    props: true 
  },
  {
    path: "/meetingroom/:id",
    name: "MeetingRoom",
    component: MeetingRoom,
    beforeEnter: participated,
    props: true
  },
  {
    path: "/user/myschedule",
    name: "MySchedule",
    component: MySchedule,
    beforeEnter: onlyAuthUser
  },
  {
    path: "/user/UserTimer",
    name: "UserTimer",
    component: UserTimer,
    beforeEnter: onlyAuthUser
  },
];

const router = new VueRouter({
  routes
});

export default router;
