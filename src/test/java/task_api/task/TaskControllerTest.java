package task_api.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskResponse createTaskResponse() {
        return new TaskResponse(
                1L,
                "Learn Spring Boot",
                "Build REST API",
                TaskStatus.TODO,
                TaskPriority.MEDIUM,
                Instant.parse("2026-09-18T02:00:00Z")
        );
    }

    private CreateTaskRequest createTaskRequest() {
        return new CreateTaskRequest(
                "Learn Spring Boot",
                "Build REST API"
        );
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        TaskPageResponse response = new TaskPageResponse(
                List.of(
                        new TaskResponse(
                                1L,
                                "Learn Spring Boot",
                                "Build a REST API",
                                TaskStatus.TODO,
                                TaskPriority.MEDIUM,
                                java.time.Instant.parse("2026-01-10T00:00:00Z")
                        )
                ),
                0,
                10,
                1,
                1,
                true,
                true
        );
        when(taskService.getAllTasks(
                eq(TaskStatus.TODO),
                eq(TaskPriority.MEDIUM),
                any(Pageable.class)
        )).thenReturn(response);

        mockMvc.perform(get("/api/tasks")
                        .param("status", "TODO")
                        .param("priority", "MEDIUM")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt,desc")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Learn Spring Boot"));

    }

    @Test
    void shouldCreateTask() throws Exception {
        TaskResponse taskResponse = createTaskResponse();

        when(taskService.createTask(any(CreateTaskRequest.class)))
                .thenReturn(taskResponse);

        mockMvc.perform(
                post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                createTaskRequest()
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
                .andExpect(jsonPath("$.status").value("TODO"));

    }

    @Test
    void shouldRejectBlankTitle() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(
                "",
                "This is test"
        );

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Failed"))
                .andExpect(jsonPath("$.detail").value("One or more fields are invalid"))
                .andExpect(jsonPath("$.errors.title").value("Title is required"));

    }

    @Test
    void shouldGetTaskById() throws Exception {
        when(taskService.getTaskById(1L))
                .thenReturn(createTaskResponse());

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        TaskResponse response = new TaskResponse(
                1L,
                "Learn Spring Boot",
                "Practice partial updates",
                TaskStatus.IN_PROGRESS,
                TaskPriority.MEDIUM,
                java.time.Instant.parse("2026-01-01T00:00:00Z")
        );

        when(taskService.updateTask(
                eq(1L),
                any(UpdateTaskRequest.class)
        )).thenReturn(response);

        UpdateTaskRequest request = new UpdateTaskRequest(
                null,
                null,
                TaskStatus.IN_PROGRESS,
                null
        );

        mockMvc.perform(patch("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));


    }

    @Test
    void shouldFilterTaskByStatusAndPriority() throws Exception {
        // Arrange
        TaskPageResponse response = new TaskPageResponse(
                List.of(),
                0,
                10,
                0,
                0,
                true,
                true
        );
        when(taskService.getAllTasks(
                eq(TaskStatus.TODO),
                eq(TaskPriority.MEDIUM),
                any(Pageable.class)
        )).thenReturn(response);

        // Act
        mockMvc.perform(get("/api/tasks")
                    .param("status", "TODO")
                    .param("priority", "MEDIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));

        // Assert

        // Verify
    }

}
