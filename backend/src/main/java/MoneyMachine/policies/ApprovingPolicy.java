package MoneyMachine.policies;

import MoneyMachine.models.User;
import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.exception.NotFoundException;
import MoneyMachine.models.enums.Role;

public class ApprovingPolicy {
    public void enforceApprovingPolicy(User user) {
        enforceUserIsNotNull(user);
        enforceUserIsNotActive(user);
        enforceUserIsNotAuthorizedToCreateAccount(user);
    }

    public void enforceUserIsNotNull(User user) {
        if (user == null) {
            throw new NotFoundException("User Not found");
        }
    }

    public void enforceUserIsNotActive(User user) {
        if (user.getIsActive() == false) {
            throw new NotAuthorizedException("User is not active");
        }
    }

    public void enforceUserIsNotAuthorizedToCreateAccount(User user) {
        if (user.getRole() != Role.USER) {
            throw new NotAuthorizedException("User is not allowed to create account");
        }
    }
}
