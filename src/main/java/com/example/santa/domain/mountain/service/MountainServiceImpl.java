package com.example.santa.domain.mountain.service;

import com.example.santa.domain.mountain.repository.MountainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MountainServiceImpl {

    private final MountainRepository mountainRepository;


}
