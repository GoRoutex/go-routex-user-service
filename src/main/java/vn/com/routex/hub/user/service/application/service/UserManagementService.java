package vn.com.routex.hub.user.service.application.service;

import vn.com.routex.hub.user.service.application.command.user.DeleteUserCommand;
import vn.com.routex.hub.user.service.application.command.user.DeleteUserResult;
import vn.com.routex.hub.user.service.application.command.user.FetchUserDetailQuery;
import vn.com.routex.hub.user.service.application.command.user.FetchUserDetailResult;
import vn.com.routex.hub.user.service.application.command.user.FetchUsersQuery;
import vn.com.routex.hub.user.service.application.command.user.FetchUsersResult;
import vn.com.routex.hub.user.service.application.command.user.SearchUserQuery;
import vn.com.routex.hub.user.service.application.command.user.SearchUserResult;
import vn.com.routex.hub.user.service.application.command.user.UpdateUserCommand;
import vn.com.routex.hub.user.service.application.command.user.UpdateUserResult;

public interface UserManagementService {
    FetchUsersResult fetchUsers(FetchUsersQuery query);

    FetchUserDetailResult fetchUserDetail(FetchUserDetailQuery query);

    SearchUserResult searchUser(SearchUserQuery query);

    UpdateUserResult updateUser(UpdateUserCommand command);

    DeleteUserResult deleteUser(DeleteUserCommand command);
}
