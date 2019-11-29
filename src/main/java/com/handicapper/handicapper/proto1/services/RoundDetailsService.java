package com.handicapper.handicapper.proto1.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Hole;
import com.handicapper.handicapper.proto1.POJOs.Player;
import com.handicapper.handicapper.proto1.POJOs.Round;
import com.handicapper.handicapper.proto1.POJOs.RoundDetails;
import com.handicapper.handicapper.proto1.repos.GolfCourseRepository;
import com.handicapper.handicapper.proto1.repos.HoleRepository;
import com.handicapper.handicapper.proto1.repos.PlayerRepository;
import com.handicapper.handicapper.proto1.repos.RoundDetailRepository;
import com.handicapper.handicapper.proto1.repos.RoundRepository;
//Test
@Service
public class RoundDetailsService {

	@Autowired(required=true)
	private RoundDetailRepository mRoundsDetailRepository;
	
	@Autowired(required=true)
	private RoundRepository mRoundHeaderRepository;
	
	@Autowired(required=true)
	private HoleRepository mHoleRepository;
	
	@Autowired
	private HandicapCalculator handicapCalculator;
	
	
	public RoundDetails addRoundDetail(RoundDetails aRoundDetail) {
		
		Round roundHeader = mRoundHeaderRepository.
							findById(aRoundDetail.getRound().getId()).orElse(null);
		Hole hole = mHoleRepository.findById(aRoundDetail.getHole().getId()).orElse(null);
		if(roundHeader==null || aRoundDetail==null || hole==null)
			return null;
		
		
		if(holeInGolfCourse(hole,roundHeader.getGolfCourse())) {
			roundHeader.getRoundDetails().add(aRoundDetail);
			mRoundHeaderRepository.save(roundHeader);
			return mRoundsDetailRepository.save(aRoundDetail);
		}
		return null;
	}
	
	private boolean holeInGolfCourse(Hole aHole,GolfCourse gc) {
		return gc.getHoles().contains(aHole);
	}
	
	public List<RoundDetails> getAllDetails(){
		Iterable<RoundDetails> iterable = mRoundsDetailRepository.findAll();
		List<RoundDetails> result = new ArrayList<>();
		for(RoundDetails roundDetail: iterable) {
			result.add(roundDetail);
		}
		return result;
	}
	
	public Round addAllRounds(List<RoundDetails> roundDetails) {
		
		if(roundDetails == null || roundDetails.size() == 0) return null;
		Round roundHeader = validateAllRoundDetailContainValidRoundHeader(roundDetails);
		if(roundHeader==null) return null;
		
		roundDetails = this.handicapCalculator.calculateAdjustableScores(roundDetails);
		
		mRoundsDetailRepository.saveAll(roundDetails);
		
		if(allHolesPlayed(roundDetails)) {
			long []aggScores=this.getAggregateScores(roundDetails);
			if(aggScores!=null && aggScores.length==2) { 
				roundHeader.setAdjustedScore(aggScores[1]);
				roundHeader.setShotsTaken((int)aggScores[0]);
				roundHeader.setHandicapDifferential(this.handicapCalculator.calculateHandicapDifferential(roundHeader));
			}
			this.mRoundHeaderRepository.save(roundHeader);
		/*if(hcpToBeCalculated)
			roundHeader = this.handicapCalculator.updateHcp(roundDetails.get(0).getRound().getId());*/
		}
		return this.mRoundHeaderRepository.findById(roundHeader.getId()).orElse(null);
	}
	public RoundDetails updateRoundDetails(RoundDetails updatedRoundDetail,long id){
		RoundDetails currentRoundDetail = mRoundsDetailRepository.findById(id).orElse(null);
		if(currentRoundDetail == null) return null;
		if(updatedRoundDetail.getShotsTaken()<=0) return currentRoundDetail;
		currentRoundDetail.setShotsTaken(updatedRoundDetail.getShotsTaken());
		currentRoundDetail = this.handicapCalculator.calculaterAdjustableScores(currentRoundDetail);
		//TODO: if all the roundDetails have score >0 and adjustableScores>0 then calc differential and everything else
		Round roundHeader = currentRoundDetail.getRound();
		mRoundsDetailRepository.save(currentRoundDetail);
		List<RoundDetails> roundDetails = roundHeader.getRoundDetails().stream().collect(Collectors.toList());
		if(this.allHolesPlayed(roundDetails)) {
			long[] aggScores = this.getAggregateScores(roundDetails);
			if(aggScores!=null|| aggScores.length > 0) {
				roundHeader.setAdjustedScore(aggScores[1]);
				roundHeader.setShotsTaken((int)aggScores[0]);
				roundHeader.setHandicapDifferential(this.handicapCalculator.calculateHandicapDifferential(roundHeader));
				this.mRoundHeaderRepository.save(roundHeader);
			} 
		}
		return currentRoundDetail;
	}
	
	private Round validateAllRoundDetailContainValidRoundHeader(List<RoundDetails> roundDetails) {
		Round roundHeader = this.mRoundHeaderRepository.findById(roundDetails.get(0).getRound().getId()).orElse(null);
		if(roundHeader==null) return roundHeader;
		for(RoundDetails aHolePlayed: roundDetails) {
			if(aHolePlayed.getRound().getId()!=roundHeader.getId()) return roundHeader;
		}
		return roundHeader;
	}
	private boolean allHolesPlayed(List<RoundDetails> roundDetails) {
		Set<Long>holesPlayed = new HashSet<>();
		Round roundHeader = this.mRoundHeaderRepository.findById(roundDetails.get(0).getRound().getId()).orElse(null);
		for(RoundDetails aHolePlayed: roundDetails) {
			if(aHolePlayed.getShotsTaken()>0)
				holesPlayed.add(aHolePlayed.getHole().getId());
		}
		return holesPlayed.size() == roundHeader.getGolfCourse().getHoles().size()? true:false;
	}
	
	private long[] getAggregateScores(List<RoundDetails> roundDetails) {
		if(roundDetails==null||roundDetails.size()==0) return null;
		long []aggScores = new long[2];
		for(RoundDetails rd:roundDetails) {
			aggScores[0]+=rd.getShotsTaken();
			aggScores[1]+=rd.getAdjustedScore();
		}
		return aggScores;
	}
}
