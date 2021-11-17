package com.app.todo.model;

import java.util.List;
import java.util.UUID;
import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Task schema
 * <p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "description",
        "user",
        "status"
})
public class Task {

    /**
     * (Required)
     */
    @JsonProperty("id")
    private UUID id;
    /**
     * (Required)
     */
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    /**
     * (Required)
     */
    @JsonProperty("user")
    private UUID user;
    @JsonProperty("status")
    private String status;

    /**
     * (Required)
     */
    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    /**
     * (Required)
     */
    @JsonProperty("id")
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * (Required)
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * (Required)
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * (Required)
     */
    @JsonProperty("user")
    public UUID getUser() {
        return user;
    }

    /**
     * (Required)
     */
    @JsonProperty("user")
    public void setUser(UUID user) {
        this.user = user;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

}