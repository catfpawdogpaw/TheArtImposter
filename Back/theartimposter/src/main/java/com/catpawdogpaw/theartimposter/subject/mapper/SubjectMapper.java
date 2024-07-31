package com.catpawdogpaw.theartimposter.subject.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.catpawdogpaw.theartimposter.subject.model.Subject;

@Mapper
public interface SubjectMapper {

	void createSubject(Subject subject);
	void deleteSubject(Long id);
	Subject getSubjectById(Long id);
	List<Subject> getAllSubjects();
}
