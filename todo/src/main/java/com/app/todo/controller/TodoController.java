package com.app.todo.controller;

import com.app.todo.model.*;
import com.app.todo.service.BoardService;
import com.app.todo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;


@RestController
@RequestMapping
public class TodoController {

    final
    BoardService boardService;

    final
    TaskService taskService;

    public TodoController(BoardService boardService, TaskService taskService) {
        this.boardService = boardService;
        this.taskService = taskService;
    }


    @Operation(summary = "List all the boards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All available boards returned successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetBoardsResponse.class))})})
    @RequestMapping(value = "/boards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBoards(@RequestParam(defaultValue = "0", name = "page_no") Integer pageNo,
                                    @RequestParam(defaultValue = "100", name = "page_size") Integer pageSize) {
        GetBoardsResponse response = boardService.getBoards(pageNo, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Create a board")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Board created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @RequestMapping(value = "/boards", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createBoard(@RequestBody PostBoardRequest postBoardRequest) {
        UUID boardId = boardService.createBoard(postBoardRequest);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).location(new URI(boardId.toString())).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Operation(summary = "Get the board details by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Board details returned successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetBoardResponse.class))}), @ApiResponse(responseCode = "404", description = "Board not found with given id",
            content = @Content)})
    @RequestMapping(value = "/boards/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBoards(@PathVariable("id") String id) {
        GetBoardResponse response = boardService.getBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @Operation(summary = "Delete a board by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Board deleted successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetBoardResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Board not found with given id",
                    content = @Content)})
    @RequestMapping(value = "/boards/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteBoard(@PathVariable("id") String id) {
        boardService.deleteBoard(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Create a Task in a Board")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created  successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"
            )})
    @RequestMapping(value = "/boards/{id}/tasks", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTaskOnBoard(@RequestBody PostTaskRequest postTaskRequest, @PathVariable("id") String boardId) {
        PostTaskResponse response = taskService.createTask(postTaskRequest, boardId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Override an existing Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task overriden successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"
            )})
    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity putTask(@RequestBody PostTaskRequest postTaskRequest, @PathVariable("id") String taskId) {
        PostTaskResponse response = taskService.putTask(postTaskRequest, taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @Operation(summary = "Update an existing Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request"
            )})
    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTask(@RequestBody PostTaskRequest postTaskRequest, @PathVariable("id") String taskId) {
        taskService.updateTask(postTaskRequest, taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete a task by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetBoardResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Task not found with given id",
                    content = @Content)})
    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTask(@PathVariable("id") String id) {
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete a task that belongs to a deleted user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GetBoardResponse.class))})})
    @RequestMapping(value = "/webhooks/user-deleted", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTask(@RequestBody DeleteOrphanedTaskRequest deleteOrphanedTaskRequest) {
        taskService.deleteOrphanedTaskRequest(deleteOrphanedTaskRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
