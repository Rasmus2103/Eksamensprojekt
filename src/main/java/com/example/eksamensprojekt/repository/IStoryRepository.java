package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.Story;

import java.sql.Date;
import java.util.List;

public interface IStoryRepository {
    List<Story> getStories(int boardid);

    List<Story> getStoriesSprintboard(int boardid, int sprintboardid);

    Story getSpecificStory(int storyid);

    void addStory(int boardid, Story story);

    void deleteStory(int storyid);

    void updateStory(int storyid, Story story);

    void updateStorySprintboardid(int storyid, int sprintboardid);

    List<String> getUserNamesByStoryId(int storyid);

    void addUserToStory(int storyid, int userid);

    void removeUserFromStory(int storyid, int userid);

    void moveStoryToBoard(int storyid, int boardid);

    void markStoryAsFinished(int storyId);

    List<Integer> getAllStoryPoints(int storyid);

    int getSumOfStoryPoints(int storyid);
}
