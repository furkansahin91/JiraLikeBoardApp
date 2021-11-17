package com.app.todo.repository;

import com.app.todo.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository  extends CrudRepository<Task, Long>{
    Task findByUuid(String uuid);

    Task findByUserid(String user);

    void deleteByUserid(String userId);
}
