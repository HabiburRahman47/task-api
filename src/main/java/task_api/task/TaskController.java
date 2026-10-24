package task_api.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private static final int MAX_PAGE_SIZE = 100;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Create a task",
            description = "Creates a new task with TODO and MEDIUM status priority by default."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid task data")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @Operation(
            summary = "List tasks",
            description = "Returns a paginated, sorted and filtered list of tasks."
    )
    @ApiResponse(responseCode = "200", description = "Tasks returned successfully")
    @GetMapping
    public TaskPageResponse getAllTasks(
            @Parameter(description = "Case-insensitive search by title and description")
            @RequestParam(required = false) String search,
            @Parameter(description = "Filter by task status")
            @RequestParam(required = false) TaskStatus status,

            @Parameter(description = "Filter by task priority")
            @RequestParam(required = false) TaskPriority priority,

            @Parameter(description = "Zero-based page number")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size from 1 to 100")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Sort property and direction")
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be greater than or equal to 0");
        }

        if (size <1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Size must be between 1 and " + MAX_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                parseSort(sort)
        );
        return taskService.getAllTasks(
                search,
                status,
                priority,
                pageable
        );
    }


    @Operation(
            summary = "Get a task by Id",
            description = "Return one task by its database id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @Operation(
            summary = "Update a task",
            description = "Update only the fields supplied in the request."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id,
                                   @Valid @RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @Operation(
            summary = "Delete a task",
            description = "Permanently deletes a task by its database ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
       taskService.deleteTask(id);
    }


    private Sort parseSort(String sort) {
        String[] sortParts = sort.split(",");
        String property = sortParts[0];

        Set<String> allowedProperties = Set.of(
                "id",
                "title",
                "status",
                "priority",
                "createdAt"
        );

        if (!allowedProperties.contains(property)) {
            throw new IllegalArgumentException("Invalid sort property: " + property);
        }

        Sort.Direction direction = Sort.Direction.ASC;

        if (sortParts.length > 1) {
            direction = Sort.Direction.fromString(sortParts[1]);
        }
        return Sort.by(direction, property);
     }
}