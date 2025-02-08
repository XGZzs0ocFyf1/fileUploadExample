package a.v.g.wordApp.service.tasks;

import a.v.g.wordApp.model.tasks.TaskStatus;
import a.v.g.wordApp.repo.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    // Метод для начала долгой задачи
    @Async
    public CompletableFuture<Void> longRunningTask(Long taskId) {
        // Создание записи о задаче
        TaskStatus taskStatus = new TaskStatus();
//        taskStatus.setTaskId(taskId);
        taskStatus.setStatus("In Progress");
        taskStatus.setMessage("Task is being processed");
        taskStatusRepository.save(taskStatus);
        System.out.println("saved");
        // Симуляция долгой операции (например, 10 секунд)
        try {
            Thread.sleep(10000); // 10 секунд
        } catch (InterruptedException e) {
            taskStatus.setStatus("Failed");
            taskStatus.setMessage("Task was interrupted");
            taskStatusRepository.save(taskStatus);
            return CompletableFuture.failedFuture(e);
        }

        // Обновляем статус задачи после завершения
        taskStatus.setStatus("Completed");
        taskStatus.setMessage("Task is completed");
        taskStatusRepository.save(taskStatus);

        return CompletableFuture.completedFuture(null);
    }

    // Метод для получения статуса задачи
    public TaskStatus getTaskStatus(Long taskId) {
        return taskStatusRepository.findByTaskId(taskId);
    }
}
