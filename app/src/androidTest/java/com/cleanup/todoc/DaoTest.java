package com.cleanup.todoc;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.data.AppDataBase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DaoTest {
    private AppDataBase database;

    private static Task TASK_1 = new Task(1, 1,"task 1", new Date().getTime());
    private static Task TASK_2 = new Task(2, 2,"task 2", new Date().getTime());

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                AppDataBase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }

    // -------------
    // FOR PROJECT DAO
    // -------------

    @Test
    public void insertAndGetProjects() throws InterruptedException {
        // BEFORE : Adding projects
        List<Project> items = LiveDataTestUtil.getValue(database.projectDao().getAllProjects());
        assertEquals(0, items.size());

        this.database.projectDao().insertProject(Project.getAllProjects()[0]);
        this.database.projectDao().insertProject(Project.getAllProjects()[1]);
        this.database.projectDao().insertProject(Project.getAllProjects()[2]);

        // TEST
        items = LiveDataTestUtil.getValue(database.projectDao().getAllProjects());
        assertEquals(3, items.size());
    }

    // -------------
    // FOR TASK DAO
    // -------------

    @Test
    public void insertAndGetTask() throws InterruptedException {
        // Add projects in database
        for (Project project : Project.getAllProjects())
            database.projectDao().insertProject(project);

        database.taskDao().insert(TASK_1);

        Task task1 = LiveDataTestUtil.getValue(database.taskDao().getTasks()).get(0);

        // TEST
        assertTrue(task1.getName().equals(TASK_1.getName())&& task1.getId() == TASK_1.getId());
    }

    @Test
    public void deleteTask() throws InterruptedException {
        // BEFORE : Adding a projects and tasks;
        for (Project project : Project.getAllProjects())
            database.projectDao().insertProject(project);

        database.taskDao().insert(TASK_1);
        database.taskDao().insert(TASK_2);

        List<Task> tasks = LiveDataTestUtil.getValue(database.taskDao().getTasks());

        // Check tasks have been added
        assertEquals(2, tasks.size());

        // THEN : deleting TASK_1
        database.taskDao().delete(tasks.get(0));

        tasks = LiveDataTestUtil.getValue(database.taskDao().getTasks());
        // TEST : task has been deleted
        assertEquals(tasks.get(0).getName(), TASK_2.getName());
        assertEquals(1, tasks.size());
    }
}
