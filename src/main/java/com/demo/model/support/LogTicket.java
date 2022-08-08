package com.demo.model.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogTicket {

    @NotNull
    private String loginId;

    private String category;

    @NotNull
    private String description;
}
