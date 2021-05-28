import { createStore } from "vuex";

import usersModule from "./modules/users/index.js";
import issuesModule from "./modules/issues/index.js";

const store = createStore({
  modules: {
    users: usersModule,
    issues: issuesModule,
  },
});

export default store;
