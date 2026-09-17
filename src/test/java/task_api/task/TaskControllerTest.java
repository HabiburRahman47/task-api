package task_api.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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

    @Test
    void shouldGetAllTasks() throws Exception {
        when(taskService.getAllTasks())
                .thenReturn(List.of(
                        new TaskResponse(
                                1L,
                                "Learn Spring test",
                                "Learn MockMvc",
                                TaskStatus.TODO,
                                TaskPriority.MEDIUM,
                                Instant.now()
                        )
                ));
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Learn Spring test"));

    }

    @Test
    void shouldCreateTask() throws Exception {
        TaskResponse taskResponse = new TaskResponse(
                1L,
                "Learn Spring Boot",
                "Build REST API with Spring Boot",
                TaskStatus.TODO,
                TaskPriority.MEDIUM,
                Instant.now()
        );

        when(taskService.createTask(any(CreateTaskRequest.class)))
                .thenReturn(taskResponse);

        mockMvc.perform(
                post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Learn Spring Boot",
                                    "description": "Build REST API with Spring Boot"
                                }
                                """)
        )
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
                .andExpect(status().isBadRequest());

    }

}
