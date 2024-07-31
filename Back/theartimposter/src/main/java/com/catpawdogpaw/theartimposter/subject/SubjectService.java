package com.catpawdogpaw.theartimposter.subject;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.catpawdogpaw.theartimposter.subject.mapper.SubjectMapper;
import com.catpawdogpaw.theartimposter.subject.model.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectService {
	private final SubjectMapper subjectMapper;

    public void createSubject(Subject subject) {
        subjectMapper.createSubject(subject);
    }


    public void deleteSubject(Long id) {
        subjectMapper.deleteSubject(id);
    }


    public Subject getSubjectById(@PathVariable Long id) {

        return subjectMapper.getSubjectById(id);
    }


    public List<Subject> getAllSubjects() {
        return subjectMapper.getAllSubjects();
    }
	

}
