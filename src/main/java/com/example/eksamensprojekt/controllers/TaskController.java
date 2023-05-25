package com.example.eksamensprojekt.controllers;
import com.example.eksamensprojekt.model.Board;
import com.example.eksamensprojekt.model.Project;
import com.example.eksamensprojekt.model.Story;
import com.example.eksamensprojekt.model.Task;
import com.example.eksamensprojekt.repository.ITaskRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class TaskController extends PMController {

    public TaskController(ApplicationContext context, @Value("task_DB") String impl) {
    }

    @PostMapping("story/createtask/{storyid}")
    public String addTask(@ModelAttribute("task") Task task, @PathVariable("storyid") int storyid) {
        taskRepository.addTask(storyid, task);
        return "redirect:/story/{storyid}";
    }

    @GetMapping("task/update/{taskid}/{projectid}")
    public String updateTask(@PathVariable("taskid") int taskid,@PathVariable("projectid") int projectid ,Model model, HttpSession session) {
        Task task = taskRepository.getSpecificTask(taskid);
        model.addAttribute("task", task);
        session.getAttribute("userid");
        model.addAttribute("userid", session.getAttribute("userid"));
        Project project = projectRepository.getSpecificProject(projectid);

        model.addAttribute("project", project);

        List<Board> boards = boardRepository.getBoards(projectid);
        model.addAttribute("boards", boards);
        return isLogged(session) ? "updatetask" : "index";
    }

    @PostMapping("task/update/{taskid}/{storyid}")
    public String updateTask(@ModelAttribute("task") Task task, @PathVariable("taskid") int taskid, @PathVariable("storyid") int storyid) {
        taskRepository.updateTaskName(taskid, task.getTaskname());
        taskRepository.updateTaskDescription(taskid, task.getTaskdescription());
        taskRepository.updateTaskStorypoints(taskid, task.getStorypoints());

        return "redirect:/story/" + storyid;
    }

    @GetMapping("storyid/slet/{storyid}/{taskid}")
    public String deleteTask(@PathVariable("storyid") int storyid, @PathVariable("taskid") int taskid, Model model) {
        taskRepository.deleteTask(taskid);

        Story story = storyRepository.getSpecificStory(storyid);
        model.addAttribute("story", story);
        return "redirect:/story/" + storyid;
    }

    @PostMapping("toggleTask/{storyid}/{taskId}")
    public String toggleTask(@PathVariable("taskId") int taskId, @PathVariable("storyid") int storyid, @RequestParam(value = "finished", required = false) Boolean finished) {
        if (finished == null) {
            finished = false;
        }
        taskRepository.updateTaskFinished(taskId, finished);
        return "redirect:/story/" + storyid;
    }





}