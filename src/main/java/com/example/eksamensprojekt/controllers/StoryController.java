package com.example.eksamensprojekt.controllers;

import com.example.eksamensprojekt.model.*;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class StoryController extends PMController {

    public StoryController(ApplicationContext context, @Value("story_DB") String impl) {
    }

    @GetMapping("storylist/{boardid}")
    public String getStories(@PathVariable("boardid") int boardid, Model model, HttpSession session) {
        List<Story> stories = storyRepository.getStories(boardid);
        List<Story> todoStories = storyRepository.getStoriesSprintboard(boardid, 0);
        List<Story> doingStories = storyRepository.getStoriesSprintboard(boardid, 1);
        List<Story> doneStories = storyRepository.getStoriesSprintboard(boardid, 2);
        model.addAttribute("stories", stories);
        model.addAttribute("todoStories", todoStories);
        model.addAttribute("doingStories", doingStories);
        model.addAttribute("doneStories", doneStories);
        model.addAttribute("boardid", boardid);

        Board board = boardRepository.getSpecificBoard(boardid);
        int projectId = board.getProjectid();
        Project project = projectRepository.getSpecificProject(projectId);
        String projectname = project.getProjectname();
        session.getAttribute("userid");
        model.addAttribute("userid");
        model.addAttribute("projectname", projectname);
        model.addAttribute("projectId", projectId);
        model.addAttribute("board", board);
        model.addAttribute("boardid", boardid);


        List<Board> boards = boardRepository.getBoards(projectId);
        model.addAttribute("boards", boards);

        Board boardname = boardRepository.getSpecificBoard(boardid);
        model.addAttribute("boardName", boardname.getBoardname());

        Object userid = session.getAttribute("userid");
        model.addAttribute("userid", userid);

        Story story = new Story();
        model.addAttribute("story", story);

        return isLogged(session) ? "storylist" : "index";
    }

    @GetMapping("sprintboardMoveRight/{storyid}/{sprintboardid}")
    public String sprintboardMoveRight(@PathVariable("storyid") int storyId, @PathVariable("sprintboardid") int sprintboardid, @RequestParam("boardid") int boardId) {
        storyRepository.updateStorySprintboardid(storyId, sprintboardid + 1);
        Board currentBoard = boardRepository.getSpecificBoard(boardId);
        return "redirect:/storylist/" + currentBoard.getBoardid();
    }

    @GetMapping("sprintboardMoveLeft/{storyid}/{sprintboardid}")
    public String sprintboardMoveLeft(@PathVariable("storyid") int storyId, @PathVariable("sprintboardid") int sprintboardid, @RequestParam("boardid") int boardId) {
        storyRepository.updateStorySprintboardid(storyId, sprintboardid - 1);
        Board currentBoard = boardRepository.getSpecificBoard(boardId);
        return "redirect:/storylist/" + currentBoard.getBoardid();
    }

    @GetMapping("moveStory/{storyId}")
    public String moveStoryToSprintBoard(@PathVariable("storyId") int storyId, @RequestParam("projectId") int projectId, @RequestParam("boardId") int boardId) {
        Story story = storyRepository.getSpecificStory(storyId);
        Board currentBoard = boardRepository.getSpecificBoard(boardId);

        int sprintBoardId = boardRepository.getBoardIdByProjectId(projectId);

        storyRepository.moveStoryToBoard(storyId, sprintBoardId);

        return "redirect:/storylist/" + currentBoard.getBoardid();
    }

    @GetMapping("moveStoryBack/{storyId}")
    public String moveStoryToBacklog(@PathVariable("storyId") int storyId) {
        Story story = storyRepository.getSpecificStory(storyId);
        Board currentBoard = boardRepository.getSpecificBoard(story.getBoardid());
        int projectId = currentBoard.getProjectid();

        int backlogBoardId = boardRepository.getBacklogBoardIdByProjectId(projectId);

        storyRepository.moveStoryToBoard(storyId, backlogBoardId);

        return "redirect:/storylist/" + currentBoard.getBoardid();
    }

    @GetMapping("moveStoryBackToSprintBoard/{storyId}")
    public String moveHistoryStoryToSprintBoard(@PathVariable("storyId") int storyId) {
        Story story = storyRepository.getSpecificStory(storyId);
        Board currentBoard = boardRepository.getSpecificBoard(story.getBoardid());
        int projectId = currentBoard.getProjectid();

        int sprintboardBoardId = boardRepository.getBoardIdByProjectId(projectId);

        storyRepository.moveStoryToBoard(storyId, sprintboardBoardId);

        return "redirect:/storylist/" + currentBoard.getBoardid();
    }

    @PostMapping("story/createstory/{boardid}")
    public String addStory(@ModelAttribute("story") Story story, @PathVariable("boardid") int boardid, HttpSession session) {
        int projectId = boardRepository.getProjectIdByBoardId(boardid);
        Project project = projectRepository.getSpecificProject(projectId);
        if (project.getProjectdeadline().before(story.getStorydeadline())) {
            session.setAttribute("errorMessage", "The story deadline cannot be after the project deadline.");
        } else {
            storyRepository.addStory(boardid, story);
        }
        return "redirect:/storylist/{boardid}";
    }

    @GetMapping("story/slet/{boardid}/{storyid}")
    public String deleteStory(@PathVariable("boardid") int boardid, @PathVariable("storyid") int storyid, Model model) {
        storyRepository.deleteStory(storyid);

        List<Story> stories = storyRepository.getStories(boardid);
        model.addAttribute("story", stories);
        return "redirect:/storylist/" + boardid;
    }

    @GetMapping("story/{storyid}")
    public String getStory(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Story story = storyRepository.getSpecificStory(storyid);
        model.addAttribute("story", story);

        int boardID = story.getBoardid();
        Board board = boardRepository.getSpecificBoard(boardID);
        model.addAttribute("board", board);
        int projectID = board.getProjectid();
        model.addAttribute("projectId", projectID);
        List<Board> boards = boardRepository.getBoards(projectID);
        model.addAttribute("boards", boards);
        Project project = projectRepository.getSpecificProject(projectID);
        String projectname = project.getProjectname();
        model.addAttribute("projectname", projectname);

        List<Task> tasks = taskRepository.getTasks(storyid);
        model.addAttribute("tasks", tasks);

        int totalStoryPoints = storyRepository.getSumOfStoryPoints(storyid);
        model.addAttribute("totalStoryPoints", totalStoryPoints);

        List<User> storyuser = storyRepository.getUserByStoryId(storyid);
        model.addAttribute("storyuser", storyuser);

        Object userid = session.getAttribute("userid");
        model.addAttribute("userid", userid);

        Task task = new Task();
        model.addAttribute("task", task);

        Task task1 = taskRepository.getSpecificTask(storyid);
        model.addAttribute("task1", task1);

        List<User> usersFromProject = userRepository.getAllUsersFromProject(projectID);
        model.addAttribute("usersFromProject", usersFromProject);

        return isLogged(session) ? "story" : "index";
    }

    @PostMapping("story/update/{storyid}/{boardid}")
    public String updateStory(@ModelAttribute("story") Story story, @PathVariable("storyid") int storyid, @PathVariable("boardid") int boardid, Model model) {
        int projectId = boardRepository.getProjectIdByBoardId(boardid);
        Project project = projectRepository.getSpecificProject(projectId);
        if (project.getProjectdeadline().before(story.getStorydeadline())) {
            model.addAttribute("errorMessage", "The story deadline cannot be after the project deadline.");
            model.addAttribute("story", story);
            Board board = boardRepository.getSpecificBoard(boardid);
            model.addAttribute("board", board);
            model.addAttribute("projectid", projectId);
            List<Board> boards = boardRepository.getBoards(projectId);
            model.addAttribute("boards", boards);
            return "redirect:/story/" + storyid;
        } else {
            storyRepository.updateStory(storyid, story);
            return "redirect:/story/" + storyid;
        }
    }

    @PostMapping("story/{storyid}/addstoryuser")
    public String addUserToStory(@PathVariable("storyid") int storyid, @RequestParam("userIds") List<Integer> userIds, Model model) {
        if (userIds != null) {
            for (int userid : userIds) {
                try {
                    storyRepository.addUserToStory(storyid, userid);
                } catch (RuntimeException e) {
                    String errorMessage = "Failed to add user to story: " + e.getMessage();
                    model.addAttribute("errorMessage", errorMessage);
                    List<User> users = userRepository.getAllUsers();
                    model.addAttribute("users", users);
                }
            }
        }
        return "redirect:/story/" + storyid;
    }

    @PostMapping("story/{storyid}/removeuserfromstory")
    public String RemoveUserFromStory(@PathVariable("storyid") int storyid, @RequestParam("userIds") List<Integer> userIds, Model model) {
        if (userIds != null) {
            for (int userid : userIds) {
                try {
                    storyRepository.removeUserFromStory(storyid, userid);
                } catch (RuntimeException e) {
                    String errorMessage = "Failed to remove user to story: " + e.getMessage();
                    model.addAttribute("errorMessage", errorMessage);
                    List<User> users = userRepository.getAllUsers();
                    model.addAttribute("users", users);
                }
            }
        }
        return "redirect:/story/" + storyid;
    }


    @PostMapping("story/{storyid}")
    public String processForm(@PathVariable("storyid") int storyid, @RequestParam Map<String, Boolean> tasks, Model model) {
        Story story = storyRepository.getSpecificStory(storyid);
        model.addAttribute("story", story);

        List<Task> taskslist = taskRepository.getTasks(storyid);
        model.addAttribute("tasks", taskslist);

        model.addAttribute("tasks", tasks);
        return "story";
    }

    @PostMapping("markStoryAsFinished/{boardid}/{storyId}")
    public String markStoryAsFinished(@PathVariable("boardid") int boardid, @PathVariable("storyId") int storyId) {
        storyRepository.markStoryAsFinished(storyId);

        boolean allTasksFinished = taskRepository.areAllTasksFinished(storyId);

        if (allTasksFinished) {
            Story story = storyRepository.getSpecificStory(storyId);
            Board currentBoard = boardRepository.getSpecificBoard(story.getBoardid());
            int projectId = currentBoard.getProjectid();
            int historyBoardId = boardRepository.getHistoryBoardIdByProjectId(projectId);

            storyRepository.moveStoryToBoard(storyId, historyBoardId);
        }

        return "redirect:/storylist/" + boardid;
    }

}
