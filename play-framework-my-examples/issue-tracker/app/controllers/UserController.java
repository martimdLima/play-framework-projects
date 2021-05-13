package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.i18n.MessagesApi;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import models.ContactInfo;
import models.User;

import views.html.userList;
import views.html.userDescription;
import views.html.createUser;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static play.libs.Scala.asScala;

public class UserController extends Controller {

    final private DataController dataController;
    final private FormFactory formFactory;
    final private Form<User> form;

    private final Logger logger = LoggerFactory.getLogger(getClass()) ;

    @Inject
    private MessagesApi messagesApi;

    @Inject
    public UserController(DataController dataController, FormFactory formFactory) {
        this.form = formFactory.form(User.class);
        this.dataController = dataController;
        this.formFactory = formFactory;
    }

    public Result listUsers(Http.Request request){
        List<User> users = dataController.getUserList();
        ContactInfo contactInfo = dataController.getContactInfo();

        return ok(userList.render(asScala(users), contactInfo, request, messagesApi.preferred(request)));
    }

    public Result detailUser(Long id, Http.Request request){
        List<User> users = dataController.getUserList();
        ContactInfo contactInfo = dataController.getContactInfo();

        User user = users.get(id.intValue());
        if(user==null){
            return notFound();
        }
        return ok(userDescription.render(user, contactInfo, request));
    }

    public Result createView(Http.Request request) {
        ContactInfo contactInfo = dataController.getContactInfo();
        return ok(createUser.render(formFactory.form(User.class), contactInfo, request,  messagesApi.preferred(request)));
    }


    public Result create(Http.Request request) {
        final Form<User> uForm = form.bindFromRequest(request);
        ContactInfo contactInfo = dataController.getContactInfo();

        if (uForm.hasErrors()) {
            return ok(createUser.render(uForm, contactInfo, request, messagesApi.preferred(request)));
        }

        int id = (int)(Math.random()*(10000-0+1)+0);
        User user = new User(new Long(id), uForm.get().getName(), uForm.get().getEmail(), uForm.get().getPassword());;

        this.dataController.addUser(user);
        return redirect(routes.UserController.listUsers());
    }

}