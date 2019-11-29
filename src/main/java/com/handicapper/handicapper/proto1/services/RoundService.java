package com.handicapper.handicapper.proto1.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Player;
import com.handicapper.handicapper.proto1.POJOs.Round;
import com.handicapper.handicapper.proto1.POJOs.Tee;
import com.handicapper.handicapper.proto1.repos.GolfCourseRepository;
import com.handicapper.handicapper.proto1.repos.PlayerRepository;
import com.handicapper.handicapper.proto1.repos.RoundRepository;
import com.handicapper.handicapper.proto1.repos.TeeRepository;

@Service
public class RoundService {

	@Autowired(required=true)
	private GolfCourseRepository mGolfCourseRepository;
	@Autowired(required=true)
	private RoundRepository mRoundRepository;
	@Autowired(required=true)
	private PlayerRepository mPlayerRepository;
	
	@Autowired(required=true)
	private TeeRepository mTeeRepository;
	
	@Autowired
	private HandicapCalculator mHandicapCalc;
	
	public Round addRound(Player player,GolfCourse course,Round aNewRound) {
		//TODO:then check if player exists if not, throw an Exception
				//TODO:check if the golfcourse id is valid if not Throw an Exception.
				//TODO:check Hole ids sync  golfcourse played in. if not throw an Exception
		Player playedBy = mPlayerRepository.findById(player.getId()).orElse(null);
		if(playedBy==null) {
			return null;
		}
		GolfCourse playedAt = mGolfCourseRepository.findById(course.getId()).orElse(null);
		if(playedAt==null) return null;
		
		Tee tee = mTeeRepository.findById(aNewRound.getTee().getId()).orElse(null);
		if(tee==null) return null;
		double hcpIndex = this.mHandicapCalc.calculateHCP(playedBy, playedAt);
		System.out.println("HCP Index:"+hcpIndex);
		aNewRound.setHcpIndex(hcpIndex);
		//playedAt.getRoundsPlayed().add(aNewRound);
		//mPlayerRepository.save(playedBy);
		//mGolfCourseRepository.save(playedAt);
		aNewRound = mRoundRepository.save(aNewRound);
		
		
		return aNewRound;
		
	}
	public Round addRound(Round aNewRound) {
		Player playedBy = mPlayerRepository.findById(aNewRound.getPlayedBy().getId()).orElse(null);
		if(playedBy==null) {
			return null;
		}
		GolfCourse playedAt = mGolfCourseRepository.findById(aNewRound.getGolfCourse().getId()).orElse(null);
		if(playedAt==null) return null;
		
		Tee tee = mTeeRepository.findById(aNewRound.getTee().getId()).orElse(null);
		if(tee==null) return null;
		
		//tee.getRounds().add(aNewRound);
		/* Why was I doing this????
		playedBy.getRoundsPlayed().add(aNewRound);
		playedAt.getRoundsPlayed().add(aNewRound);
		*/
		//mTeeRepository.save(tee);
		
		double hcpIndex = this.mHandicapCalc.calculateHCP(playedBy, playedAt);
		aNewRound.setHcpIndex(hcpIndex);
		mPlayerRepository.save(playedBy);
		mGolfCourseRepository.save(playedAt);
		Round aRound = mRoundRepository.save(aNewRound);
		aRound.getPlayedBy();
		aRound.getGolfCourse();
		aRound.getTee();
		return aRound;
		
	}
	public Round updateRound(long roundId,Round updatedRound) {
		Round oldRoundDetails = mRoundRepository.findById(roundId).orElse(null);
		if(oldRoundDetails==null) return null;
		oldRoundDetails.setGolfCourse(updatedRound.getGolfCourse());
		//oldRoundDetails.setPlayer(updatedRound.getPlayer());
		oldRoundDetails.setGamePlayedOn(updatedRound.getGamePlayedOn());
		updatedRound = mRoundRepository.save(oldRoundDetails);
		return oldRoundDetails;
	}
	
	public List<Round> getAllRounds(){
		List<Round> rounds = new ArrayList<>();
		Iterable<Round> rItr = mRoundRepository.findAll();
		for(Round r:rItr) {
			rounds.add(r);
		}
		return rounds;
	}
	
	public Round getARound(long id) {
		Round aRound = mRoundRepository.findById(id).orElse(null);
		return aRound;
	}
}
