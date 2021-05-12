$ ->
  $.get "/users", (users) ->
    $.each users, (index, user) ->
      $("#users").append $("<li>").text user.name

  $.get "/issues", (issues) ->
    $.each issues, (index, issue) ->
      $("#issues").append $("<li>").text issue.summary
      $("#issues").append $("<li>").text issue.description