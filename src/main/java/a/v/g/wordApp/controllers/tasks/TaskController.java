package a.v.g.wordApp.controllers.tasks;


import a.v.g.wordApp.model.tasks.TaskStatus;
import a.v.g.wordApp.service.tasks.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Метод для запуска долгой задачи
    @GetMapping("/dev/start-task/{taskId}")
    public String startTask(@PathVariable Long taskId) {
        taskService.longRunningTask(taskId);  // Запуск долгой задачи
        return "Task started! Check back later for result.";
    }

    // Метод для получения статуса выполнения задачи
    @GetMapping("/dev/task-result/{taskId}")
    public TaskStatus getResult(@PathVariable Long taskId) {
        return taskService.getTaskStatus(taskId);  // Возвращаем статус задачи
    }
}
