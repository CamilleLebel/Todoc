package com.cleanup.todoc.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.repositories.ProjectRepository;
import com.cleanup.todoc.repositories.TaskRepository;
import com.cleanup.todoc.viewmodels.MainActivityViewModel;

import java.util.concurrent.Executor;

/**
 * Factory permettant la fabrication d'un {@link ViewModel}
 */
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final Executor executor;

    public ViewModelFactory(ProjectRepository projectRepository,
                            TaskRepository taskRepository, Executor executor) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(projectRepository, taskRepository, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
