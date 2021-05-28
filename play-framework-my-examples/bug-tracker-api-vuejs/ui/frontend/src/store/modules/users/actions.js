import axios from "axios";

export default {
  async loadUsers({commit}) {

    const response = await axios.get("http://localhost:9000/api/users/");

    const responseData = response.data.body;

    if (!responseData) {
      throw new Error(responseData.message || "Failed to fetch");
    }

    const users = [];

    for (const key in responseData) {
      const user = {
        id: responseData[key].id,
        name: responseData[key].name,
        email: responseData[key].email,
        password: responseData[key].password,
      };
      users.push(user);
    }

    commit("setUsers", users);
    commit("setFetchTimestamp");
  },
  async loadUser({commit},  id) {

    const response = await axios.get(`http://localhost:9000/api/users/${id}`)

    const responseData = response.data.body;

    if (!responseData) {
      throw new Error(responseData.message || "Failed to fetch");
    }

    const user = responseData;

    commit("setUser", user);
  },
};
