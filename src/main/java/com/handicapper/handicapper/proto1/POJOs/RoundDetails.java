package com.handicapper.handicapper.proto1.POJOs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class RoundDetails {
	@Id
	@SequenceGenerator(name="SEQ_ROUNDDET_IDs", sequenceName="SEQ_ROUNDDET_ID_GEN",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ROUNDDET_IDs")
	private long Id;
	private long shotsTaken;
	private long adjustedScore;
	
	@ManyToOne
	@JoinColumn
	@JsonBackReference(value="roundHeader")
	private Round round;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	//@JsonBackReference(value="roundPlayed")
	private Hole hole;
	
	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public long getShotsTaken() {
		return shotsTaken;
	}

	public void setShotsTaken(long shotsTaken) {
		this.shotsTaken = shotsTaken;
	}

	public Round getRound() {
		return round;
	}

	public void setRound(Round round) {
		this.round = round;
	}

	public Hole getHole() {
		return hole;
	}

	public void setHole(Hole hole) {
		this.hole = hole;
	}

	public long getAdjustedScore() {
		return adjustedScore;
	}

	public void setAdjustedScore(long adjustedScore) {
		this.adjustedScore = adjustedScore;
	}
	
}
