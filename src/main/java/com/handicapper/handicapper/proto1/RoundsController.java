package com.handicapper.handicapper.proto1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.handicapper.handicapper.proto1.POJOs.Round;
import com.handicapper.handicapper.proto1.services.HandicapCalculator;
import com.handicapper.handicapper.proto1.services.PlayerService;
import com.handicapper.handicapper.proto1.services.RoundService;

@CrossOrigin
@RestController
public class RoundsController {
	@Autowired
	private RoundService mRoundsService;
	@Autowired
	private HandicapCalculator handicapCalculator;
	
	@RequestMapping(method=RequestMethod.POST, value="/rounds")
	public Round addRound(@RequestBody Round aRound) {
		return mRoundsService.addRound(aRound);
		
	}
	@RequestMapping(method=RequestMethod.GET,value="rounds")
	public List<Round> getRounds(){
		return mRoundsService.getAllRounds();
	}
	
	@RequestMapping(method=RequestMethod.GET,value="rounds/{id}")
	public Round getARound(@PathVariable("id")long id) {
		return mRoundsService.getARound(id);
	}
	@RequestMapping(method=RequestMethod.GET,value="rounds/getHandicap/{id}")
	public Round getHandicap(@PathVariable("id")long id) {
		return handicapCalculator.updateHcp(id);
	}
	
}
