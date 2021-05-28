export default {
  issueList(state) {
    return state.issueList;
  },
  issue(state) {
    return state.issue;
  },
  hasIssues(state) {
    return state.issues && state.issues.length > 0;
  },
  isIssue(_, getters, _2, rootGetters) {
    const issues = getters.issues;
    const issueId = rootGetters.issueId;
    return issues.some((issue) => issue.id === issueId);
  },
  shouldUpdate(state) {
    return !state.lastFetch
      ? true
      : (new Date().getTime() - state.lastFetch) / 1000 > 60;
  },
};
