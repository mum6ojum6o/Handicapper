package com.handicapper.handicapper.proto1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.handicapper.handicapper.proto1.POJOs.GolfCourse;
import com.handicapper.handicapper.proto1.POJOs.Tee;
import com.handicapper.handicapper.proto1.services.TeeService;
@CrossOrigin
@RestController
public class TeeController {

	@Autowired
	TeeService mTeeService;
	
	@RequestMapping("/tees")
	public List<Tee> getTees(){
		return mTeeService.getTees();
	}
	
	@RequestMapping("/tees/{id}")
	public List<Tee> getTee(@PathVariable("id") long id){
		return mTeeService.getTees();
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/tees")
	public Tee addTee(@RequestBody Tee tee) {
		return mTeeService.addTee(tee);
	}
	@RequestMapping(method=RequestMethod.PUT,value="/tees/{id}")
	public Tee updateTee(@PathVariable("id") long id, @RequestBody Tee tee) {
		mTeeService.updateTee(id,tee);
		return tee; 
	}
	
}
