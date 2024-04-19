package com.example.santa.challege.controller;

import com.example.santa.challege.dto.ChallengeCreateDto;
import com.example.santa.challege.dto.ChallengeResponseDto;
import com.example.santa.challege.entity.Challenge;
import com.example.santa.challege.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @Autowired
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping
    public List<Challenge> getAllChallenges() {
        return challengeService.findAllChallenges();
    }

    @GetMapping("/{id}")
    public ChallengeResponseDto getChallengeById(@PathVariable Long id) {
        ChallengeResponseDto challengeById = challengeService.findChallengeById(id);
        return challengeById;
    }

    @PostMapping
    public ChallengeResponseDto createChallenge(@RequestBody ChallengeCreateDto challengeCreateDto) {
        return challengeService.saveChallenge(challengeCreateDto);
    }

    @PatchMapping("/{id}")
    public ChallengeResponseDto updateChallenge(@PathVariable Long id, @RequestBody ChallengeCreateDto challengeCreateDto) {
        return challengeService.updateChallenge(id, challengeCreateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteChallenge(@PathVariable Long id) {
        challengeService.deleteChallenge(id);
    }
}
