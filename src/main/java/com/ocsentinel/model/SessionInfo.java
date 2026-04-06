package com.ocsentinel.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SessionInfo {
    private String jwtToken;
    private String feedToken;
    private String clientCode;
    private String name;
    private boolean loggedIn;
}
