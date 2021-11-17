package com.app.todo.service;

import com.app.todo.model.BoardModel;
import com.app.todo.model.GetBoardResponse;
import com.app.todo.model.PostBoardRequest;
import com.app.todo.model.Task;
import com.app.todo.repository.BoardRepository;
import com.app.todo.exception.NotFoundException;
import com.app.todo.model.GetBoardsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public GetBoardsResponse getBoards(Integer pageNo, Integer pageSize) {
        GetBoardsResponse response = new GetBoardsResponse();
        BoardModel boardModel = new BoardModel();
        List<BoardModel> boardModelList = new ArrayList<>();
        List<com.app.todo.entity.Board> boards = getBoardsByPagination(pageNo, pageSize);

        for (com.app.todo.entity.Board board : boards) {
            boardModel = new BoardModel();
            boardModel.setDescription(board.getDescription());
            boardModel.setName(board.getName());
            boardModel.setId(UUID.fromString(board.getUuid()));
            boardModelList.add(boardModel);
        }
        response.setBoards(boardModelList);
        return response;
    }

    public List<com.app.todo.entity.Board> getBoardsByPagination(Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<com.app.todo.entity.Board> pagedResult = boardRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return Collections.emptyList();
    }

    public UUID createBoard(PostBoardRequest postBoardRequest) {
        com.app.todo.entity.Board board = new com.app.todo.entity.Board();
        board.setDescription(postBoardRequest.getDescription());
        board.setName(postBoardRequest.getName());
        board.setUuid(UUID.randomUUID().toString());
        boardRepository.save(board);
        return UUID.fromString(board.getUuid());
    }

    public void deleteBoard(String id) {
        com.app.todo.entity.Board board = boardRepository.findByUuid(id);
        if (board != null) {
            boardRepository.delete(board);
            return;
        }
        throw new NotFoundException("board not found", id, "id");
    }

    public GetBoardResponse getBoard(String id) {
        GetBoardResponse response = new GetBoardResponse();
        BoardModel boardModel = new BoardModel();

        com.app.todo.entity.Board board = boardRepository.findByUuid(id);
        if (board == null) {
            throw new NotFoundException("board not found", id, "id");
        }

        boardModel.setId(UUID.fromString(board.getUuid()));
        boardModel.setName(board.getName());
        boardModel.setDescription(board.getDescription());

        List<Task> taskModelList = new ArrayList<>();
        for (com.app.todo.entity.Task task : board.getTasks()) {
            Task taskModel = new Task();
            taskModel.setDescription(task.getDescription());
            taskModel.setName(task.getName());
            taskModel.setStatus(task.getStatus());
            taskModel.setId(UUID.fromString(task.getUuid()));
            taskModel.setUser(UUID.fromString(task.getUserid()));
            taskModelList.add(taskModel);
        }
        boardModel.setTasks(taskModelList);
        response.setBoard(boardModel);
        return response;
    }
}
