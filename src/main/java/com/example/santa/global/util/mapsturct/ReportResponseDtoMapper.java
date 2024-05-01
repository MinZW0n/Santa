package com.example.santa.global.util.mapsturct;

import com.example.santa.domain.report.dto.ReportResponseDto;
import com.example.santa.domain.report.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportResponseDtoMapper extends EntityMapper<ReportResponseDto, Report> {

    @Mapping(source = "reporter.id", target = "reporterId")
    @Mapping(source = "reporter.name", target = "reporterName")
    @Mapping(source = "reporter.nickname", target = "reporterNickname")
    @Mapping(source = "reportedParticipant.id", target = "reportedParticipantId")
    @Mapping(source = "reportedParticipant.name", target = "reportedParticipantName")
    @Mapping(source = "reportedParticipant.nickname", target = "reportedParticipantNickname")
    ReportResponseDto toDto(Report report);
}
