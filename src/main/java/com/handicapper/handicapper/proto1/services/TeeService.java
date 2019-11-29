package com.handicapper.handicapper.proto1.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Tee;
import com.handicapper.handicapper.proto1.repos.TeeRepository;

@Service
public class TeeService {

	@Autowired(required=true)
	TeeRepository mTeeRepository;
	
	public Tee addTee(Tee tee) {
		if(tee!=null)
			mTeeRepository.save(tee);
		return tee;
	}
	
	public Tee getTeeById(long id) {
		return mTeeRepository.findById(id).orElse(null);
	}
	
	public List<Tee> getTees() {
		Iterable<Tee> itr =  mTeeRepository.findAll();
		List<Tee> tees = new ArrayList<>();
		for(Tee aTee: itr) 
			tees.add(aTee);
		return tees;
	}
	
	public Tee updateTee(long id, Tee updatedTee) {
		Tee currentTee = mTeeRepository.findById(id).orElse(null);
		if(currentTee==null) return null;
		currentTee.setTeeName(updatedTee.getTeeName());
		//currentTee.setHcpIndex(updatedTee.getHcpIndex());
		currentTee.setSlopeRating(updatedTee.getSlopeRating());
		mTeeRepository.save(currentTee);
		return currentTee;
	}
}
