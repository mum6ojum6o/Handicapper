package com.handicapper.handicapper.proto1.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.handicapper.handicapper.proto1.POJOs.Hole;
import com.handicapper.handicapper.proto1.repos.HoleRepository;

@Service
public class HoleService {
	
	@Autowired(required=true)
	HoleRepository mHoleRepository;
	
	public Hole addHole(Hole hole) {
		if(hole!=null)
			mHoleRepository.save(hole);
		return hole;
	}
	
	public Hole getHoleById(long id) {
		return mHoleRepository.findById(id).orElse(null);
	}
	
	public List<Hole> getHoles() {
		Iterable<Hole> itr =  mHoleRepository.findAll();
		List<Hole> holes = new ArrayList<>();
		for(Hole aHole: itr) 
			holes.add(aHole);
		return holes;
	}
	
	public Hole updateTee(long id, Hole updatedHole) {
		Hole currentHole = mHoleRepository.findById(id).orElse(null);
		if(currentHole==null) return null;
		currentHole.setHoleNumber(updatedHole.getHoleNumber());
		currentHole.setStrokeIndex(updatedHole.getStrokeIndex());
		currentHole.setPar(updatedHole.getPar());
		mHoleRepository.save(currentHole);
		return currentHole;
	}
	
}
