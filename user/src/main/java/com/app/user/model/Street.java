package com.app.user.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "number",
        "name"
})
@Data
public class Street {

    @JsonProperty("number")
    private Integer number;
    @JsonProperty("name")
    private String name;
}
