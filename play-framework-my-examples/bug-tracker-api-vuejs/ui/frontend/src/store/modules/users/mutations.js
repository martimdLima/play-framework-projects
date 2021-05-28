export default {
  setUsers(state, payload) {
    state.userList = payload;
  },
  setUser(state, payload) {
    state.user = payload;
  },
  setFetchTimestamp(state) {
    state.lastFetch = new Date().getTime();
  }

  /*
  registerUser(state, payload) {
    state.users.push(payload);
  },
  setUsers(state, payload) {
    state.users = payload;
  },
  setFetchTimestamp(state) {
    state.lastFetch = new Date().getTime();
  },*/
};
