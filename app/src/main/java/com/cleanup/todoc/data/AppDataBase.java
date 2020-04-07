package com.cleanup.todoc.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.Date;

/**
 * Représente la base de données.
 */
@Database(entities = {Task.class, Project.class}, version = 2, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static volatile AppDataBase INSTANCE;

    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();

    public static AppDataBase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (AppDataBase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "AppDataBase.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback(){
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                new PopulateDBAsyncTask(INSTANCE).execute();
            }
        };
    }

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProjectDao projectDao;
        private TaskDao taskDao;

        private PopulateDBAsyncTask(AppDataBase db) {
            projectDao = db.projectDao();
            taskDao = db.taskDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 3; i++)
                projectDao.insertProject(Project.getAllProjects()[i]);

            taskDao.insert(new Task(0,1L,"Nettoyer les vitres",new Date().getTime()));
            taskDao.insert(new Task(0,2L,"Vider le lave vaiselle",new Date().getTime()));
            taskDao.insert(new Task(0,2L,"Passer l'aspirateur",new Date().getTime()));
            taskDao.insert(new Task(0,1L,"Arroser les plantes",new Date().getTime()));
            taskDao.insert(new Task(0,3L,"Nettoyer les toilettes",new Date().getTime()));
            return null;
        }
    }
}