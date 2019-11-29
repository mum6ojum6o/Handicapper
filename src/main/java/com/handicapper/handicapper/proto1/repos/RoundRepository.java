package com.handicapper.handicapper.proto1.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Player;
import com.handicapper.handicapper.proto1.POJOs.Round;

@Repository
public interface RoundRepository extends CrudRepository<Round,Long>{
	
	public List<Round> findByPlayedByAndGolfCourse(Player player, GolfCourse golfCourse);

}
