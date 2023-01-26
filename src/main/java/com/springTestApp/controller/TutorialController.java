package com.springTestApp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springTestApp.entity.Tutorial;
import com.springTestApp.repository.TutorialRepository;

@Controller
public class TutorialController {

  @Autowired
  private TutorialRepository tutorialRepository;

  @GetMapping("/tasks")
  public String getAll(Model model, @Param("keyword") String keyword) {
    try {
      List<Tutorial> tutorials = new ArrayList<Tutorial>();

      if (keyword == null) {
        tutorialRepository.findAll().forEach(tutorials::add);
      } else {
        tutorialRepository.findByTitleContainingIgnoreCase(keyword).forEach(tutorials::add);
        model.addAttribute("keyword", keyword);
      }

      model.addAttribute("tutorials", tutorials);
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
    }

    return "tasks";
  }

  @GetMapping("/tasks/new")
  public String addTutorial(Model model) {
    Tutorial tutorial = new Tutorial();

    model.addAttribute("tutorial", tutorial);
    model.addAttribute("pageTitle", "Lisa uus");

    return "task_form";
  }

  @PostMapping("/tasks/save")
  public String saveTutorial(Tutorial tutorial, RedirectAttributes redirectAttributes) {
    try {
      tutorialRepository.save(tutorial);

      redirectAttributes.addFlashAttribute("message", "Ülesanne lisatud!");
    } catch (Exception e) {
      redirectAttributes.addAttribute("message", e.getMessage());
    }

    return "redirect:/tasks";
  }

  @GetMapping("/tasks/{id}")
  public String editTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Tutorial tutorial = tutorialRepository.findById(id).get();

      model.addAttribute("tutorial", tutorial);
      model.addAttribute("pageTitle", "Muuda (ID: " + id + ")");

      return "task_form";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());

      return "redirect:/tasks";
    }
  }

  @GetMapping("/tasks/delete/{id}")
  public String deleteTutorial(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      tutorialRepository.deleteById(id);

      redirectAttributes.addFlashAttribute("message", "Ülesanne kustutatud!");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", e.getMessage());
    }

    return "redirect:/tasks";
  }


}
