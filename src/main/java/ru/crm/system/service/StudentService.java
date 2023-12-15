package ru.crm.system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.crm.system.model.Student;
import ru.crm.system.repository.StudentRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    //create update
    public Student save(Student student) {
        return studentRepository.save(student);
    }
    //read
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public List<Student> findAllByNameContaining(String studentName) {
        return studentRepository.findAllByNameContaining(studentName);
    }

    public Student getById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with id " + id + " not found in DB"));
    }

    //delete
    public void delete(int id) {
        studentRepository.deleteById(id);
    }
}