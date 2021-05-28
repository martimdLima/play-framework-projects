import axios from "axios";

export default {
  async loadIssues({commit}) {

    const response = await axios.get("http://localhost:9000/api/issues/");

    const responseData = await response.data.body;

    if (!responseData) {
      throw new Error(responseData.message || "Failed to fetch");
    }

    const issues = [];

    for (const key in responseData) {
      //const introduced = new Date(responseData[key].introduced);
      //const updated = new Date(responseData[key].updated);
      //const discontinued = new Date(responseData[key].discontinued);
      const user =  {...responseData[key].user};


      const issue = {
        id: responseData[key].id,
        name: responseData[key].name,
        introduced: new Intl.DateTimeFormat("pt-PT").format(new Date(responseData[key].introduced)),
        updated: new Intl.DateTimeFormat("pt-PT").format(new Date(responseData[key].updated)),
        discontinued: new Intl.DateTimeFormat("pt-PT").format(new Date(responseData[key].discontinued)),
        application: responseData[key].application,
        category: responseData[key].category,
        status: responseData[key].status,
        summary: responseData[key].summary,
        description: responseData[key].description,
        user: user
      };


      issues.push(issue);
    }

    commit("setIssues", issues);
    commit("setFetchTimestamp");
  },
  async loadIssue({commit},  id) {

    const response = await axios.get(`http://localhost:9000/api/issues/${id}`)

    const responseData = response.data.body;

    if (!responseData) {
      throw new Error(responseData.message || "Failed to fetch");
    }

    const issue = responseData;

    issue.introduced = new Intl.DateTimeFormat("pt-PT").format(new Date(issue.introduced)),
    issue.updated = new Intl.DateTimeFormat("pt-PT").format(new Date(issue.updated)),
    issue.discontinued = new Intl.DateTimeFormat("pt-PT").format(new Date(issue.discontinued)),

    commit("setIssue", issue);
  },
};
