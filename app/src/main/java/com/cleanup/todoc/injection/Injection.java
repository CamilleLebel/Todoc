package com.cleanup.todoc.injection;

import android.content.Context;

import com.cleanup.todoc.data.AppDataBase;
import com.cleanup.todoc.repositories.ProjectRepository;
import com.cleanup.todoc.repositories.TaskRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Classe d'injection des instances souhaitées.
 */
public class Injection {

    private static ProjectRepository provideProjectRepository(Context context){
        AppDataBase database = AppDataBase.getInstance(context);
        return new ProjectRepository(database.projectDao());
    }

    private static TaskRepository provideTaskRepository(Context context) {
        AppDataBase database = AppDataBase.getInstance(context);
        return new TaskRepository(database.taskDao());
    }

    private static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        ProjectRepository projectRepository = provideProjectRepository(context);
        TaskRepository taskRepository = provideTaskRepository(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(projectRepository, taskRepository, executor);
    }
}
