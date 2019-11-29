package com.handicapper.handicapper.proto1.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Hole;
import com.handicapper.handicapper.proto1.POJOs.Player;
import com.handicapper.handicapper.proto1.POJOs.Round;
import com.handicapper.handicapper.proto1.POJOs.Tee;
import com.handicapper.handicapper.proto1.repos.GolfCourseRepository;
import com.handicapper.handicapper.proto1.repos.HoleRepository;
import com.handicapper.handicapper.proto1.repos.PlayerRepository;
import com.handicapper.handicapper.proto1.repos.RoundRepository;
import com.handicapper.handicapper.proto1.repos.TeeRepository;

@Service
public class GolfCourseService {
	@Autowired(required=true)
	private GolfCourseRepository mGolfCourseRepository;
	@Autowired(required=true)
	private PlayerRepository mPlayerRepository;
	@Autowired(required=true)
	private TeeRepository mTeeRepository;
	
	@Autowired(required=true)
	private HoleRepository mHoleRepository;
	
	@Autowired(required=true)
	private RoundRepository mRoundRepository;
	
	@Autowired
	private HandicapCalculator handicapCalculator;
	
	
	public GolfCourse addGolfCourse(GolfCourse newGolfCourse) {
		for(Player p: newGolfCourse.getPlayers()) {
			Player pl = mPlayerRepository.findById(p.getId()).orElse(null);
			if(pl!=null) {
				pl.getMemberOf().add(newGolfCourse);
			}
		}
		mGolfCourseRepository.save(newGolfCourse);
		return newGolfCourse;
	}
	public GolfCourse getGolfCourse(long Id) {
		Optional<GolfCourse> golfCourse= mGolfCourseRepository.findById(Id);
		if(golfCourse !=null) {
			Set<Player> members = getMembersByGolfCourse(Id).stream().collect(Collectors.toSet());
			golfCourse.get().setPlayers(members);
		}
		return golfCourse.orElse(null);
	}
	public List<GolfCourse> getGolfCourses(){
		Iterable<GolfCourse> itr =  mGolfCourseRepository.findAll();
		List<GolfCourse> golfCourses = new ArrayList<>();
		for(GolfCourse aGolfCourse: itr) 
			golfCourses.add(aGolfCourse);
		return golfCourses;
	}
	public GolfCourse updateGolfCourseFast(long id, GolfCourse updatedGolfCourse) {
		mGolfCourseRepository.save(updatedGolfCourse);
		return updatedGolfCourse;
	}
	public boolean associatMember(GolfCourse golfCourse, long memberId) {
		Player memberToBeAdded = mPlayerRepository.findById(memberId).orElse(null);
		if(memberToBeAdded==null) return false;
		golfCourse.getPlayers().add(memberToBeAdded);
		mGolfCourseRepository.save(golfCourse);
		return true;
	}
	public GolfCourse associateTee(long id, Tee tee) {
		Tee existingTee = mTeeRepository.findById(tee.getId()).orElse(null);
		GolfCourse existingGolfCourse = mGolfCourseRepository.findById(id).orElse(null);
		if(existingTee == null || existingGolfCourse==null) return null;
		existingGolfCourse.getTees().add(existingTee);
		existingTee.setGolfCourse(existingGolfCourse);
		mTeeRepository.save(existingTee);
		mGolfCourseRepository.save(existingGolfCourse);
		return existingGolfCourse;
	}
	
	public GolfCourse associateHole(long id, Hole hole){
		Hole existingHole = mHoleRepository.findById(hole.getId()).orElse(null); 
		GolfCourse existingGolfCourse = mGolfCourseRepository.findById(id).orElse(null);
		if(existingHole == null || existingGolfCourse==null) return null;
		existingGolfCourse.getHoles().add(existingHole);
		existingHole.setGolfCourse(existingGolfCourse);
		mHoleRepository.save(existingHole);
		mGolfCourseRepository.save(existingGolfCourse);
		return existingGolfCourse;
	}
	
	public List<Player> getMembersById(long id){
		List<Player> members = new ArrayList<>();
		GolfCourse golfCourse = mGolfCourseRepository.findById(id).orElse(null);
		for(Player aMember: mPlayerRepository.findAll()) {
			if(aMember.getMemberOf().contains(golfCourse)) {
				members.add(aMember);
			}
		}
		return members;
	}
	
	public List<Player> getMembersByGolfCourse(long id){
		List<Player> result=null;
		GolfCourse golfCourse = mGolfCourseRepository.findById(id).orElse(null);
		if(golfCourse!=null) {
			result=mPlayerRepository.findByMemberOf(golfCourse);
			
			for(Player aPlayer:result) {
				Set<GolfCourse> courses = aPlayer.getMemberOf().stream().filter(gc -> gc.getId() == id)
						.collect(Collectors.toSet());
				Set<Round> rounds = aPlayer.getRoundsPlayed().stream()
				.filter(r -> r.getGolfCourse().equals(golfCourse)).collect(Collectors.toSet());
				aPlayer.setRoundsPlayed(rounds);
				aPlayer.setMemberOf(courses);
			}
		}
		
		return result;
	}
	
	
	public Player getPlayerDetailsSpecificToGolfCourse(long golfCourseId, long playerId) {
		Player player = this.mPlayerRepository.findById(playerId).orElse(null);
		
		Set<GolfCourse> filteredGC = player.getMemberOf().stream()
				.filter(gc -> gc.getId()== golfCourseId).collect(Collectors.toSet());
		player.setMemberOf( filteredGC);
		Set<Round> rounds =  player.getRoundsPlayed().stream()
				.filter(r ->r.getGolfCourse().getId()== golfCourseId).collect(Collectors.toSet());
		player.setRoundsPlayed(rounds);
		
		return player;

	}
	
	public double getHCP(long playerId, long golfCourseId) {
		Player player = this.mPlayerRepository.findById(playerId).orElse(null);
		GolfCourse golfCourse = this.mGolfCourseRepository.findById(golfCourseId).orElse(null);
		if(player==null|| golfCourse==null) return Double.MIN_VALUE;
		
		return this.handicapCalculator.calculateHCP(player, golfCourse);
	}
}
