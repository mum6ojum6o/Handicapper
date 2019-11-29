package com.handicapper.handicapper.proto1.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Player;

@Repository
public interface GolfCourseRepository extends CrudRepository<GolfCourse, Long> {

	
}
