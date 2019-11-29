package com.handicapper.handicapper.proto1.POJOs;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Round {
	@Id
	@SequenceGenerator(name="SEQ_ROUND_IDs", sequenceName="SEQ_ROUND_ID_GEN",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ROUND_IDs")
	private long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference(value="rounds")
	private GolfCourse golfCourse;
	
	private long adjustedScore; //adjustedScore of the current round.
	private double hcpIndex;  //hcpIndex prior to current round
	
	@ManyToOne
	@JsonBackReference
	private Player playedBy;
	
	private int shotsTaken; //total shots taken in the current round.
	private long gamePlayedOn;
	private double handicapDifferential; //handicap differential of the current round
	
	@OneToMany(mappedBy="round")
	@JsonManagedReference(value="roundHeader")
	private Set<RoundDetails> roundDetails = new HashSet<>();
	
	@ManyToOne
	private Tee tee;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public GolfCourse getGolfCourse() {
		return golfCourse;
	}
	public void setGolfCourse(GolfCourse aGolfCourse) {
		this.golfCourse = aGolfCourse;
	}
	public Player getPlayedBy() {
		return playedBy;
	}
	public void setPlayedBy(Player player) {
		this.playedBy = player;
	}
	public int getShotsTaken() {
		return shotsTaken;
	}
	public void setShotsTaken(int shotsTaken) {
		this.shotsTaken = shotsTaken;
	}
	public long getGamePlayedOn() {
		return gamePlayedOn;
	}
	public void setGamePlayedOn(long gamePlayedOn) {
		this.gamePlayedOn = gamePlayedOn;
	}
	public Set<RoundDetails> getRoundDetails() {
		return roundDetails;
	}
	public void setRoundDetails(Set<RoundDetails> roundDetails) {
		this.roundDetails = roundDetails;
	}
	public void setTee(Tee aTee) {
		this.tee= aTee;
	}
	public Tee getTee() {
		return this.tee;
	}
	public void setHandicapDifferential(double calcIndex) {
		this.handicapDifferential = calcIndex;
	}
	
	public double getHandicapDifferential() {
		return this.handicapDifferential;
	}
	public long getAdjustedScore() {
		return adjustedScore;
	}
	public void setAdjustedScore(long adjustedScore) {
		this.adjustedScore = adjustedScore;
	}
	public double getHcpIndex() {
		return hcpIndex;
	}
	public void setHcpIndex(double hcpIndex) {
		this.hcpIndex = hcpIndex;
	}
	
	
	
}
