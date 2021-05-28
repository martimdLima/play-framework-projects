<template>
  <div>
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Email</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>
            <router-link :to="this.userDetailsLink(user.id)">{{
              user.name
            }}</router-link>
          </td>
          <td>{{ user.email }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import //mapGetters,
//mapActions
"vuex";

export default {
  name: "Users",
  methods: {
    async loadUsers() {
      await this.$store.dispatch("users/loadUsers");
    },
    userDetailsLink(id) {
      return `/users/` + id;
    },
    //...mapActions(["users/loadUsers"])
  },
  created() {
    this.loadUsers();
  },
  // computed: ...mapGetters(["userList"])
  computed: {
    users() {
      return this.$store.state.users.userList;
    },
  },
};
</script>
