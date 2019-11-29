package com.handicapper.handicapper.proto1.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.handicapper.handicapper.proto1.POJOs.Tee;

@Repository
public interface TeeRepository extends CrudRepository<Tee,Long>{

}