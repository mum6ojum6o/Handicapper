package com.handicapper.handicapper.proto1.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player,Long>{
	
	
	List<Player> findByMemberOf(GolfCourse golfCourse);
	List<Player> findByMemberOfAndId(GolfCourse golfCourse,long id);
	
	/*@Query("from Player where")
	Player getPlayerDetailsSpecificToAGolfCourse();*/
}
