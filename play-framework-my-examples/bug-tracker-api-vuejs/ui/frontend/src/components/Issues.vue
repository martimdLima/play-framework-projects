<template>
  <div>
    <div>
      <table>
        <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Name</th>
            <th scope="col">Assigned</th>
            <th scope="col">Status</th>
            <th scope="col">Introduced</th>
            <th scope="col">Updated</th>
            <th scope="col">Discontinued</th>
            <th scope="col">Category</th>
            <th scope="col">Application</th>
          </tr>
        </thead>
        <tbody v-for="issue in issues" :key="issue.id">
          <th scope="row">{{ issue.id }}</th>
          <td>
            <router-link :to="this.issueDetailsLink(issue.id)">{{
              issue.name
            }}</router-link>
          </td>
          <td>{{ issue.user.name }}</td>
          <td>{{ issue.status }}</td>
          <td>{{ issue.introduced }}</td>
          <td>{{ issue.updated }}</td>
          <td>{{ issue.discontinued }}</td>
          <td>{{ issue.category }}</td>
          <td>{{ issue.application }}</td>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: "issues",
  methods: {
    async loadIssues() {
      await this.$store.dispatch("issues/loadIssues");
    },
    issueDetailsLink(id) {
      return `/issues/` + id;
    },
    //...mapActions(["issues/loadissues"])
  },
  created() {
    this.loadIssues();
  },
  // computed: ...mapGetters(["issueList"])
  computed: {
    issues() {
      return this.$store.state.issues.issueList;
    },
  },
};
</script>
