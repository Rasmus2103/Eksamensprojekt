package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.model.Board;
import com.example.eksamensprojekt.model.Story;
import com.example.eksamensprojekt.model.Task;
import com.example.eksamensprojekt.model.User;
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
    model.addAttribute("projectId", projectId);

    Object userid = session.getAttribute("userid");
    model.addAttribute("userid", userid);

    return isLogged(session) ? "storylist" : "index";
}

    @GetMapping("moveStory/{storyId}")
    public String moveStoryToSprintBoard(@PathVariable("storyId") int storyId, @RequestParam("projectId") int projectId, @RequestParam("boardId") int boardId) {
        // First, find the current board and project associated with the story.
        Story story = storyRepository.getSpecificStory(storyId);
        Board currentBoard = boardRepository.getSpecificBoard(story.getBoardid());

        System.out.println("storyId: " + storyId);
        System.out.println("currentBoard: " + currentBoard);
        System.out.println("projectId: " + projectId);

        // Now, find the sprint board id for the corresponding project.
        int sprintBoardId = boardRepository.getBoardIdByProjectId(projectId);

        System.out.println("sprintBoardId: " + sprintBoardId);

        // Move the story to the sprint board.
        storyRepository.moveStoryToBoard(storyId, sprintBoardId);

        // Redirect back to the story list for the original board.
        return "redirect:/storylist/" + currentBoard.getBoardid();
    }


    @GetMapping("story/createstory/{boardid}")
    public String addStory(@PathVariable("boardid") int boardid, Model model, HttpSession session) {
        Story story = new Story();
        model.addAttribute("story", story);

        Board board = boardRepository.getSpecificBoard(boardid);
        model.addAttribute("board", board);

        return isLogged(session) ? "createstory" : "index";
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

        List<Task> tasks = taskRepository.getTasks(storyid);
        model.addAttribute("tasks", tasks);

        int totalStoryPoints = storyRepository.getSumOfStoryPoints(storyid);
        model.addAttribute("totalStoryPoints", totalStoryPoints);

        List<String> userNames = storyRepository.getUserNamesByStoryId(storyid);
        model.addAttribute("userNames", userNames);

        Object userid = session.getAttribute("userid");
        model.addAttribute("userid", userid);

        return isLogged(session) ? "story" : "index";
    }

    @GetMapping("story/update/{storyid}")
    public String updateStory(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Story story = storyRepository.getSpecificStory(storyid);
        model.addAttribute("story", story);
        return isLogged(session) ? "updatestory" : "index";
    }

    @PostMapping("story/update/{storyid}/{boardid}")
    public String updateStory(@ModelAttribute("story") Story story, @PathVariable("storyid") int storyid, @PathVariable("boardid") int boardid) {
        storyRepository.updateStoryName(storyid, story.getStoryname());
        storyRepository.updateStoryDescription(storyid, story.getStorydescription());
        storyRepository.updateStoryAcceptcriteria(storyid, story.getAcceptcriteria());
        storyRepository.updateStoryDeadline(storyid, story.getStorydeadline());
        return "redirect:/storylist/" + boardid;
    }

    @GetMapping("story/{storyid}/addstoryuser")
    public String addUserToStory(@PathVariable("storyid") int storyid, Model model, HttpSession session) {
        Story story = storyRepository.getSpecificStory(storyid);
        List<User> users = userRepository.getAllUsers();

        model.addAttribute("story", story);
        model.addAttribute("users", users);

        return isLogged(session) ? "addstoryuser" : "index";
    }

    @PostMapping("story/{storyid}/addstoryuser")
    public String addUserToStory(@PathVariable("storyid") int storyid, @RequestParam("userIds") List<Integer> userIds) {
        for(int userid: userIds) {
            storyRepository.addUserToStory(storyid, userid);
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

}
