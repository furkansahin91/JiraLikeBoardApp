package com.app.user.model;

import com.app.user.entity.Board;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "board"

})
@Getter
@Setter
public class GetBoardResponse {

    @JsonProperty("boards")
    private Board board;

}
