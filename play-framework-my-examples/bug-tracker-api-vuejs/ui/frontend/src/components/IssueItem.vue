<template>
  <div>
    <div class="card">
      <!--<img src="img_avatar.png" alt="Avatar" style="width:100%">-->
      <div class="container">
        <h4>
          Name: <b>{{ issue.name }}</b>
        </h4>
        <h4>Assigned to: {{ user.name }}</h4>
        <h4>Application: {{ issue.application }}</h4>
        <h4>Category: {{ issue.category }}</h4>
        <h4>
          Status: <b>{{ issue.status }}</b>
        </h4>

        <h4>Introduced: {{ issue.introduced }}</h4>
        <h4>Updated: {{ issue.updated }}</h4>
        <h4>Discontinued: {{ issue.discontinued }}</h4>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "issues",
  methods: {
    async loadIssue() {
      await this.$store.dispatch("issues/loadIssue", this.$route.params.id);
    },
    //...mapActions(["issues/loadissues"])
  },
  created() {
    console.log(this.$route.params.id);
    this.loadIssue();
  },
  // computed: ...mapGetters(["issueList"])
  computed: {
    issue() {
      return this.$store.state.issues.issue;
    },
    user() {
      return { ...this.issue.user };
    },
  },
};
</script>
