package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryDBTest {

    //IRepositoryDB repositoryDBTest = new RepositoryDBStub();

    @BeforeEach
    void setUp() {
    }

    @Test
    void usernameExistsTrue() {
        // boolean result = repositoryDBTest.usernameExists("Test");
        //  assertEquals(true, result);
    }

    @Test
    void usernameExistsFalse() {
        // boolean result = repositoryDBTest.usernameExists("John Doe");
        //assertEquals(false, result);
    }

    @Test
    void deleteUser() {
        User testUser1 = new User(3, "test3", "test3", "test3");

        // repositoryDBTest.registerUser(testUser1);
        // repositoryDBTest.deleteUser(3);

        // assertNull(repositoryDBTest.getUser(3));
    }

    @Test
    void deleteMultipleUsers() {
        User testUser1 = new User(3, "test3", "test3", "test3");
        User testUser2 = new User(3, "test4", "test4", "test4");
        User testUser3 = new User(3, "test5", "test5", "test5");

        //repositoryDBTest.registerUser(testUser1);
        //repositoryDBTest.registerUser(testUser2);;
        //repositoryDBTest.deleteUser(3);
        //repositoryDBTest.registerUser(testUser3);
        //repositoryDBTest.deleteUser(5);


        //assertNull(repositoryDBTest.getUser(3));
        //assertNotNull(repositoryDBTest.getUser(4));
        //assertNull(repositoryDBTest.getUser(5));
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