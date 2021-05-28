export default {
  setIssues(state, payload) {
    state.issueList = payload;
  },
  setIssue(state, payload) {
    state.issue = payload;
  },
  registerIssue(state, payload) {
    state.issues.push(payload);
  },
  setFetchTimestamp(state) {
    state.lastFetch = new Date().getTime();
  },
};
