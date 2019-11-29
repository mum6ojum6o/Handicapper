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
import com.handicapper.handicapper.proto1.POJOs.Player;
import com.handicapper.handicapper.proto1.services.GolfCourseService;
import com.handicapper.handicapper.proto1.services.PlayerService;

@CrossOrigin
@RestController
public class PlayerController {
	
	@Autowired
	private PlayerService mPlayerService;
	@Autowired
	private GolfCourseService mGolfCourseService;
	
	@RequestMapping("/players")
	public List<Player> getAllPlayers() {
		return mPlayerService.getPlayers();
	}
	@RequestMapping("/players/{id}")
	public Player getTopicById(@PathVariable("id") long id) {
		return mPlayerService.getPlayer(id);
	}
	@RequestMapping(method=RequestMethod.POST,value="/players")
	public Player addPlayer(@RequestBody Player newPlayer) {
		System.out.println("In Course Service!!!"+newPlayer.getId()+" "+newPlayer.getMemberOf().size()+" ");
		return mPlayerService.addPlayer(newPlayer);
	}
	
	@RequestMapping(method=RequestMethod.PUT,value="/players/{id}")
	public Player updatePlayer(@RequestBody Player player,@PathVariable long id) {
		System.out.println("In Course Service!!!"+player.getId()+" "+player.getMemberOf().size()+" "+player.getEmail());
		return mPlayerService.updatePlayer(id,player);
	}
	@RequestMapping(method=RequestMethod.PUT,value="players/{id}/golfCourse")
	public Player associateGolfCourse(@RequestBody GolfCourse golfCourse, @PathVariable long id) {
		System.out.println("update Course and Player "+id+" golfCourse "+golfCourse.getId());
		return mPlayerService.updatePlayerMembership(id,golfCourse);
	}
	
}
