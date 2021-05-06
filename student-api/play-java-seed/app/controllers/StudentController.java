package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Student;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import store.StudentStore;
import utils.Util;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class StudentController extends Controller {

    // The actions will be implemented using asynchronous, non-blocking code.
    // This means that our action methods will return CompletionStage<Result> instead of just Result.
    private HttpExecutionContext ec;
    private StudentStore studentStore;

    // Dealing with asynchronous programming in a Play Framework controller, we have to provide an HttpExecutionContext.
    @Inject
    public StudentController(HttpExecutionContext ec, StudentStore studentStore) {
        // Both the HttpExecutionContext and StudentStore are inject into the constructor via the @Inject annotation
        this.studentStore = studentStore;
        this.ec = ec;
    }

    // Mapped as a POST action, this method handles the creation of the Student object
    // /student

    // returns a CompletionStage<Result>, which enables writing non-blocking code using the CompletedFuture.supplyAsync method.
    public CompletionStage<Result> create(Http.Request request) {
        // uses a call from the injected Http.Request class to get the request body into Jackson's JsonNode class
        JsonNode json = request.body().asJson();
        return supplyAsync(() -> {
            if (json == null) {
                return badRequest(Util.createResponse("Expecting Json data", false));
            }

            // returning a CompletionStage<Result> enables writing non-blocking code using the CompletedFuture.supplyAsync method.

            Optional<Student> studentOptional = studentStore.addStudent(Json.fromJson(json, Student.class));
            return studentOptional.map(student -> {
                JsonNode jsonObject = Json.toJson(student);
                return created(Util.createResponse(jsonObject, true));
            }).orElse(internalServerError(Util.createResponse("Could not create data.", false)));
        }, ec.current());
    }

    // the listStudents action returns a list of all the students that have been stored so far.
    // It's mapped to / as a GET request:
    public CompletionStage<Result> listStudents() {
        return supplyAsync(() -> {
            Set<Student> result = studentStore.getAllStudents();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonData = mapper.convertValue(result, JsonNode.class);
            return ok(Util.createResponse(jsonData, true));
        }, ec.current());
    }

    // To get a student, the id of the student is passed as a path parameter in a GET request to /student/:id.
    // This will hit the retrieve action.
    public CompletionStage<Result> retrieve(int id) {
        return supplyAsync(() -> {
            final Optional<Student> studentOptional = studentStore.getStudent(id);
            return studentOptional.map(student -> {
                JsonNode jsonObjects = Json.toJson(student);
                return ok(Util.createResponse(jsonObjects, true));
            }).orElse(notFound(Util.createResponse("Student with id:" + id + " not found", false)));
        }, ec.current());
    }


    // A PUT request to /student hits the StudentController.update method,
    // which updates the student information by calling the updateStudent method of the StudentStore
    public CompletionStage<Result> update(Http.Request request) {
        JsonNode json = request.body().asJson();
        return supplyAsync(() -> {
            if (json == null) {
                return badRequest(Util.createResponse("Expecting Json data", false));
            }
            Optional<Student> studentOptional = studentStore.updateStudent(Json.fromJson(json, Student.class));
            return studentOptional.map(student -> {
                if (student == null) {
                    return notFound(Util.createResponse("Student not found", false));
                }
                JsonNode jsonObject = Json.toJson(student);
                return ok(Util.createResponse(jsonObject, true));
            }).orElse(internalServerError(Util.createResponse("Could not create data.", false)));
        }, ec.current());
    }

    // The delete action is mapped to /student/:id.
    // The id is supplied to identify which record to delete
    public CompletionStage<Result> delete(int id) {
        return supplyAsync(() -> {
            boolean status = studentStore.deleteStudent(id);
            if (!status) {
                return notFound(Util.createResponse("Student with id:" + id + " not found", false));
            }
            return ok(Util.createResponse("Student with id:" + id + " deleted", true));
        }, ec.current());
    }
}