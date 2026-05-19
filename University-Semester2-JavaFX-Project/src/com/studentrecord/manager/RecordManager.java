package com.studentrecord.manager;

import com.studentrecord.model.Student;
import java.util.List;

public interface RecordManager
{
    void addStudent(Student student);

    void updateStudent(Student updatedStudent);

    void deleteStudent(int studentID);

    Student searchByID(int studentID);

    List<Student> searchByName(String name);

    List<Student> getAllStudents();
}