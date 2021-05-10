- [Introduction](#orgfeadacf)
- [Create a New Play App](#orgd6a901a)
- [Routing Requests](#orga5e3134)
- [Controllers](#orge4f878d)
- [Server-Side Templates](#org9136df5)
- [JSON](#org8a24b80)
- [Static Assets](#org596b984)
- [Asset Compiler](#org11b50a0)
- [Testing](#org7266eac)
- [Configuration](#orgb37a0ab)
- [Build](#org6b9f77a)
- [Useful Links](#org6da3840)

---


<a id="orgfeadacf"></a>

# Introduction

The Play Framework is a high velocity web framework for Java and Scala that enables a highly productive workflow but doesn’t sacrifice scalability. Play features a “just-hit-refresh” workflow that enables a rapid development cycle. The compilation and application reloading happens behind the scenes. Play is built for the modern web by being non-blocking, RESTful by default, and having built-in asset compilers for modern client-side technologies like TypeScript, CoffeeScript, LESS, and more.


<a id="orgd6a901a"></a>

# Create a New Play App

The easiest way to get started with Play is with Lightbend Starter Projects - a place where you can download sample projects that contains all that you need to get started:

-   [Play Starter](http://developer.lightbend.com/start/?group=play)

Download one of the sample projects.

Unzip it and run &ldquo;./sbt run” command in your terminal.

Your new Play application should be running and you should be able to browse to localhost:9000 and see your new application.

To get to the local documentation for Play, navigate to localhost:9000/@documentation.

Another way to create a new app is by using the sbt new command. You will need sbt 0.13.15 or newer. To create a new Java project using “sbt new,” you can run the following command:

-   `sbt new playframework/play-java-seed.g8`

Or, for a new Scala project:

-   `sbt new playframework/play-scala-seed.g8`

After that, you can either use a basic code editor or import your project in IntelliJ or Eclipse by using ScalaIDE.

Your new project has the following layout:
| DIRECTORY | DESCRIPTION                                                                                          |
|--------- |---------------------------------------------------------------------------------------------------- |
| app       | The app source directory for Java, Scala, and client side source code.                               |
| conf      | The config directory containing the route mapping, application configuration, and log configuration. |
| public    | The static assets directory (e.g. images, HTML, CSS, JavaScript).                                    |
| test      | The test source directory.                                                                           |
| project   | sbt configuration files.                                                                             |


<a id="orga5e3134"></a>

# Routing Requests

The conf/routes file defines how Play routes requests based on their HTTP verb and path. When you make a request to <http://localhost:9000> your browser makes a GET request with a / path. You can see in the default routes file there is a routing definition to handle that case:

-   `GET / controllers.HomeController.index()`

The third parameter is the method (the action) that will be responsible for handling the request and returning a response. The structure of the routes file is:

-   `VERB PATH CONTROLLER_CLASS.CONTROLLER_METHOD`

Only valid HTTP methods are allowed. If you change GET to FOO, you will get a compile error indicating that FOO is not a valid method.

The path part of the routes file can be parameterized to extract information and pass it to the controller. For instance, to pull an id out of a path, you would do this:

-   `GET /user/:id controllers.Users.get(id: Long)`

The &ldquo;:&rdquo; matches on one / separated segment, allowing you to extract multiple values like:

-   `GET /user/:id/:name controllers.Users.get(id: Long, name: String)`

You can also extract the rest of the path using a &ldquo;\*&rdquo; like this: `GET /files/*file controllers.Files.get(file: String)` Paths can also use regex expressions to limit what they match on.

Query string parameters can be automatically extracted into controller method parameters. To handle a GET request to /foo?bar=neat, define a route like this:

-   `GET /foo controllers.FooController.get(bar: String)`

The query string parameters are type-safe, so if you set the type as Int, then there will be an error if the parameter cannot be converted to an Int.

You can also have default and optional parameters.

One of the reasons that Play compiles the routes file is to provide a reverse routing API so that you never have to hard code URLs into your application. Instead, you call a method in the reverse router, which returns the route defined by the “routes” file. This enables you to easily refactor your URLs without breaking your app.


<a id="orge4f878d"></a>

# Controllers

Controllers in Play are responsible for handling a request and returning a response. Here is a basic Java controller (which would live in app/controllers/FooController.java):

```java
package controllers;

import play.mvc.Result;
import play.mvc.Controller;

public class FooController extends Controller {
    public Result get() {
        return ok("Hello Foo");
    }
}
```

By extending the base play.mvc.Controller class, we pull in some convenience methods, but doing so is not required. The controller instance is created by using Guice, a dependency injection framework. The get() method returns a play.mvc.Result, which represents the HTTP response. In this case, the response is a status code &ldquo;200 OK&rdquo; response because the ok helper was used to set that status code. There are many other helpers like notFound and badRequest that wrap the general purpose play.mvc.Status API. The response body in this example is just a String, but it could also be HTML content, a stream, a file, etc.

The corresponding Scala controller is quite similar (and would live in app/controllers/FooController.scala):

```scala
package controllers
import javax.inject.Inject
import play.api.mvc.BaseController
import play.api.mvc.ControllerComponents

class FooController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
    def get = Action {
        Ok(&quot;Hello foo&quot;)
    }
}
```

The primary difference with this Scala example is that the controller returns an Action which holds a function that takes a request (optionally specified) and returns a response. Just as the Java controller, by default, we use dependency injection to create an instance of the controller. It can be provided by Guice or it can use compile-time Dependency Injection. The controllerComponents instance injected has a number of built-in utilities so that we can have simpler controllers, but as long as you have an Action, it will work.


The Controller class (in the Java API) has some convenience methods to interact with the other parts of the request and response:
| METHOD                                                            | DESCRIPTION                                                                                                                                                |
|----------------------------------------------------------------- |---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| ctx()                                                             | Returns the HTTP context, which can be used to retrieve and store data relevant to the current request.                                                    |
| flash(), flash(String key), and flash(String key, String value)   | Can be used to access a state that is only available for a single request after the current one (this is useful for displaying messages after a redirect). |
| session(), session(String key), session(String key, String value) | Can be used to access the session state which is backed by a cookie.                                                                                       |
| request()                                                         | Return the current HTTP request object, which can be used for reading HTTP request headers, body, and any other data related to the request.               |
| response()                                                        | Returns the current HTTP response object, which can be used to set cookies, HTTP headers, etc.                                                             |


In the Scala API, these types of operations are done either on the Action function’s optional request parameter or on the Result, for example:
```scala
def get = Action { request =>
  Ok("Hello")
    .withHeaders("Foo" -> "bar")
    .withSession("SessionFoo" -> "The-value")
}
```

There are other response helper methods that you can use depending on the HTTP Status code you need to be returned:
| HTTP STATUS                | JAVA                      | SCALA                           |
|-------------------------- |------------------------- |------------------------------- |
| 200 OK                     | ok                        | Ok                              |
| 201 Created                | created()                 | Created                         |
| 301 Moved Permanently      | movedPermanently          | MovedPermanently                |
| 302 Found                  | found                     | Found                           |
| 303 See Other              | seeOther or redirect      | SeeOther                        |
| 307 Temporary Redirect     | temporaryRedirect         | TemporaryRedirect               |
| 308 Permanent Redirect     | permanentRedirect         | PermanentRedirect               |
| 404 Not Found              | notFound                  | NotFound                        |
| 406 Not Acceptable         | notAcceptable             | NotAcceptable                   |
| 415 Unsupported Media Type | unsupportedMediaType      | UnsupportedMediaType            |
| 500 Internal Server Error  | internalServerError       | InternalServerError             |
| Any status                 | status(413, “Oops&ldquo;) | Status(413)(&ldquo;Oops&rdquo;) |


Controllers in Play are internally asynchronous and non-blocking, and there is support for using non-blocking APIs in a more idiomatic way. For example, you can just return a Result directly, as shown before, or your actions can return CompletableFuture<Result>:
```java
public CompletableFuture<Result> getAsync() {
  // Some async API call
  CompletableFuture<String> asyncApiResponse = someAsyncApi();
  return asyncApiResponse.thenApply(value -> ok("Api Result: " + value));
}
```

In Scala, there is Action.async to better integrate with APIs returning a Future:

```scala
def getAsync = Action.async {
  val asyncApiResponse: Future[String] = someAsyncApi()
  asyncApiResponse.map(value => Ok("Api Result: " + value))
}
```

Interceptors can be added to controllers in order to add security, logging, caching, and other custom behaviors. This is called Action Composition. In Play’s Java API, annotations are used to add the interceptors. In Scala, Action Composition is achieved through functional composition.

Controllers go much deeper than the typical request and response handling. For instance, a controller can return a stream or it can be used to setup a push connection (Comet, EventSource, WebSocket, etc). Controllers can also handle more than just HTML; they can be used for JSON, binary files, or any content type using custom Body Parsers.


<a id="org9136df5"></a>

# Server-Side Templates

Web applications can use server-side templates as a way to create HTML content. In Play, the default server-side templating system is Twirl. There are also numerous other plugins that support a large variety of other templating systems. To use the Scala templates, create a something.scala.html file in the app/views directory. The naming of the file is used to name the function that will be called to render the template:
| PATH                            | BECOMES                 |
|------------------------------- |----------------------- |
| app/views/foo.scala.html        | views.html.foo()        |
| app/views/users/show.scala.html | views.html.users.show() |

To use the compiled template from Java, simply call the render static method:
```java
views.html.foo.render()
```

From Scala, use the apply function:
```scala
views.html.foo()
```

Templates can take parameters, so the (optional) first line of a Scala template is the parameters. Every Scala statement in a Scala template is prefixed with an @, so to specify that a template takes a String parameter, use the following:
```java
@(message: String)
```

The body of the template is just a combination of “@” prefixed Scala statements and raw HTML. For instance:
```java
@(title: String, message: String)
<html>
    <head>
        <title>@title</title>
    </head>
    <body>
        <h1>@message</h1>
    </body>
</html>
```

Since the Scala templates are compiled into functions, they are easily composed. If the previous example is named main.scala.html, then to reuse it from within another template, simply do this:
```java
@main("My Page Title", “Some message")
```

Typical template operations like loops just use normal Scala expressions, like so:
```java
@for(name <- names) {
    <li>@name</li>
}
```

A conditional “if” statement would look like:
```java
@if(names.isEmpty()) {
    <h1>Nothing to display</h1>
} else {
    <h1>@{names.size()} names!</h1>
}
```

The Scala templates include a number of other features and patterns, like reusable HTML components, including forms via the @form function. One of the huge benefits of the Scala templates is that you will see compile errors in your browser just like you do with controllers, routes, and everything else that is compiled by Play.


<a id="org8a24b80"></a>

# JSON

In addition to regular HTML content, Play controllers can also receive and return JSON serialized data. The Play Java API wraps the popular Jackson library with some convenience functions. Here is an example Java controller that has actions to get a User from somewhere (a database, for example) and transform it to JSON, and another action to receive a JSON, parse it to a User object, and save it. Let’s first assume that we have the following model:
```java
package models;

public class User {
    public String name;
    public String email;
}
```

And then the controller:
```java
package controllers;

import models.User;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Controller;
import com.fasterxml.jackson.databind.JsonNode;

public class UsersController extends Controller {
    public Result get(Long id) {
        User user = getUserFromSomewhere(id);
            return ok(Json.toJson(user));
    }

    public Result save() {
        JsonNode json = request().body().asJson();
        User user = Json.fromJson(json, User.class);
        saveUserSomehow(user);
        return created("User was created");
    }
}
```

The same thing in Scala works in a similar way, but uses a macro-based API to generate the serializer and de-serializer at compile time, thus avoiding the use of runtime reflection. Again, let’s first create a model class:
```scala
package models
case class User(name: String, email: String)
```

```scala
package controllers
import models._
import play.api.mvc._
import play.api.libs.json.Json

class UsersController(val controllerComponents: ControllerComponents) extends BaseController {
    implicit val userFormat = Json.format[User]

    def get(id: Long) = Action {
        val user: User = getUserFromSomewhere(id)
        Ok(Json.toJson(user))
    }

    def save = Action(parse.json) { request =>
        val user = request.body.as[User]
        saveUser(user)
        Created("User was created")
    }
}
```

These examples show serializing and de-serializing an object. Both the Java and Scala APIs in Play have methods for traversing a JSON structure to locate and extract data, as well as methods to create and manipulate JSON structures.

To set up routing to either of these controller methods, add the following to your routes file:
```java
GET /users/:id controllers.UsersController.get(id: Long)
POST /users controllers.UsersControllers.save
```


<a id="org596b984"></a>

# Static Assets

The public directory contains static assets that do not need to go through a compilation process to be used in the browser. There is a default mapping in the conf/routes file that sets up a way to serve these assets from the *assets* URL prefix using Play’s built-in Assets controller:
```java
GET /assets/*file controllers.Assets.at(path="public", file)
```

At first glance, it seems that these assets are being read directly out of the file system. However, doing so would make Play applications more difficult to deploy since Play uses a container-less deployment model that is ultimately just a bunch of jar files. Instead, Play’s built-in Assets controller serves assets from within the Java classpath. The public directory is actually a source directory that puts its contents into a public package in the classpath (or generated jar file when creating a distribution).

To load an asset via a server-side template, use the reverse router to get the right URL, like so:
```html
<img src="@routes.Assets.at("images/favicon.png")" >
```

Given the previous routing definition, the reverse router will resolve that to the /assets/images/favicon.png path


<a id="org11b50a0"></a>

# Asset Compiler

Play has an Asset Compiler built-in that will compile client-side assets like CoffeeScript and LESS as part of the normal compilation process. This process will also minify JavaScript resources to reduce their size. Assets to be compiled go in either the app/assets/javascripts or app/assets/stylesheets directory. For example, a new app/assets/javascripts/index.coffee file will be compiled and added to the classpath as assets/javascripts/index.js and minified as assets/stylesheets/index.min.js. To load the minified JavaScript via a server-side template, use

```html
<script=“@routes.Assets.versioned(“javascripts/index.js")"></script>
```

For production distributions, Play will also do JavaScript concatenation. There are a number of other open source asset compiler plugins for Play. Check out the sbt-web plugins list.


<a id="org7266eac"></a>

# Testing

The test directory contains the unit, functional, and integration tests for your project. You can write your tests with any framework that has a JUnit compatible test runner. Play has some specific helpers for JUnit: Specs2 (a Scala testing framework) and ScalaTest (another Scala testing framework). You can test different parts of a Play application independently without starting a server, or your tests can also start a Play server, make actual requests against the server, and test the UI with Selenium through a fake browser (HTMLUnit) or through a real browser (Chrome, Firefox, etc). Here is a simple Java and JUnit test of a controller:
```java
package controllers;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.route;

public class HomeControllerTest extends WithApplication {

    @Test
    public void testIndex() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/");

        // app variable is provided by WithApplication.
        Result result = route(app, request);
        assertEquals(OK, result.status());
    }
}
```

The same thing with Scala and ScalaTest would be:
```scala
package controllers
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest {
  "HomeController GET" should {
    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get
      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }
  }
}
```

You can run the tests either using your IDE or from the command line using sbt test. See more about how to run tests with sbt here.


<a id="orgb37a0ab"></a>

# Configuration

The conf/application.conf file contains your application’s default configuration. There, you can override config or define your own. For instance, if you want to create a new config parameter named “foo” with a value of bar, you would simply add the following to the file:

```
foo = bar
```

To read that config in Java, you need to inject a Config object:

```java
import javax.inject.Inject;
import com.typesafe.config.Config;

public class FooReader {
    private final Config config;

    @Inject
    public FooReader(Config config) {
        this.config = config;
    }

    public String getFoo() {
        return this.config.getString("foo");
    }
}
```

In Scala, things are similar, except you need to inject play.api.Configuration instead:

```scala
import javax.inject.Inject
import play.api.Configuration

class FooReeader @Inject()(config: Configuration) {
  def foo = config.get[String]("foo")
}
```

You can specify additional config files to deal with configuration that varies between environments. Play’s config system is built on the Typesafe Config library. See more about how Play uses it here.


<a id="org6b9f77a"></a>

# Build

Play uses the sbt build tool for managing dependencies, compiling the app, running the app, running the tests, and packaging the app. The project/build.properties file specifies the version of sbt to use. Any sbt plugins can be added in the project/plugins.sbt file. The primary build definition is in the build.sbt file, which will look something like this:

```conf
name := "your-project-name"
organization := "com.acme"
version := "1.0-SNAPSHOT"
lazy val root = (project in file(".")).enablePlugins(PlayJava)
scalaVersion := "2.12.3"
libraryDependencies += guice
```

Note: For Scala projects, make sure to use PlayScala instead of PlayJava when enabling plugins. This changes some defaults in Play’s template compiler to make it more idiomatic Scala.

The libraryDependencies section of the build.sbt defines the application dependencies that should be available in a public Maven repository. You can also add your own Maven repository using the resolvers setting. The dependencies in libraryDependencies are a comma-separated list in this form:

```conf
libraryDependencies += "group" % "artifact" % "version"
```

As an example, to add the MySQL driver, add the following line:

```conf
libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.6"
```

Play has a number of optional dependencies with shortcut aliases:

| DEPENDENCY                    | JAVA                  | SCALA                 |
|----------------------------- |--------------------- |--------------------- |
| JDBC Connection Pool and APIs | jdbc                  | javaJdbc              |
| Cache API                     | ehcache (or cacheApi) | ehcache (or cacheApi) |
| Built-in Filters (gzip, etc)  | filters               | filters               |
| Guice APIs and Dependencies   | guice                 | guide                 |
| Play WS                       | ws                    | javaWs                |

Play’s build also supports sub-projects so that you can partition your application into multiple smaller pieces. This can improve build times and make different pieces more easily reusable.


<a id="org6da3840"></a>

# Useful Links

-   [Introduction To Play](https://www.baeldung.com/java-intro-to-the-play-framework)

-   [REST API with Play Framework](https://www.baeldung.com/rest-api-with-play)

-   [Routing In Play Applications](https://www.baeldung.com/routing-in-play)

-   [Asynchronous HTTP Programming with Play Framework](https://www.baeldung.com/java-play-asynchronous-http-programming)

-   [Getting Started With Play Framework](https://dzone.com/refcardz/getting-started-play-framework?chapter=12)

-   [Play Starter](https://developer.lightbend.com/start/?group=play)

-   [Play Framework Tutorials](https://www.playframework.com/documentation/2.6.x/Tutorials)

-   [Building Modern Web Applications with AngularJS and Play Framework](https://www.toptal.com/java/building-modern-web-applications-with-angularjs-and-play-framework)
