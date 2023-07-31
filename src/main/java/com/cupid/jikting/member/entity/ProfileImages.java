package com.cupid.jikting.member.entity;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ProfileImages {

    @OneToMany(mappedBy = "memberProfile", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ProfileImage> profileImages = new ArrayList<>();

    public void update(List<ProfileImage> profileImages) {
        if (this.profileImages.isEmpty()) {
            this.profileImages.addAll(profileImages);
            return;
        }
        profileImages.forEach(updateProfileImage -> {
            ProfileImage profileImage = findById(updateProfileImage.getId());
            profileImage.update(updateProfileImage.getUrl(), updateProfileImage.getSequence());
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

    private ProfileImage findById(Long profileImageId) {
        return profileImages.stream()
                .filter(profileImage -> profileImage.isSameAs(profileImageId))
                .findAny()
                .orElseThrow(() -> new NotFoundException(ApplicationError.PROFILE_IMAGE_NOT_FOUND));
    }
}
