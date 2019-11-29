package com.handicapper.handicapper.proto1.services;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Player;
import com.handicapper.handicapper.proto1.POJOs.Round;
import com.handicapper.handicapper.proto1.POJOs.RoundDetails;
import com.handicapper.handicapper.proto1.POJOs.Tee;
import com.handicapper.handicapper.proto1.repos.HoleRepository;
import com.handicapper.handicapper.proto1.repos.RoundDetailRepository;
import com.handicapper.handicapper.proto1.repos.RoundRepository;
import com.handicapper.handicapper.proto1.POJOs.Hole;
@Service
public class HandicapCalculator {
	
	@Autowired
	RoundRepository roundRepository;
	
	@Autowired
	RoundDetailRepository roundDetailRepository;
	@Autowired
	HoleRepository mHoleRepository;
	/*********************************************************
	 * Handicap Differential =  ((Adjusted Gross Score - Course Rating) * 113)/Slope Rating of Tee )
	 * HCP Index = Handicap Differential *0.96
	 * 
	 ************************************************************/
	public Round updateHcp(long roundid) {
		double differential=0.0;
		Round aRound = roundRepository.findById(roundid).orElse(null);
		if(aRound == null) return null;
		
		aRound.setShotsTaken(aggregateTotalStrokes(aRound.getRoundDetails()));
		differential = aRound.getShotsTaken() - aRound.getTee().getCourseRating();
		differential *=113;
		differential /=aRound.getTee().getSlopeRating();
		//differential *= 0.96;
		aRound.setHandicapDifferential(differential);
		return roundRepository.save(aRound);
		
	}
	
	/*******************************************************************************
	 * Function to calculate the HCPIndex of a player.
	 * Method is usually used to populate the hcpIndex in the round header table.
	 * @param player
	 * @param golfCourse
	 * @return
	 ******************************************************************************/
	public double calculateHCP(Player player , GolfCourse golfCourse) {
		
		List<Round> roundsRecorded = this.roundRepository.findByPlayedByAndGolfCourse(player, golfCourse);
		if(roundsRecorded == null||roundsRecorded.size()==0) return 36.4;//should be 40.4 for women
		List<Round> elligibleRounds = roundsRecorded.stream()
				.filter(r -> r.getShotsTaken()>0 && r.getHandicapDifferential()!=0)
				.collect(Collectors.toList());
		short numberOfDiffsToBeConsidered = numberOfDifferentialsToBeConsidered(elligibleRounds.size());
		if(numberOfDiffsToBeConsidered <0) return 0.0d;  //sanity check
		//calculate the sum of the minimum (numberOfDiffsToBeConsidered) differentials.
		double sumHandicapDifferential = getMinHandicapDifferentialSum(elligibleRounds, numberOfDiffsToBeConsidered);
		
		return ((sumHandicapDifferential/numberOfDiffsToBeConsidered)*0.96);
	}
	
	/*************************************************************
	 * Method to calculate the course Handicap
	 * Tee tee = tee the player would be playing on.
	 ************************************************************ */
	public long calculateCourseHandicap(double hcpIndex, Tee tee) {
		return Math.round((hcpIndex*tee.getSlopeRating())/113);
	}
	
	/**************************************************************
	 * Method that returns the Maximum Equitable Stroke Control
	 **************************************************************/
	public long getEquitableStroke(long courseHandicap) {
		if(courseHandicap <=9) return 2;
		else if(courseHandicap <= 19)return 7;
		else if(courseHandicap <= 29)return 8;
		else if(courseHandicap <= 39)return 9;
		else return 10;
	} 
	
	/***************************************
	 * 
	 * 
	 * @param roundDetails
	 * @return roundDetails with updated adjusted scores.
	 */
	public List<RoundDetails> calculateAdjustableScores(List<RoundDetails> roundDetails){
		Round round = this.roundRepository.findById(roundDetails.get(0).getRound().getId()).orElse(null);
		if(round == null) return null; //TODO: Exception Handling....
		long courseHandicap = this.calculateCourseHandicap(round.getHcpIndex(), round.getTee());
		long maxStrokes = this.getEquitableStroke(courseHandicap);
		for(RoundDetails rd: roundDetails) {
			Hole hole = this.mHoleRepository.findById(rd.getHole().getId()).orElse(null);
			if(hole==null) continue;
			if(maxStrokes==2)
				rd.setAdjustedScore(Math.min(rd.getShotsTaken(), hole.getPar()+2));
			else
				rd.setAdjustedScore(Math.min(rd.getShotsTaken(), maxStrokes));
			
			System.out.println("ShotsTaken:"+rd.getShotsTaken()+" Adjusted:"+rd.getAdjustedScore());
		}
		return roundDetails;
	}
	
	
	public RoundDetails calculaterAdjustableScores(RoundDetails roundDetail) {
		Round round = this.roundRepository.findById(roundDetail.getRound().getId()).orElse(null);
		if(round == null) return null; //TODO: Exception Handling....
		long courseHandicap = this.calculateCourseHandicap(round.getHcpIndex(), round.getTee());
		long maxStrokes = this.getEquitableStroke(courseHandicap);
		Hole hole = this.mHoleRepository.findById(roundDetail.getHole().getId()).orElse(null);
		if(hole==null) return null;
		if(maxStrokes==2)
			roundDetail.setAdjustedScore(Math.min(roundDetail.getShotsTaken(), hole.getPar()+2));
		else
			roundDetail.setAdjustedScore(Math.min(roundDetail.getShotsTaken(), maxStrokes));
		
		return roundDetail;
	}
	
	public double calculateHandicapDifferential(Round roundHeader) {
		if(roundHeader==null) return 0.0;
		double handicapDifferential = 
				(((roundHeader.getAdjustedScore()-roundHeader.getTee().getCourseRating())*113)/roundHeader.getTee().getSlopeRating());
		System.out.println("HandicapDifferential:"+handicapDifferential);
		return handicapDifferential;
	}
	/******************************* 
	 * @param numOfRoundsPlayed= total number of rounds played by the user
	 * @return number of handicap differential to be considered for HCPIndex calc
	 ****************************/
	private short numberOfDifferentialsToBeConsidered(long numOfRoundsPlayed) {
		if(numOfRoundsPlayed<=0) return -1;
		if(numOfRoundsPlayed <=6)
			return 1;
		else if(numOfRoundsPlayed <=8)
			return 2;
		else if(numOfRoundsPlayed <=10)
			return 3;
		else if(numOfRoundsPlayed <=12)
			return 4;
		else if(numOfRoundsPlayed <=14)
			return 5;
		else if(numOfRoundsPlayed <=16)
			return 6;
		else if(numOfRoundsPlayed <=17)
			return 7;
		else if(numOfRoundsPlayed <=18)
			return 8;
		else if(numOfRoundsPlayed <=19)
			return 9;
		else
			return 10;
	}
	
	/*************************************************************
	 * 
	 * Function to return the sum of handicap differentials.
	 * @param elligibleRounds  - rounds considered for extracting handicap diffs
	 * @param numberOfDiffsToBeConsidered - total number of handicaps to be considered.
	 * @return sum of the handicap differential
	 ****************************************************************/
	private double getMinHandicapDifferentialSum(List<Round>elligibleRounds,short numberOfDiffsToBeConsidered) {
		List<Round> roundsSortedBasisOfHandicapDifferential = elligibleRounds.stream().sorted(new Comparator<Round>() {

			@Override
			public int compare(Round arg0, Round arg1) {
				if(arg0.getHandicapDifferential() <= arg1.getHandicapDifferential())
				return -1;
				else return 1;
			}}).collect(Collectors.toList());
		double sum=0;;
		for(int i=0;i< numberOfDiffsToBeConsidered;i++) {
			System.out.println("diff:"+roundsSortedBasisOfHandicapDifferential.get(i).getHandicapDifferential());
			sum+=roundsSortedBasisOfHandicapDifferential.get(i).getHandicapDifferential();
		}
		System.out.println("MinHandicapDiffSUM: "+sum);
		return sum;
	}
	
	
	private int aggregateTotalStrokes(Set<RoundDetails> roundDetails){
		int strokes = 0;
		Iterator<RoundDetails> itr = roundDetails.iterator();
		while(itr.hasNext()) {
			strokes+=itr.next().getShotsTaken();
		}
		return strokes;
	}
	/********************************************
	 * 
	 * Course handicap = (HCP Index * Course Slope)/113
	 * 
	 ********************************************/

}
