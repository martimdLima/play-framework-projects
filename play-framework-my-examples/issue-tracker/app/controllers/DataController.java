package controllers;

import models.ContactInfo;
import models.Issue;
import models.Revenue;
import models.User;

import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DataController {

    public List<User> userList;
    public List<Issue> issueList;
    public List<Revenue> monthlyRevenue;
    public ContactInfo contactInfo;

    public DataController() {
        this.initializeContactInfo();
        this.initializeIssueList();
        this.initializeMonthlyCost();
        this.initializeUserList();
    }

    public List<User> getUserList() {
        return userList;
    }

    public void addUser(User user) {
        this.userList.add(user);
    }

    public void addIssue(Issue issue) {
        this.issueList.add(issue);
    }

    public List<Issue> getIssueList() {
        return issueList;
    }

    public List<Revenue> getLast6MonthsRevenue() {
        List<Revenue> lastMonthsRevenue = new ArrayList<>();
        YearMonth nowYearMonth = YearMonth.now();
        YearMonth sixMonthsAgo = nowYearMonth.minus(Period.ofMonths(7));


        for (Revenue revenue : this.monthlyRevenue) {
            if (revenue.yearMonth.isAfter(sixMonthsAgo)) {
                lastMonthsRevenue.add(revenue);
            }
        }

        return lastMonthsRevenue;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void initializeContactInfo() {
        this.contactInfo = new ContactInfo(
                "(351) 915552081",
                "martim.d.lima@protonmail.com",
                "JBay Solutions");
    }

    public void initializeIssueList() {
        this.issueList = new ArrayList<>();
        this.issueList.add(new Issue(
                new Long(000001), "testIssue", "testApp", "testCategory", "unResolved", "testSummary", "TestDescription", new Date(), new Date()));
        this.issueList.add(new Issue(
                new Long(000002), "testIssue2", "testApp", "testCategory", "unResolved", "testSummary", "TestDescription", new Date(), new Date()));
        this.issueList.add(new Issue(
                new Long(000003), "testIssue3", "testApp", "testCategory", "unResolved", "testSummary", "TestDescription", new Date(), new Date()));
    }

    public void initializeMonthlyCost() {
        this.monthlyRevenue = new ArrayList<>();
        this.monthlyRevenue.add(new Revenue(
                12.1,
                4,
                2020));
        this.monthlyRevenue.add(new Revenue(
                13.231,
                5,
                2020));
        this.monthlyRevenue.add(new Revenue(
                13.1,
                6,
                2020));
        this.monthlyRevenue.add(new Revenue(
                16.2,
                7,
                2020));
        this.monthlyRevenue.add(new Revenue(
                14.731,
                8,
                2020));
        this.monthlyRevenue.add(new Revenue(
                17.4,
                9,
                2020));
        this.monthlyRevenue.add(new Revenue(
                17.13,
                10,
                2020));
        this.monthlyRevenue.add(new Revenue(
                17.9,
                11,
                2020));
        this.monthlyRevenue.add(new Revenue(
                22.3,
                12,
                2020));
        this.monthlyRevenue.add(new Revenue(
                21.84,
                1,
                2021));
        this.monthlyRevenue.add(new Revenue(
                23.04,
                2,
                2021));
        this.monthlyRevenue.add(new Revenue(
                26.1,
                3,
                2021));
        this.monthlyRevenue.add(new Revenue(
                26.61,
                4,
                2021));
    }

    public void initializeUserList() {
        this.userList = new ArrayList<>();
        this.userList.add(new User(new Long(000001), "John Doe", "test@test.com", "secret"));
        this.userList.add(new User(new Long(000002), "John Doe", "test@test.com", "secret"));
        this.userList.add(new User(new Long(000003), "John Doe", "test@test.com", "secret"));

    }

}
