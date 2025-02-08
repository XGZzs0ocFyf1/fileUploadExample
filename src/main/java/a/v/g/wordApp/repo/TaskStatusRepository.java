package a.v.g.wordApp.repo;


import a.v.g.wordApp.model.tasks.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

    TaskStatus findByTaskId(Long taskId);

}