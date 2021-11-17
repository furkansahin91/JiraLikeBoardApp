package com.app.user.service;

import com.app.user.entity.Customer;
import com.app.user.exception.FailedAttemptsException;
import com.app.user.exception.NotFoundException;
import com.app.user.model.DeleteOrphanedTaskRequest;
import com.app.user.model.DeletedUserData;
import com.app.user.model.UserResponse;
import com.app.user.repository.CustomerRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();

    public void saveUser(Customer user) {
        userRepository.save(user);
    }

    public void deleteUser(String id) {
        Optional<Customer> user = userRepository.findById(id);
        IMap<String, Integer> iMap = hzInstance.getMap("key");
        Integer retryCount = iMap.get("retryCount") != null ? iMap.get("retryCount") : 0;
        if (retryCount >= 5) {
            throw new FailedAttemptsException("Too many failed attempts!", id, "id");
        }
        if (user.isEmpty()) {
            iMap.put("retryCount", ++retryCount, 15, TimeUnit.MINUTES);
            throw new NotFoundException("User not found", id, "id");
        }

        sendDeletedUserDataToWebhook(id);
        userRepository.deleteById(id);
    }

    public void fetchUsers() {
        ResponseEntity<UserResponse> userResponseResponseEntity = restTemplate.getForEntity("https://randomuser.me/api/?results=100", UserResponse.class);
        UserResponse userResponse = userResponseResponseEntity.getBody();
        System.out.println("results" + userResponse.getInfo().getResults());

        CollectionUtils.emptyIfNull(userResponse.getResults()).stream().
                //save users to the db
                        forEach(i -> saveUser(new Customer(i.getLogin().getUuid())));
    }

    public void sendDeletedUserDataToWebhook(String id) {
        DeleteOrphanedTaskRequest request = new DeleteOrphanedTaskRequest();
        DeletedUserData userData = new DeletedUserData();
        userData.setUser(id);
        request.setTime(LocalDateTime.now().toString());
        request.setData(userData);
        restTemplate.postForObject("http://localhost:8081/webhooks/user-deleted", request, DeleteOrphanedTaskRequest.class);
    }

    public void getUser(String id) {
        Optional<Customer> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("user not found", id, "id");
        }
    }
}
