package com.desiato.whitecanvas.dto;

import com.desiato.whitecanvas.model.User;

public record AuthenticatedUser(User user, UserToken userToken) {}

