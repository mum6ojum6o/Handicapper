package com.handicapper.handicapper.proto1.POJOs;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Hole {

	@javax.persistence.Id
	@SequenceGenerator(name="SEQ_HOLE_IDs", sequenceName="SEQ_HOLE_ID_GEN",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_HOLE_IDs")
	private long Id;
	
	private short holeNumber;
	private short strokeIndex;
	private short par;
	
	@ManyToOne
	@JoinColumn
	@JsonBackReference(value="holes")
	GolfCourse golfCourse;
	
	@OneToMany(mappedBy="hole")
	//@JsonManagedReference(value="roundPlayed")
	@JsonBackReference(value="roundPlayed")
	Set<RoundDetails> roundPlayed = new HashSet<>();
	public Hole() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Hole(long id, short holeNumber, short strokeIndex, GolfCourse golfCourse) {
		super();
		Id = id;
		this.holeNumber = holeNumber;
		this.strokeIndex = strokeIndex;
		this.golfCourse = golfCourse;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public short getHoleNumber() {
		return holeNumber;
	}

	public void setHoleNumber(short holeNumber) {
		this.holeNumber = holeNumber;
	}

	public short getStrokeIndex() {
		return strokeIndex;
	}

	public void setStrokeIndex(short strokeIndex) {
		this.strokeIndex = strokeIndex;
	}

	public GolfCourse getGolfCourse() {
		return golfCourse;
	}

	public void setGolfCourse(GolfCourse golfCourse) {
		this.golfCourse = golfCourse;
	}

	public short getPar() {
		return par;
	}

	public void setPar(short par) {
		this.par = par;
	}

	public Set<RoundDetails> getRoundPlayed() {
		return roundPlayed;
	}

	public void setRoundPlayed(Set<RoundDetails> roundPlayed) {
		this.roundPlayed = roundPlayed;
	}
	
	
}
