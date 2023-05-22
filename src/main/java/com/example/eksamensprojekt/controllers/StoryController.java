package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.model.*;
import com.example.eksamensprojekt.repository.IStoryRepository;
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
    private IStoryRepository storyRepository;

    public StoryController(ApplicationContext context, @Value("story_DB") String impl) {
        this.storyRepository =(IStoryRepository) context.getBean(impl);
    }

@GetMapping("storylist/{boardid}")
public String getStories(@PathVariable("boardid") int boardid, Model model, HttpSession session) {
    List<Story> stories = storyRepository.getStories(boardid);
    model.addAttribute("stories", stories);

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

    @GetMapping("moveStory/{storyId}")
    public String moveStoryToSprintBoard(@PathVariable("storyId") int storyId, @RequestParam("projectId") int projectId, @RequestParam("boardId") int boardId) {
        Story story = storyRepository.getSpecificStory(storyId);
        Board currentBoard = boardRepository.getSpecificBoard(boardId);

        /*System.out.println("storyId: " + storyId);
        System.out.println("currentBoard: " + currentBoard);
        System.out.println("projectId: " + projectId);*/

        int sprintBoardId = boardRepository.getBoardIdByProjectId(projectId);

        //System.out.println("sprintBoardId: " + sprintBoardId);

        storyRepository.moveStoryToBoard(storyId, sprintBoardId);

        // Redirect back to the story list for the original board.
        return "redirect:/storylist/" + currentBoard.getBoardid();
    }

    @GetMapping("moveStoryBack/{storyId}")
    public String moveStoryToBacklog(@PathVariable("storyId") int storyId) {
        // First, find the current board and project associated with the story.
        Story story = storyRepository.getSpecificStory(storyId);
        Board currentBoard = boardRepository.getSpecificBoard(story.getBoardid());
        int projectId = currentBoard.getProjectid();

        // Now, find the backlog board id for the corresponding project.
        int backlogBoardId = boardRepository.getBacklogBoardIdByProjectId(projectId);

        // Move the story to the backlog board.
        storyRepository.moveStoryToBoard(storyId, backlogBoardId);

        // Redirect back to the story list for the original board.
        return "redirect:/storylist/" + currentBoard.getBoardid();
    }

    @GetMapping("storylist/moveStoryBackToSprintBoard/{storyId}")
    public String moveHistoryStoryToSprintBoard(@PathVariable("storyId") int storyId) {
        // First, find the current board and project associated with the story.
        Story story = storyRepository.getSpecificStory(storyId);
        Board currentBoard = boardRepository.getSpecificBoard(story.getBoardid());
        int projectId = currentBoard.getProjectid();

        // Now, find the backlog board id for the corresponding project.
        int sprintboardBoardId = boardRepository.getBoardIdByProjectId(projectId);

        // Move the story to the backlog board.
        storyRepository.moveStoryToBoard(storyId, sprintboardBoardId);

        // Redirect back to the story list for the original board.
        return "redirect:/storylist/" + currentBoard.getBoardid();
    }

    @PostMapping("story/createstory/{boardid}")
    public String addStory(@ModelAttribute("story") Story story, @PathVariable("boardid") int boardid) {
        storyRepository.addStory(boardid, story);
        return "redirect:/storylist/{boardid}";
    }

    @GetMapping("story/slet/{boardid}/{storyid}")
    public String deleteStory(@PathVariable ("boardid") int boardid, @PathVariable("storyid") int storyid, Model model) {
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

        List<String> userNames = storyRepository.getUserNamesByStoryId(storyid);
        model.addAttribute("userNames", userNames);

        Object userid = session.getAttribute("userid");
        model.addAttribute("userid", userid);

        Task task = new Task();
        model.addAttribute("task", task);

        Task task1 = taskRepository.getSpecificTask(storyid);
        model.addAttribute("task1", task1);

        List<User> users = userRepository.getAllUsers();
        model.addAttribute("users", users);


        return isLogged(session) ? "story" : "index";
    }

    @PostMapping("story/update/{storyid}/{boardid}")
    public String updateStory(@ModelAttribute("story") Story story, @PathVariable("storyid") int storyid, @PathVariable("boardid") int boardid) {
        storyRepository.updateStoryName(storyid, story.getStoryname());
        storyRepository.updateStoryDescription(storyid, story.getStorydescription());
        storyRepository.updateStoryAcceptcriteria(storyid, story.getAcceptcriteria());
        storyRepository.updateStoryDeadline(storyid, story.getStorydeadline());
        return "redirect:/story/" + storyid;
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


    @PostMapping("story/{storyid}")
    public String processForm(@PathVariable("storyid") int storyid, @RequestParam Map<String, Boolean> tasks, Model model) {
        Story story = storyRepository.getSpecificStory(storyid);
        model.addAttribute("story", story);

        List<Task> taskslist = taskRepository.getTasks(storyid);
        model.addAttribute("tasks", taskslist);

        Board boardname = boardRepository.getSpecificBoard(storyid);
        model.addAttribute("boardName", boardname.getBoardname());

        model.addAttribute("tasks", tasks);
        return "story";
    }

    @PostMapping("markStoryAsFinished/{boardid}/{storyId}")
    public String markStoryAsFinished(@PathVariable("boardid") int boardid, @PathVariable("storyId") int storyId) {
        // First, update the story's isFinished status to true
        storyRepository.markStoryAsFinished(storyId);

        // Check if all tasks associated with the story are finished
        boolean allTasksFinished = taskRepository.areAllTasksFinished(storyId);

        if (allTasksFinished) {
            // Find the history board ID for the project
            Story story = storyRepository.getSpecificStory(storyId);
            Board currentBoard = boardRepository.getSpecificBoard(story.getBoardid());
            int projectId = currentBoard.getProjectid();
            int historyBoardId = boardRepository.getHistoryBoardIdByProjectId(projectId);

            // Move the story to the history board
            storyRepository.moveStoryToBoard(storyId, historyBoardId);
        }

        // Redirect back to the story details page
        return "redirect:/storylist/" + boardid;
    }




}
