package com.handicapper.handicapper.proto1.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.handicapper.handicapper.proto1.POJOs.Player;
import com.handicapper.handicapper.proto1.POJOs.RoundDetails;

@Repository
public interface RoundDetailRepository extends CrudRepository<RoundDetails,Long>{

}
