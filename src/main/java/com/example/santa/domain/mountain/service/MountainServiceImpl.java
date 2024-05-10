package com.example.santa.domain.mountain.service;


import com.example.santa.domain.mountain.dto.MountainResponseDto;
import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.mountain.repository.MountainRepository;
import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import com.example.santa.global.util.mapsturct.MountainResponseDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService {
    private final MountainRepository mountainRepository;
    private final MountainResponseDtoMapper mountainResponseDtoMapper;


    @Override
    public Page<MountainResponseDto> findAllMountains(Pageable pageable){
        Page<Mountain> mountains = mountainRepository.findAll(pageable);
        return mountains.map(mountainResponseDtoMapper::toDto);
    }

    @Override
    public MountainResponseDto findMountainById(Long id){
        Mountain mountain= mountainRepository.findById(id)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.MOUNTAIN_NOT_FOUND));
        return mountainResponseDtoMapper.toDto(mountain);
    }
}
