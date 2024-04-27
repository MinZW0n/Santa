package com.example.santa.domain.meeting.dto;

import com.example.santa.domain.meeting.entity.Participant;
import com.example.santa.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private Long userId;
    private String userName;
    private String userImage;


}
