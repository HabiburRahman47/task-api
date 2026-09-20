package task_api.task;

import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @Size(max = 5000, message = "Description must no exceed 5000")
        String description,
        TaskStatus status,
        TaskPriority priority
) {


}
