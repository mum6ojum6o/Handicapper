package com.handicapper.handicapper.proto1.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Player;
import com.handicapper.handicapper.proto1.repos.GolfCourseRepository;
import com.handicapper.handicapper.proto1.repos.PlayerRepository;

@Service
public class PlayerService {
	@Autowired(required=true)
	PlayerRepository mPlayerRepository;
	@Autowired
	GolfCourseRepository mGolfCourseRepository;
	
	public Player addPlayer(Player newPlayer) {
		
		for(GolfCourse gc: newPlayer.getMemberOf()) {
			GolfCourse extractedGolfCourse = mGolfCourseRepository.findById(gc.getId()).orElse(null);
			if(extractedGolfCourse != null) {
				extractedGolfCourse.getPlayers().add(newPlayer);
			}
		}
		
		mPlayerRepository.save(newPlayer);
		return newPlayer;
	}
	public Player getPlayer(long Id) {
		Optional<Player> player= mPlayerRepository.findById(Id);
		return player.orElse(null);
	}
	public List<Player> getPlayers(){
		Iterable<Player> itr =  mPlayerRepository.findAll();
		List<Player> players = new ArrayList<>();
		for(Player player: itr) 
			players.add(player);
		return players;
	}
	private void addGolfCourse(GolfCourse aGc) {
		
	}
	
	
	
	
	public Player updatePlayer(long id,Player updatedPlayer) { 
		Player existingPlayer = mPlayerRepository.findById(id).orElse(null); 
		if(existingPlayer==null)return null;
		existingPlayer.setFirstName(updatedPlayer.getFirstName());
		existingPlayer.setLastName(updatedPlayer.getLastName());
		existingPlayer.getMemberOf().addAll(updatedPlayer.getMemberOf());
		existingPlayer.setEmail(updatedPlayer.getEmail());
		Set<GolfCourse> membershipSet = existingPlayer.getMemberOf(); 
		for(GolfCourse aGolfCourse:membershipSet) {
			aGolfCourse.getPlayers().add(existingPlayer);
			mGolfCourseRepository.save(aGolfCourse);
		}
		mPlayerRepository.save(existingPlayer);
		return updatedPlayer;
	}
	public Player updatePlayerMembership(long id, GolfCourse aGC)
	{
		Player existingPlayer = mPlayerRepository.findById(id).orElse(null); 
		GolfCourse golfCourse = mGolfCourseRepository.findById(aGC.getId()).orElse(null);
		if(existingPlayer==null || golfCourse==null) return null;
		existingPlayer.getMemberOf().add(golfCourse);
		mPlayerRepository.save(existingPlayer);
		golfCourse.getPlayers().add(existingPlayer);
		mGolfCourseRepository.save(golfCourse);
		return existingPlayer;
	}
	
	public Player getPlayerMemberOf(long playerId, long golfCourseId) {
		//Player player = this.mPlayerRepository.findById(playerId).orElse(null);
		GolfCourse gc = this.mGolfCourseRepository.findById(golfCourseId).orElse(null);
		Player player = this.mPlayerRepository.findByMemberOfAndId(gc, playerId).get(0);
		return player;
	}
	
}
