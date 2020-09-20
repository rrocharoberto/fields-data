package com.roberto.field.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.roberto.field.entities.FieldEntity;

@Repository
public interface FieldDAO extends CrudRepository<FieldEntity, String> {

}
