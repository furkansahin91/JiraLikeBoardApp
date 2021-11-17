package com.app.todo.controller;

import com.app.todo.model.PostBoardRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.app.todo.repository.BoardRepository;
import com.app.todo.repository.TaskRepository;
import com.app.todo.service.BoardService;
import com.app.todo.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(initializers = {TodoControllerTest.Initializer.class})
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskRepository taskRepository;
    @MockBean
    private BoardRepository boardRepository;
    @Autowired
    private RestTemplate restTemplate;

    BoardService boardService;
    TaskService taskService;
    TodoController todoController;


    ObjectMapper objectMapper = new ObjectMapper();

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");
    ;

    @Before
    public void init() {
        boardService = new BoardService(boardRepository);
        taskService = new TaskService(taskRepository, boardRepository, restTemplate);
        todoController = new TodoController(boardService, taskService);

        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    public void createABoard() throws Exception {
        PostBoardRequest boardRequest = new PostBoardRequest();
        boardRequest.setDescription("new board");
        boardRequest.setName("board_1");
        String requestJson = objectMapper.writeValueAsString(boardRequest);

        mockMvc.perform(post("/boards").content(requestJson).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }
}
