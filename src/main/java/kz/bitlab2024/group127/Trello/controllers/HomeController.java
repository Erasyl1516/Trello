package kz.bitlab2024.group127.Trello.controllers;

import jdk.jfr.Category;
import kz.bitlab2024.group127.Trello.entites.Folders;
import kz.bitlab2024.group127.Trello.entites.TaskCategories;
import kz.bitlab2024.group127.Trello.entites.Tasks;
import kz.bitlab2024.group127.Trello.repozitories.CategoriesRepozitori;
import kz.bitlab2024.group127.Trello.repozitories.FoldersRepozitori;
import kz.bitlab2024.group127.Trello.repozitories.TasksRepozitori;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Data
@AllArgsConstructor
@NoArgsConstructor


public class HomeController {
    @Autowired
    private FoldersRepozitori foldersRepozitori;
    @Autowired
    private TasksRepozitori tasksRepozitori;
   @Autowired
   private CategoriesRepozitori categoriesRepozitori;
    @GetMapping(value = "/allFolders")
    public String GetHome(Model model){
        model.addAttribute("folders", foldersRepozitori.findAll());
        return "home";
    }
    @GetMapping(value = "/details/{id}")
    private String GetDetails(@PathVariable(name = "id")Long id, Model model){
        Folders folder = foldersRepozitori.findById(id).orElseThrow();
        model.addAttribute("folder", folder);
        List<Tasks> tasks = tasksRepozitori.findAllByFolder(folder);
        model.addAttribute("tasks", tasks);
        List<TaskCategories>taskCategories = folder.getCategories();
        model.addAttribute("taskCategories", taskCategories);
        return "details";
    }
    @PostMapping(value = "/addTask")
    private String AddTask(@RequestParam(name="task_name")String task_name,
                           @RequestParam(name="task_description")String task_description,
                           @RequestParam(name="folder_id") Long folder_id){
        Tasks task = new Tasks();
        task.setTitle(task_name);
        task.setDescription(task_description);
        task.setStatus(0);
        Folders folder = foldersRepozitori.findById(folder_id).orElseThrow();
        task.setFolder(folder);
        tasksRepozitori.save(task);
        return "redirect:/details/"+folder.getId();
    }
    @GetMapping(value = "/detailTask/{taskId}")
    private String GetDetailsTask(@PathVariable(name = "taskId")Long taskId, Model model){
      Tasks task = tasksRepozitori.findById(taskId).orElseThrow();
      model.addAttribute("task", task);
        return "Taskdetails";
    }
    @PostMapping(value = "/addCategory")
    private String AddCategory(@RequestParam(name = "category_name")String category_name
                               ){
        TaskCategories categories = new TaskCategories();
        categories.setName(category_name);
        categoriesRepozitori.save(categories);
        return "redirect:/categories";
    }
    @GetMapping(value = "/categories")
    public String GetCategories(Model model){
        model.addAttribute("categories", categoriesRepozitori.findAll());
        return "categories";
    }
    @PostMapping(value = "/taskDelete")
    private String DeleteTask(@RequestParam(name = "id")Long id){
       Tasks task = tasksRepozitori.findById(id).orElseThrow();
       tasksRepozitori.delete(task);
        return "redirect:/allFolders";
    }
    @GetMapping(value = "/categorydetails/{id}")
    public String GetCategoryDetails(@PathVariable(name = "id")long id, Model model){
     TaskCategories categories = categoriesRepozitori.findById(id).orElseThrow();
     model.addAttribute("categories", categories);
        return "CategoryDetails";
    }
    @PostMapping(value = "/addFolder")
    private String AddFolder(@RequestParam(name="folder_name")String folder_name,
                             @RequestParam(name = "category_id")Long category_id){
      TaskCategories categories = categoriesRepozitori.findById(category_id).orElseThrow();
      Folders folder = new Folders();
      folder.setName(folder_name);
      folder.getCategories() .add(categories);
      categories.getFolders().add(folder);
      foldersRepozitori.save(folder);
      categoriesRepozitori.save(categories);
        return "redirect:/categorydetails/" + categories.getId();
    }
    @PostMapping(value = "/FolderDelete")
    private String DeleteFolder(@RequestParam(name = "id")Long id,
                                @RequestParam(name ="category_id")Long category_id){
        Folders folder = foldersRepozitori.findById(id).orElseThrow();
        for (TaskCategories c: folder.getCategories()){
            c.getFolders().remove(folder);
        }
        folder.getCategories().clear();
        categoriesRepozitori.saveAll(categoriesRepozitori.findAll());
        tasksRepozitori.deleteAll(folder.getTasks());
        foldersRepozitori.delete(folder);
        return "redirect:/categorydetails/" + category_id;
    }
    @PostMapping (value = "/editTask")
    private String editTask(@RequestParam(name = "id_task")Long id_task,
                            @RequestParam(name = "task_name")String task_name,
                            @RequestParam(name = "task_description")String task_description, Model model){
        Tasks task = tasksRepozitori.findById(id_task).orElseThrow();
        task.setTitle(task_name);
        task.setDescription(task_description);
        tasksRepozitori.save(task);
        model.addAttribute("task", task);
        return "redirect:/detailTask/"+task.getId();
    }
    @PostMapping(value = "/editCategory")
    private String editCategory(@RequestParam(name = "id_category")Long id_category,
                                @RequestParam(name = "category_name")String category_name,Model model){
      TaskCategories taskCategories = categoriesRepozitori.findById(id_category).orElseThrow();
      taskCategories.setName(category_name);
      categoriesRepozitori.save(taskCategories);
      model.addAttribute("taskCategories", taskCategories);
      return "redirect:/categorydetails/" + id_category;
    }
    @PostMapping(value = "/editFolder")
    private String editFolder(@RequestParam(name = "id_folder")Long id_folder,
                              @RequestParam(name = "folder_name")String folder_name,Model model){
         Folders folders = foldersRepozitori.findById(id_folder).orElseThrow();
         folders.setName(folder_name);
         foldersRepozitori.save(folders);
         model.addAttribute("folder", folders);
        return "redirect:/details/" + id_folder;
    }
    @PostMapping(value = "/CategoryDelete")
    private String DeleteFolder(@RequestParam(name = "id_delete")Long id_delete){
       TaskCategories categories = categoriesRepozitori.findById(id_delete).orElseThrow();
       List<Folders> folders = foldersRepozitori.findAllByCategoriesContains(categories);
       for (Folders f: folders){
         tasksRepozitori.deleteAllByFolder(f);
       }
       foldersRepozitori.deleteAllByCategories(categories);
       categoriesRepozitori.delete(categories);
        return "redirect:/categories";
    }
    @PostMapping("/taskStatus")
    private String changeStatus(
                   @RequestParam(name="status_id") Long status_id,
                   @RequestParam(name = "status_name") int status_name){
        Tasks task = tasksRepozitori.findById(status_id).orElseThrow();
        task.setStatus(status_name);
        tasksRepozitori.save(task);
        return "redirect:/detailTask/"+task.getId();
    }

}
