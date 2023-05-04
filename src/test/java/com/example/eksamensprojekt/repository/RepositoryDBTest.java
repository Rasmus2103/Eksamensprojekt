package com.example.eksamensprojekt.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepositoryDBTest {

    IRepositoryDB repositoryDBTest = new RepositoryDBStub();

    @BeforeEach
    void setUp() {
    }

    @Test
    void usernameExistsTrue() {
        boolean result = repositoryDBTest.usernameExists("Test");
        assertEquals(true, result);
    }

    @Test
    void usernameExistsFalse() {
        boolean result = repositoryDBTest.usernameExists("John Doe");
        assertEquals(false, result);
    }

    @Test
    void deleteUser() {
    }

    @Test
    void addProject() {
    }

    @Test
    void addUserToProject() {
    }

    @Test
    void addStory() {
    }

    @Test
    void updateStoryDescription() {
    }

    @Test
    void updateStoryDeadline() {
    }

    @Test
    void getSpecificTask() {
    }

    @Test
    void addTask() {
    }

    @Test
    void updateTaskStorypoints() {
    }
}