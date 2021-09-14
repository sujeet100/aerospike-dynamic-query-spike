package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.request.SearchParams;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Singleton
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(SearchParams searchParams) {
        if (searchParams.containsPrimary()) {
            User user = userRepository.getById(searchParams);
            return user != null ? List.of(user) : Collections.emptyList();
        } else if (searchParams.containsMobileNumber()) {
            // call mapping table repo and get list of ucics
            // call userRepository.getByIds(list(ids), searchParams);

        } else if (searchParams.containsSecondaryIndex()) {
            return userRepository.getByFilterAndExpression(searchParams);
        }
        return Collections.emptyList();
    }

    public void save() {
        userRepository.save();
    }
}
