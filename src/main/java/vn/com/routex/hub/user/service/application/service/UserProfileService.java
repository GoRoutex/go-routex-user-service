package vn.com.routex.hub.user.service.application.service;

import vn.com.routex.hub.user.service.application.command.profile.CompleteProfileCommand;
import vn.com.routex.hub.user.service.application.command.profile.CompleteProfileResult;
import vn.com.routex.hub.user.service.application.command.profile.GetMyProfileCommand;
import vn.com.routex.hub.user.service.application.command.profile.GetMyProfileResult;
import vn.com.routex.hub.user.service.application.command.profile.GetUserProfileCommand;
import vn.com.routex.hub.user.service.application.command.profile.GetUserProfileResult;
import vn.com.routex.hub.user.service.application.command.profile.UpdateProfileCommand;
import vn.com.routex.hub.user.service.application.command.profile.UpdateProfileResult;

public interface UserProfileService {

    GetUserProfileResult  getUserProfile(GetUserProfileCommand command);

    GetMyProfileResult getMyProfile(GetMyProfileCommand command);

    CompleteProfileResult completeProfile(CompleteProfileCommand build);

    UpdateProfileResult updateProfile(UpdateProfileCommand command);
}
