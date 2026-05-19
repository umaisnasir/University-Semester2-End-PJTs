package com.studentrecord;

import com.studentrecord.manager.StudentManager;
import com.studentrecord.ui.MainWindow;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        StudentManager manager = new StudentManager();
        MainWindow mainWindow = new MainWindow(manager);

        Scene scene = new Scene(mainWindow.getRoot(), 900, 600);

        primaryStage.setTitle("Student Record Management System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}