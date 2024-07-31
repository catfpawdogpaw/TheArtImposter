package com.catpawdogpaw.theartimposter.subject;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catpawdogpaw.theartimposter.subject.model.Subject;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectCotroller {
	private final SubjectService subjectService;
	
	@PostMapping
	public void createSubject(@RequestBody Subject subject) {
		subjectService.createSubject(subject);
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable("id") Long id) {
    	subjectService.deleteSubject(id);
    }

    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable("id") Long id) {
        return subjectService.getSubjectById(id);
    }

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

}
