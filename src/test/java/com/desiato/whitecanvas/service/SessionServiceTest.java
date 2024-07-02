package com.desiato.whitecanvas.service;

import com.desiato.whitecanvas.BaseTest;
import com.desiato.whitecanvas.dto.AuthenticatedUser;
import com.desiato.whitecanvas.dto.UserToken;
import com.desiato.whitecanvas.model.Session;
import com.desiato.whitecanvas.model.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SessionServiceTest extends BaseTest {

    @Test
    void createSession_ShouldReturnCreatedSession() throws Exception {

        User existingUser = testAuthenticationHelper.createAndPersistUser();
        Session createdSession = sessionService.createSession(existingUser);

        assertNotNull(createdSession, "Session is not be null");
        assertNotNull(createdSession.getId(), "Session ID should be set after saving");
    }

    @Test
    void findUserByToken_ShouldReturnUser() throws Exception {
        // Arrange
        AuthenticatedUser existingUser = testAuthenticationHelper.createAndAuthenticateUser();
        UserToken userToken = existingUser.userToken();
        Session session = new Session(userToken.value(), existingUser.user().getId());

        // Save session to database
        sessionRepository.save(session);

        // Act
        Optional<User> retrievedUser = sessionService.findUserByToken(userToken);

        // Assert
        assertTrue(retrievedUser.isPresent(), "User should be present");
        assertEquals(existingUser.user(), retrievedUser.get(), "Retrieved user should match the existing user");
    }
}
