package com.handicapper.handicapper.proto1.POJOs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.handicapper.handicapper.proto1.repos.GolfCourseRepository;
import com.handicapper.handicapper.proto1.services.GolfCourseService;

@Entity
public class Player {
	@Id
	@SequenceGenerator(name="SEQ_PLAYER_IDs", sequenceName="SEQ_PLAYER_ID_GEN",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PLAYER_IDs")
	private long id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	//private char gender;
	@ManyToMany(mappedBy="players", fetch=FetchType.EAGER)
	private Set<GolfCourse> memberOf = new HashSet<>();
	
	@OneToMany(mappedBy="playedBy")
	@JsonManagedReference
	private Set<Round> rounds = new HashSet<>();
	
	public Set<GolfCourse> getMemberOf() {
		return memberOf;
	}
	
	public void setMemberOf(Set<GolfCourse> memberOf) {
		this.memberOf = memberOf;
	}
	public Player() {
		super();
	}
	public Player(long id, String firstName, String lastName, String phoneNumber, 
			String email, Set<GolfCourse> golfCourseMemberOf,Set<Round>rounds) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.memberOf = golfCourseMemberOf;
		this.rounds = rounds;
	}
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<Round> getRoundsPlayed() {
		return rounds;
	}
	public void setRoundsPlayed(Set<Round> roundsPlayed) {
		this.rounds = roundsPlayed;
	}
	
}
