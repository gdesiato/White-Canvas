package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.TestAuthenticationHelper;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
import com.desiato.whitecanvas.dto.UserToken;
import com.desiato.whitecanvas.model.Session;
import com.desiato.whitecanvas.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SessionServiceTest extends BaseTest {

    private  User createdAndPersistedUser;

    @BeforeEach
    void setup() throws Exception {
        createdAndPersistedUser = testAuthenticationHelper.createAndPersistUser();
    }

    @Test
    void createSession_ShouldReturnCreatedSession() {

        Session createdSession = sessionService.createSession(createdAndPersistedUser);

        assertNotNull(createdSession, "Session is not be null");
        assertNotNull(createdSession.getId(), "Session ID should be set after saving");
    }

    @Test
    void findUserByToken_ShouldReturnUser() throws Exception {
        AuthenticatedUser existingUser = testAuthenticationHelper.createAndAuthenticateUser();
        UserToken userToken = existingUser.userToken();

        Optional<User> retrievedUser = sessionService.findUserByToken(userToken);

        assertTrue(retrievedUser.isPresent(), "User should be present");
        assertEquals(existingUser.user().getId(), retrievedUser.get().getId(), "Retrieved user should match the existing user");
    }
}
