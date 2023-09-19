package com.khali.api3.controllers;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khali.api3.domain.user.User;
import com.khali.api3.domain.user.UserType;
import com.khali.api3.domain.permission.Permission;
import com.khali.api3.repositories.UserRepository;
import com.khali.api3.services.MembersService;
import com.khali.api3.services.ResultCenterService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired private final MembersService membersService;
    @Autowired private final ResultCenterService resultCenterService;

    @Autowired
    public UserController(
        UserRepository userRepository,
        MembersService membersService,
        ResultCenterService resultCenterService
    ) {
        this.userRepository = userRepository;
        this.membersService = membersService;
        this.resultCenterService = resultCenterService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @GetMapping("/{id}/permissions")
    public List<Permission> getUserPermissions(@PathVariable Long id) {
        List<Permission> permissions = new ArrayList<Permission>();
        System.out.println("user not found");
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        System.out.println("user found");
        if (user.getUserType().equals(UserType.Admin)) {
            permissions.add(Permission.FullAccess);
            permissions.add(Permission.Register);
            permissions.add(Permission.Report);
        }
        if (membersService.getMembersByUser(user).size() > 0) permissions.add(Permission.Appoint);
        if (resultCenterService.findByGestorID(id).size() > 0) permissions.add(Permission.Validate);
        return permissions;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setActive(true);
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        user.setRegistration(userDetails.getRegistration());
        user.setName(userDetails.getName());
        user.setUserType(userDetails.getUserType());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setActive(userDetails.getActive());

        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
