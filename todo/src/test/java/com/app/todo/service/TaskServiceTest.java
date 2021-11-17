package com.app.todo.service;

import com.app.todo.entity.Task;
import com.app.todo.model.PostTaskRequest;
import com.app.todo.entity.Board;
import com.app.todo.repository.BoardRepository;
import com.app.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    BoardRepository boardRepository;

    @Mock
    RestTemplate restTemplate;

    @Test
    public void createTask() {
        PostTaskRequest request = new PostTaskRequest();
        request.setDescription("task1");
        request.setName("my task");
        request.setUser(UUID.randomUUID().toString());

        Board board = new Board();
        board.setName("my board");
        when(boardRepository.findByUuid(anyString())).thenReturn(board);

        ResponseEntity<Object> response = new ResponseEntity<Object>(HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), any())).thenReturn(response);
        taskService.createTask(request, "board_1");

        verify(taskRepository, atLeastOnce()).save(any());
    }

    @Test
    public void deleteTask() {

        Task task = new Task();
        task.setName("task1");

        UUID uuid = UUID.randomUUID();
        task.setUuid(uuid.toString());
        when(taskRepository.findByUuid(anyString())).thenReturn(task);
        taskService.deleteTask(uuid.toString());

        verify(taskRepository, atLeastOnce()).delete(task);
    }

    @Test
    public void updateTask() {
        PostTaskRequest request = new PostTaskRequest();
        request.setDescription("task1");
        request.setName("my task");
        request.setUser(UUID.randomUUID().toString());

        Task task = new Task();
        task.setName("task");
        task.setUuid(UUID.randomUUID().toString());
        ResponseEntity<Object> response = new ResponseEntity<Object>(HttpStatus.OK);
        when(taskRepository.findByUuid(anyString())).thenReturn(task);
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(response);
        taskService.updateTask(request, "board_1");

        verify(taskRepository, atLeastOnce()).save(any());
    }

    @Test
    public void putTask() {
        PostTaskRequest request = new PostTaskRequest();
        request.setDescription("task1");
        request.setName("my task");
        request.setUser(UUID.randomUUID().toString());

        Task task = new Task();
        task.setName("task");
        task.setUuid(UUID.randomUUID().toString());
        ResponseEntity<Object> response = new ResponseEntity<Object>(HttpStatus.OK);
        when(taskRepository.findByUuid(anyString())).thenReturn(task);
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(response);
        taskService.putTask(request, "board_1");

        verify(taskRepository, atLeastOnce()).save(any());
    }
}
