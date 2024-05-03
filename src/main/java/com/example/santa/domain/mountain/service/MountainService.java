package com.example.santa.domain.mountain.service;


import com.example.santa.domain.mountain.dto.MountainResponseDto;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface MountainService {
    Page<MountainResponseDto> findAllMountains(Pageable pageable);
    MountainResponseDto findMountainById(Long id);


}
