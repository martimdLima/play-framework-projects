export default {
  userList(state) {
    return state.userList;
  },
  user(state) {
    return state.user;
  },


  hasUsers(state) {
    return state.users && state.users.length > 0;
  },
  isUser(_, getters, _2, rootGetters) {
    const users = getters.users;
    const userId = rootGetters.userId;
    return users.some((user) => user.id === userId);
  },
  shouldUpdate(state) {
    return !state.lastFetch
      ? true
      : (new Date().getTime() - state.lastFetch) / 1000 > 60;
  },
};
