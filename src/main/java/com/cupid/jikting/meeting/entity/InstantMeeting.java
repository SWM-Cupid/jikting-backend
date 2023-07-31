package com.cupid.jikting.meeting.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE instant_meeting SET is_deleted = true WHERE instant_meeting_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "instant_meeting_id"))
@Entity
public class InstantMeeting extends BaseEntity {

    private LocalDateTime schedule;
    private String place;
    private int memberCount;

    @Builder.Default
    @OneToMany(mappedBy = "instantMeeting")
    private List<InstantMeetingMember> instantMeetingMembers = new ArrayList<>();

    public boolean isAttend(Long memberProfileId) {
        return instantMeetingMembers.stream()
                .anyMatch(instantMeetingMember -> instantMeetingMember.getMemberProfileId().equals(memberProfileId));
    }
}
