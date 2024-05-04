package com.example.santa.domain.report.dto;

import com.example.santa.domain.user.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {

    private String reason;

    private Long reportedParticipantId;

}
