import { createRouter, createWebHistory } from "vue-router";
import UsersList from "../views/UsersList.vue"
import User from "../views/User.vue"
import IssuesList from "../views/IssuesList.vue"
import Issue from "../views/Issue.vue"

//import Users from "../components/Users.vue";
//import User from "../components/UserItem.vue";
//import Issues from "../components/Issues.vue";
//import Issue from "../components/IssueItem.vue";
import Home from "../views/Home.vue";

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/users",
    name: "Users",
    component: UsersList,
  },
  {
    path: "/users/:id",
    name: "User",
    component: User,
  },
  {
    path: "/issues",
    name: "Issues",
    component: IssuesList,
  },
  {
    path: "/issues/:id",
    name: "Issue",
    component: Issue,
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/About.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
