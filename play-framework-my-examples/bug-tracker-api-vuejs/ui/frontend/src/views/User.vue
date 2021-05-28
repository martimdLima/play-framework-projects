<template>
  <div>
    <User />
  </div>
</template>

<script>
// @ is an alias to /src
import User from "../components/UserItem";

export default {


  name: 'Users List',
  components: {
      User
  }
}
</script>
