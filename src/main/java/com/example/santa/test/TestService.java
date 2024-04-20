//<<<<<<<< HEAD:src/main/java/com/example/santa/test/TestService.java
//package com.example.santa.test;
//
//import com.example.santa.Mountain;
//========
//package com.example.santa.domain.mountain.service;
//
//import com.example.santa.domain.mountain.dto.TestDto;
//import com.example.santa.domain.mountain.entity.Mountain;
//import com.example.santa.domain.mountain.repository.TestRepository;
//>>>>>>>> feature/login:src/main/java/com/example/santa/domain/mountain/service/TestService.java
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TestService {
//
//    private final TestRepository testRepository;
//
//    public TestService(TestRepository testRepository) {
//        this.testRepository = testRepository;
//    }
//
//    public List<Mountain> getMountains() {
//        return testRepository.findAll();
//    }
//
//    public Mountain add(TestDto testDto){
//        Mountain mountain = new Mountain();
//        mountain.setName(testDto.getName());
//        mountain.setLocation(testDto.getLocation());
//        mountain.setHeight(testDto.getHeight());
//        mountain.setLatitude(testDto.getLatitude());
//        mountain.setLongitude(testDto.getLongitude());
//
//        return testRepository.save(mountain);
//    }
//}
