package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(JPAIssueRepository.class)
public interface IssueRepository {

    CompletionStage<Issue> add(Issue issue);

    CompletionStage<Stream<Issue>> list();
}

