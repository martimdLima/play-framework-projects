<template>
  <div>
    <h1>User {{ user.id }} Details</h1>
    <div class="card">
      <!--<img src="img_avatar.png" alt="Avatar" style="width:100%">-->
      <div class="container">
        <h4>
          <b>{{ user.name }}</b>
        </h4>
        <p>{{ user.email }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "issues",
  methods: {
    async loadUser() {
      await this.$store.dispatch("users/loadUser", this.$route.params.id);
    },
    //...mapActions(["issues/loadissues"])
  },
  created() {
    console.log(this.$route.params.id);
    this.loadUser();
  },
  // computed: ...mapGetters(["issueList"])
  computed: {
    user() {
      return this.$store.state.users.user;
    },
  },
};
</script>
