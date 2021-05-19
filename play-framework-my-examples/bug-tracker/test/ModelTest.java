import io.ebean.PagedList;
import models.Issue;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;
import repository.IssueRepository;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class ModelTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    private String formatted(Date date) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    @Test
    public void findById() {
        final IssueRepository issueRepository = app.injector().instanceOf(IssueRepository.class);
        final CompletionStage<Optional<Issue>> stage = issueRepository.lookup(21L);

        await().atMost(1, SECONDS).until(() -> {
            final Optional<Issue> macintosh = stage.toCompletableFuture().get();
            return macintosh
                .map(mac -> mac.name.equals("Macintosh") && formatted(mac.introduced).equals("1984-01-24"))
                .orElseGet(() -> false);
        });
    }
    
    @Test
    public void pagination() {
        final IssueRepository issueRepository = app.injector().instanceOf(IssueRepository.class);
        CompletionStage<PagedList<Issue>> stage = issueRepository.page(1, 20, "name", "ASC", "");

        // Test the completed result
        await().atMost(1, SECONDS).until(() -> {
            PagedList<Issue> computers = stage.toCompletableFuture().get();
            return computers.getTotalCount() == 574 &&
                computers.getTotalPageCount() == 29 &&
                computers.getList().size() == 20;
        });
    }
    
}
