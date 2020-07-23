import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import Home from '../views/Home.vue';
import Main from '../views/Main.vue';
import StudyCreate from '../views/StudyCreate.vue';
import StudyDetail from '../views/StudyDetail.vue';
import Contact from '../views/Contact.vue';
import Guide from '../views/Guide.vue';
import UserDetail from '../views/UserDetail.vue';

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
];

const router = new VueRouter({
  routes
});

export default router;
