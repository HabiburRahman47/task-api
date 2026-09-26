package task_api.task;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecifications {
    private TaskSpecifications() {

    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> build(String search, TaskStatus status, TaskPriority priority) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                String pattern = "%" + search.toLowerCase() + "%";

                Predicate titleMatches = criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("title")
                        ),
                        pattern
                );

                Predicate descriptionMatches = criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("description")
                        ),
                        pattern
                );

                predicates.add(
                        criteriaBuilder.or(
                                titleMatches,
                                descriptionMatches
                        )
                );
            }

            if (status != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("status"),
                                status
                        )
                );
            }

            if (priority != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("priority"),
                                priority
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }


}
