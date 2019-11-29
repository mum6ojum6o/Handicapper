package com.handicapper.handicapper.proto1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Hole;
import com.handicapper.handicapper.proto1.POJOs.Player;
import com.handicapper.handicapper.proto1.POJOs.Tee;
import com.handicapper.handicapper.proto1.services.GolfCourseService;
import com.handicapper.handicapper.proto1.services.PlayerService;

@CrossOrigin
@RestController
public class GolfCourseController {
	@Autowired
	private GolfCourseService mGolfCourseService;
	@Autowired
	private PlayerService mPlayerService;
	
	@RequestMapping("/golfCourses")
	public List<GolfCourse> getCourses(){
		return mGolfCourseService.getGolfCourses();
	}
	@RequestMapping(method=RequestMethod.POST,value="/golfCourses")
	public void addCourse(@RequestBody GolfCourse newCourse) {
		//System.out.println("In Course Service!!!"+newCourse.getId()+" "+newCourse.getName()+" ");
		mGolfCourseService.addGolfCourse(newCourse);
	}
	@RequestMapping("/golfCourses/{id}")
	public GolfCourse getTopicById(@PathVariable("id") long id) {
		return mGolfCourseService.getGolfCourse(id);
	}
	@RequestMapping(method=RequestMethod.PUT,value="/golfCourses/{id}/tees")
	public GolfCourse associateTeeWithGolfCourse(@PathVariable("id")long id,@RequestBody Tee tee) {
		return mGolfCourseService.associateTee(id,tee);
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="/golfCourses/{id}/holes")
	public GolfCourse associateTeeWithGolfCourse(@PathVariable("id")long id,@RequestBody Hole hole) {
		return mGolfCourseService.associateHole(id,hole);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/golfCourses/{id}/players")
	public List<Player> getMembers(@PathVariable("id")long id) {
		//return mGolfCourseService.getMembersById(id);
		return mGolfCourseService.getMembersByGolfCourse(id);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/golfCourses/{id}/players/{playerId}")
	public Player getPlayerDetailSpecificToTheCourse(@PathVariable("id")long id, @PathVariable("playerId") long playerId) {
		//return new Player(124321412,"abra","ka Dabra","321423","2asd@fds.com",null,null);
		//return this.mPlayerService.getPlayerMemberOf(playerId, id);
		return this.mGolfCourseService.getPlayerDetailsSpecificToGolfCourse(id,playerId);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/golfCourses/hcpIndex/{id}/players/{playerId}")
	public double getHcpIndex(@PathVariable("id")long id, @PathVariable("playerId") long playerId) {
		return this.mGolfCourseService.getHCP(playerId, id);
	}
	
	/*@RequestMapping(method=RequestMethod.PUT,value="/golfCourses/{id}")
	public GolfCourse updatePlayer(@RequestBody GolfCourse updatedGolfCourse,@PathVariable long id) {
		return mGolfCourseService.updateGolfCourse(updatedGolfCourse);
	}*/
}
