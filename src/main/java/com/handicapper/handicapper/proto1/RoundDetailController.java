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
import com.handicapper.handicapper.proto1.POJOs.RoundDetails;
import com.handicapper.handicapper.proto1.services.PlayerService;
import com.handicapper.handicapper.proto1.services.RoundDetailsService;

@CrossOrigin
@RestController
public class RoundDetailController {

	@Autowired(required=true)
	RoundDetailsService mRoundDetailsService;
	
	@Autowired(required=true)
	PlayerService mPlayerService;
	
	@RequestMapping("/roundDetails")
	public List<RoundDetails> getAllRoundDetails(){
		return mRoundDetailsService.getAllDetails();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/roundDetails")
	public RoundDetails addRoundDetail(@RequestBody RoundDetails newRoundDetail) {
		return mRoundDetailsService.addRoundDetail(newRoundDetail);
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/addAllRounds")
	public Round addAllRoundDetails(@RequestBody List<RoundDetails> roundDetails){
		return mRoundDetailsService.addAllRounds(roundDetails);
	}
	@RequestMapping(method=RequestMethod.PUT,value="/roundDetails/{id}")
	public RoundDetails updateRoundDetail(@RequestBody RoundDetails roundDetail,@PathVariable long id) {
		return mRoundDetailsService.updateRoundDetails(roundDetail, id);
	}
}
