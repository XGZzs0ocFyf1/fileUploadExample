package a.v.g.wordApp.service.business.admin;


import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AdminService {




    public String generateWords(){
            return "";
    }


    @Async
    public CompletableFuture<String> longRunningTask() {
        // Симуляция долгой операции (например, выполнение задачи, которая занимает 10 секунд)
        try {
            System.out.println("поехали");
            Thread.sleep(10000); // 10 секунд
            System.out.println("доехали");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("Task completed");
    }

}
