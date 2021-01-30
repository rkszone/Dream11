package com.web.dream11.repository;

import com.web.dream11.models.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmailAndPassword(String user, String password);
    User findByEmail(String email);
    User findById(int id);
}
