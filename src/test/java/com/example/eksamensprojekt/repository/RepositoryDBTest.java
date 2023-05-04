package com.example.eksamensprojekt.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
class RepositoryDBTest {

    RepositoryDB testRepo = new RepositoryDB();

    @BeforeEach
    void setUp() {
    }

    @Test
    void usernameExists(){
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