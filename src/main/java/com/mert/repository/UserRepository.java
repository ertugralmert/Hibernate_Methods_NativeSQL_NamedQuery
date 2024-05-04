package com.mert.repository;

import com.mert.entity.User;
import com.mert.utility.Repository;

public class UserRepository extends Repository<User, Long> {

    public UserRepository(){
        super(new User());
    }
}
