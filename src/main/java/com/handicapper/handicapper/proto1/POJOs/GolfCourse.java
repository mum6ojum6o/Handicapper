package com.handicapper.handicapper.proto1.POJOs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class GolfCourse {
	@javax.persistence.Id
	@SequenceGenerator(name="SEQ_GOLF_COURSE_IDs", sequenceName="SEQ_GOLF_COURSE_ID_GEN",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GOLF_COURSE_IDs")
	private long Id;
	
	private String CourseName;
	@ManyToMany(cascade = {
	        CascadeType.PERSIST,
	        CascadeType.MERGE
	    })
	@JoinTable(name="player_GC_membership",
		joinColumns= {@JoinColumn(name="player_id", referencedColumnName = "id")},
		inverseJoinColumns= {@JoinColumn(name="GolfCourse_id", referencedColumnName ="id")}
	)
	@JsonBackReference
	private Set<Player> players = new HashSet<>();
	
	@OneToMany(mappedBy="golfCourse",cascade = CascadeType.ALL)
	@JsonManagedReference(value="tees")
	private Set<Tee> tees = new HashSet<>();
	
	@OneToMany(mappedBy="golfCourse",cascade = CascadeType.ALL)
	//@JsonBackReference(value="holes")
	@JsonManagedReference(value="holes")
	private Set<Hole> holes = new HashSet<>();
	
	@OneToMany(mappedBy="golfCourse")
	@JsonManagedReference(value="rounds" )
	private Set<Round> rounds = new HashSet<>();
	
	public GolfCourse() {
		
	}
	public GolfCourse(long id, String courseName, Set<Player> players, Set<Tee> tees,Set<Hole> holes,Set<Round> rounds) {
		System.out.println(players.size()+" ");
		Id = id;
		CourseName = courseName;
		this.players = players;
		this.tees = tees;
		this.holes = holes;
		this.rounds = rounds;
	}
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public String getCourseName() {
		return CourseName;
	}
	public void setCourseName(String courseName) {
		CourseName = courseName;
	}
	public Set<Player> getPlayers() {
		return players;
	}
	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
	public Set<Tee> getTees() {
		return tees;
	}
	public void setTees(Set<Tee> tees) {
		this.tees = tees;
	}
	public Set<Hole> getHoles() {
		return holes;
	}
	public void setHoles(Set<Hole> holes) {
		this.holes = holes;
	}
	public Set<Round> getRoundsPlayed() {
		return rounds;
	}
	public void setRoundsPlayed(Set<Round> rounds) {
		this.rounds = rounds;
	}
	

}
