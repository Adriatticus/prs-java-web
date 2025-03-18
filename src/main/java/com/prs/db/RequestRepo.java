package com.prs.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prs.model.Request;


public interface RequestRepo extends JpaRepository<Request, Integer> {

	@Query("SELECT MAX(r.requestNumber) FROM Request r")
	String findMaxRequestNumber();
	
	@Query(value = "SELECT * FROM REQUEST WHERE STATUS = 'REVIEW' AND USERID != :userId", nativeQuery =true)
	List<Request> listInReview(@Param("userId") int userId);
	}
	
