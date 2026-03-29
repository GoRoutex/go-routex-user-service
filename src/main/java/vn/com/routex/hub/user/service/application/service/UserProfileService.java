package vn.com.routex.hub.user.service.application.service;

import vn.com.routex.hub.user.service.application.dto.profile.CompleteProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.CompleteProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetMyProfileResult;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileCommand;
import vn.com.routex.hub.user.service.application.dto.profile.GetUserProfileResult;

public interface UserProfileService {

    GetUserProfileResult  getUserProfile(GetUserProfileCommand command);

    GetMyProfileResult getMyProfile(GetMyProfileCommand command);

    CompleteProfileResult completeProfile(CompleteProfileCommand build);
}
