package com.app.todo.service;

import com.app.todo.entity.Task;
import com.app.todo.model.PostTaskRequest;
import com.app.todo.model.PostTaskResponse;
import com.app.todo.entity.Board;
import com.app.todo.exception.NotFoundException;
import com.app.todo.model.DeleteOrphanedTaskRequest;
import com.app.todo.repository.BoardRepository;
import com.app.todo.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class TaskService {

    public static final String USER = "user";
    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;
    private final RestTemplate restTemplate;

    public TaskService(TaskRepository taskRepository, BoardRepository boardRepository, RestTemplate restTemplate) {
        this.taskRepository = taskRepository;
        this.boardRepository = boardRepository;
        this.restTemplate = restTemplate;
    }

    public PostTaskResponse createTask(PostTaskRequest postTaskRequest, String boardId) {
        Board board = boardRepository.findByUuid(boardId);
        if (board == null) {
            throw new NotFoundException("board not found", boardId, "id");
        }
        Task task = new Task();
        task.setDescription(postTaskRequest.getDescription());
        task.setName(postTaskRequest.getName());
        task.setStatus("CREATED");
        task.setUuid(UUID.randomUUID().toString());

        checkUserUuid(postTaskRequest.getUser());
        task.setUserid(postTaskRequest.getUser());
        task.setBoard(board);
        taskRepository.save(task);

        PostTaskResponse response = preparePostTaskResponse(task);
        return response;
    }

    private void checkUserUuid(String user) {
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity("http://localhost:8082/users/" + user, Object.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new NotFoundException("given user id not found in centralized user service", user, "user");
            }
        } catch (Exception e) {
            throw new NotFoundException("given user id not found in centralized user service", user, USER);
        }
    }

    public PostTaskResponse putTask(PostTaskRequest postTaskRequest, String taskId) {
        Task task = taskRepository.findByUuid(taskId);
        if (task == null) {
            throw new NotFoundException("task not found", taskId, "id");
        }

        checkUserUuid(postTaskRequest.getUser());
        task.setUserid(postTaskRequest.getUser());
        task.setName(postTaskRequest.getName());
        task.setDescription(postTaskRequest.getDescription());
        task.setUuid(UUID.randomUUID().toString());
        task.setStatus("CREATED");
        taskRepository.save(task);

        PostTaskResponse response = preparePostTaskResponse(task);

        return response;
    }

    private PostTaskResponse preparePostTaskResponse(Task task) {
        PostTaskResponse response = new PostTaskResponse();
        com.app.todo.model.Task taskModel = new com.app.todo.model.Task();
        taskModel.setId(UUID.fromString(task.getUuid()));
        taskModel.setUser(UUID.fromString(task.getUserid()));
        taskModel.setName(task.getName());
        taskModel.setDescription(task.getDescription());
        taskModel.setStatus(task.getStatus());
        response.setTask(taskModel);
        return response;
    }

    public void updateTask(PostTaskRequest postTaskRequest, String taskId) {
        Task task = taskRepository.findByUuid(taskId);
        if (task == null) {
            throw new NotFoundException("task not found", taskId, "id");
        }
        checkUserUuid(postTaskRequest.getUser());
        task.setUserid(postTaskRequest.getUser());
        task.setName(postTaskRequest.getName());
        task.setDescription(postTaskRequest.getDescription());
        taskRepository.save(task);
    }

    public void deleteTask(String id) {
        Task task = taskRepository.findByUuid(id);
        if (task == null) {
            throw new NotFoundException("task not found", id, "id");
        }
        taskRepository.delete(task);
    }

    public void deleteOrphanedTaskRequest(DeleteOrphanedTaskRequest deleteOrphanedTaskRequest) {
        Task task = taskRepository.findByUserid(deleteOrphanedTaskRequest.getData().getUser());
        if(task != null) {
            taskRepository.deleteByUserid(task.getUserid());
        }
    }
}
