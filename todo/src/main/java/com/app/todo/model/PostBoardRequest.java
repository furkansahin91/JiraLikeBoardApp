package com.app.todo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "description"
})
@Getter
@Setter
public class PostBoardRequest {

    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;

}
