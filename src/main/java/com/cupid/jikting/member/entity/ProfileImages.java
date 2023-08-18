package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.dto.ImageRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ProfileImages {

    private static final String DEFAULT_IMAGE_URL = "https://cupid-images.s3.ap-northeast-2.amazonaws.com/default.png";

    @OneToMany(mappedBy = "memberProfile", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ProfileImage> profileImages = new ArrayList<>();

    public void createDefaultImages(MemberProfile memberProfile) {
        profileImages = Stream.of(Sequence.MAIN, Sequence.FIRST, Sequence.SECOND)
                .map(sequence -> ProfileImage.builder()
                        .memberProfile(memberProfile)
                        .url(DEFAULT_IMAGE_URL)
                        .sequence(sequence)
                        .build())
                .collect(Collectors.toList());
    }

    public void update(List<ImageRequest> imageRequests) {
        imageRequests.forEach(updateProfileImage -> {
            ProfileImage profileImage = findBySequence(updateProfileImage.getSequence());
            profileImage.update(updateProfileImage.getUrl());
        });
    }

    public List<ProfileImage> getProfileImages() {
        return Collections.unmodifiableList(profileImages);
    }

    public String getMainImageUrl() {
        return profileImages.stream()
                .filter(ProfileImage::isMain)
                .map(ProfileImage::getUrl)
                .findAny()
                .orElseThrow(() -> new NotFoundException(ApplicationError.NOT_EXIST_REGISTERED_IMAGES));
    }

    private ProfileImage findBySequence(String sequence) {
        return profileImages.stream()
                .filter(profileImage -> profileImage.isSameSequence(sequence))
                .findAny()
                .orElseThrow(() -> new NotFoundException(ApplicationError.PROFILE_IMAGE_NOT_FOUND));
    }
}
