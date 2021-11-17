package com.app.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
public class User {

    @Id
    private String id;
}
