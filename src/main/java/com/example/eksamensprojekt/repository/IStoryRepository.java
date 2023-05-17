package com.example.eksamensprojekt.repository;

import com.example.eksamensprojekt.model.Story;

import java.sql.Date;
import java.util.List;

public interface IStoryRepository {
    List<Story> getStories(int boardid);
    Story getSpecificStory(int storyid);
    void addStory(int boardid, Story story);
    void deleteStory(int storyid);
    void updateStoryName(int storyid, String storyname);
    void updateStoryDescription(int storyid, String storydescription);
    void updateStoryAcceptcriteria(int storyid, String storyacceptcriteria);
    void updateStoryDeadline(int storyid, Date storydeadline);
    List<String> getUserNamesByStoryId(int storyid);
    void addUserToStory(int storyid, int userid);
    void moveStoryToBoard(int storyid, int boardid);
    void markStoryAsFinished(int storyId);
    List<Integer>getAllStoryPoints(int storyid);
    int getSumOfStoryPoints(int storyid);
}
