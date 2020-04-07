package com.cleanup.todoc.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectRepository;
import com.cleanup.todoc.repositories.TaskRepository;

import java.util.List;
import java.util.concurrent.Executor;


public class MainActivityViewModel extends ViewModel {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final Executor executor;

    public MainActivityViewModel(ProjectRepository projectRepository,
                                 TaskRepository taskRepository, Executor executor) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.executor = executor;
    }

    // -------------
    // FOR PROJECT
    // -------------

    public LiveData<List<Project>> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    // -------------
    // FOR TASK
    // -------------

    public LiveData<List<Task>> getTasks() {
        return taskRepository.getTasks();
    }

    public void createTask(final Task task) {
        executor.execute(() -> taskRepository.insertTask(task));
    }

    public void deleteTask(Task task) {
        executor.execute(() -> taskRepository.deleteTask(task));
    }
}
