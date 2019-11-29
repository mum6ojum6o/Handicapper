package com.handicapper.handicapper.proto1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.handicapper.handicapper.proto1.POJOs.Hole;
import com.handicapper.handicapper.proto1.POJOs.Tee;
import com.handicapper.handicapper.proto1.services.HoleService;

@CrossOrigin
@RestController
public class HolesController {
	@Autowired
	HoleService mHolesService;
	
	@RequestMapping("/holes")
	public List<Hole> getHoles(){
		return mHolesService.getHoles();
	}
	
	@RequestMapping("/holes/{id}")
	public Hole getHoleById(@PathVariable("id") long id) {
		return mHolesService.getHoleById(id);
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/holes")
	public Hole addHole(@RequestBody Hole hole) {
		return mHolesService.addHole(hole);
	}
	@RequestMapping(method=RequestMethod.PUT,value="/holes/{id}")
	public Hole updateTee(@PathVariable("id") long id, @RequestBody Hole hole) {
		mHolesService.updateTee(id,hole);
		return hole; 
	}
}
