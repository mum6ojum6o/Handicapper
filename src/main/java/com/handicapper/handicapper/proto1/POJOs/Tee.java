package com.handicapper.handicapper.proto1.POJOs;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Tee {
	@Id
	@SequenceGenerator(name="SEQ_TEE_IDs", sequenceName="SEQ_TEE_ID_GEN",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TEE_IDs")
	private long id;
	private String teeName;
	//private double hcpIndex;
	private double courseRating;
	private double slopeRating;
	@ManyToOne
	@JoinColumn
	@JsonBackReference(value="tees")
	private GolfCourse golfCourse;
	
	@OneToMany(cascade= {CascadeType.ALL})
	@JsonBackReference(value="tee")
	private Set<Round> rounds=new HashSet<>();
	
	public Tee() {
		super();
	}
	
	public Tee(long id, String teeName, /*double hcpIndex,*/ double slopeRating, GolfCourse golfCourse) {
		super();
		this.id = id;
		this.teeName = teeName;
		//this.hcpIndex = hcpIndex;
		this.slopeRating = slopeRating;
		this.golfCourse = golfCourse;
	}
	
	
	public String getTeeName() {
		return teeName;
	}
	public void setTeeName(String teeName) {
		this.teeName = teeName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public GolfCourse getGolfCourse() {
		return golfCourse;
	}
	public void setGolfCourse(GolfCourse golfCourse) {
		this.golfCourse = golfCourse;
	}
	/*public double getHcpIndex() {
		return hcpIndex;
	}
	public void setHcpIndex(double hcpIndex) {
		this.hcpIndex = hcpIndex;
	}*/
	public double getSlopeRating() {
		return slopeRating;
	}
	public void setSlopeRating(double slopeRating) {
		this.slopeRating = slopeRating;
	}

	public double getCourseRating() {
		return courseRating;
	}

	public void setCourseRating(double courseRating) {
		this.courseRating = courseRating;
	}
	public void setRounds(Set<Round> rounds) {
		this.rounds = rounds;
	}
	public Set<Round> getRounds(){
		return this.rounds;
	}
	
}
