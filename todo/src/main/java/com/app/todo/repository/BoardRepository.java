package com.app.todo.repository;

import com.app.todo.entity.Board;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends PagingAndSortingRepository<Board, Long> {
    Board findByUuid(String uuid);
}
