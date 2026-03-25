package vn.com.routex.hub.user.service.application.service;

import vn.com.routex.hub.user.service.application.dto.membership.GetMyMembershipCommand;
import vn.com.routex.hub.user.service.application.dto.membership.GetMyMembershipResult;

public interface MembershipProfileService {


    GetMyMembershipResult getMyMembership(GetMyMembershipCommand command);
}
