package com.cupid.jikting.meeting.entity;

import com.cupid.jikting.common.entity.BaseEntity;
import com.cupid.jikting.member.entity.MemberProfile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE instant_meeting_member SET is_deleted = true WHERE instant_meeting_member_id = ?")
@AttributeOverride(name = "id", column = @Column(name = "instant_meeting_member_id"))
@Entity
public class InstantMeetingMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_profile_id")
    private MemberProfile memberProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instant_meeting_id")
    private InstantMeeting instantMeeting;

    public boolean isSameMemberProfileId(Long memberProfileId) {
        return memberProfile.isSameAs(memberProfileId);
    }

    public static InstantMeetingMember of(InstantMeeting instantMeeting, MemberProfile memberProfile) {
        InstantMeetingMember instantMeetingMember = new InstantMeetingMember(memberProfile, instantMeeting);
        instantMeeting.addMemberProfile(instantMeetingMember);
        memberProfile.addInstantMeeting(instantMeetingMember);
        return instantMeetingMember;
    }
}
