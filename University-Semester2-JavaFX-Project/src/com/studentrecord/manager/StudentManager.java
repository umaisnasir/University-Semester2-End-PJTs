package com.studentrecord.manager;

import com.studentrecord.model.Student;
import com.studentrecord.util.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class StudentManager implements RecordManager
{
    private List<Student> studentList;
    private FileHandler   fileHandler;


    public StudentManager()
    {
        fileHandler = new FileHandler();
        studentList = fileHandler.loadAllStudents();
    }


    @Override
    public void addStudent(Student student)
    {
        studentList.add(student);
        fileHandler.saveAllStudents(studentList);
    }


    @Override
    public void updateStudent(Student updatedStudent)
    {
        for (int i = 0; i < studentList.size(); i++)
        {
            if (studentList.get(i).getStudentID() == updatedStudent.getStudentID())
            {
                studentList.set(i, updatedStudent);
                break;
            }
        }

        fileHandler.saveAllStudents(studentList);
    }


    @Override
    public void deleteStudent(int studentID)
    {
        studentList.removeIf(s -> s.getStudentID() == studentID);
        fileHandler.saveAllStudents(studentList);
    }


    @Override
    public Student searchByID(int studentID)
    {
        for (Student student : studentList)
        {
            if (student.getStudentID() == studentID)
            {
                return student;
            }
        }

        return null;
    }


    @Override
    public List<Student> searchByName(String name)
    {
        List<Student> results = new ArrayList<>();
        String        keyword = name.toLowerCase().trim();

        for (Student student : studentList)
        {
            if (student.getName().toLowerCase().contains(keyword))
            {
                results.add(student);
            }
        }

        return results;
    }


    @Override
    public List<Student> getAllStudents()
    {
        return new ArrayList<>(studentList);
    }


    public boolean idAlreadyExists(int studentID)
    {
        return searchByID(studentID) != null;
    }
}