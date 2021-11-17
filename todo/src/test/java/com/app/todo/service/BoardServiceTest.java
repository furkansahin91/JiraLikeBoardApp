package com.app.todo.service;

import com.app.todo.entity.Task;
import com.app.todo.model.GetBoardResponse;
import com.app.todo.model.PostBoardRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    BoardService boardService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    BoardRepository boardRepository;

    @Mock
    RestTemplate restTemplate;

    @Test
    public void createBoard() {
        PostBoardRequest request = new PostBoardRequest();
        request.setDescription("board1");
        request.setName("my board");

        boardService.createBoard(request);

        verify(boardRepository, atLeastOnce()).save(any());
    }

    @Test
    public void deleteBoard() {

        Board board = new Board();
        board.setName("board1");
        board.setDescription("general board");

        UUID uuid = UUID.randomUUID();
        board.setUuid(uuid.toString());
        when(boardRepository.findByUuid(anyString())).thenReturn(board);
        boardService.deleteBoard(uuid.toString());

        verify(boardRepository, atLeastOnce()).delete(board);
    }

    @Test
    public void getBoard() {
        Board board = new Board();
        board.setName("board1");
        board.setDescription("general board");

        UUID uuid = UUID.randomUUID();
        board.setUuid(uuid.toString());
        when(boardRepository.findByUuid(anyString())).thenReturn(board);
        List<Task> taskList = new ArrayList<>();
        Task task = new Task();
        task.setBoard(board);
        task.setName("task");
        task.setUuid(UUID.randomUUID().toString());
        task.setUserid(UUID.randomUUID().toString());
        taskList.add(task);
        board.setTasks(taskList);
        GetBoardResponse response = boardService.getBoard(uuid.toString());
        assertEquals(response.getBoard().getDescription(),"general board");

    }
}
