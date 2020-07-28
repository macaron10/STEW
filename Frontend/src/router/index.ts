import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import Home from '../views/Home.vue';
import Main from '../views/Main.vue';
// Study Group 관련
import StudyCreate from '../views/StudyCreate.vue';
import StudyDetail from '../views/StudyDetail.vue';
import StudyUpdate from '../views/StudyUpdate.vue';
// Contact & Guide
import Contact from '../views/Contact.vue';
import Guide from '../views/Guide.vue';
// User 관련
import Signup from "../views/Signup.vue";
import UserDetail from '../views/UserDetail.vue';
import Login from "../components/Login.vue";
// MeetingRoom
import MeetingRoom from "../views/MeetingRoom.vue";

Vue.use(VueRouter);

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
    path: "/meetingroom/:id",
    name: "MeetingRoom",
    component: MeetingRoom
  },
];

const router = new VueRouter({
  routes
});

export default router;
