package com.roberto.field.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.roberto.field.entities.BoundaryEntity;
import com.roberto.field.entities.FieldEntity;

@Repository
public interface FieldDAO extends CrudRepository<FieldEntity, String> {

	
	@Query("select b from BoundaryEntity b where b.id=:boundaryId")
	public BoundaryEntity findBoundaryById(@Param("boundaryId") String boundaryId);
	
	@Transactional
	@Modifying
	@Query("delete from CoordinateEntity c where c.boundary.id=:boundaryId")
	void deleteCoodinatesOfBoundary(@Param("boundaryId") String boundaryId);
}
