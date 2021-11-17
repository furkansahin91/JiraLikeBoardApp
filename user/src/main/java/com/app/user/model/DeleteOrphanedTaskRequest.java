package com.app.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time",
        "data"
})
@Getter
@Setter
public class DeleteOrphanedTaskRequest {

    @JsonProperty("time")
    private String time;
    @JsonProperty("data")
    private DeletedUserData data;


}
