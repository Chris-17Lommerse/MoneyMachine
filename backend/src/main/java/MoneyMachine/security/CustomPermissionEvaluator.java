package MoneyMachine.security;

import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    //private final ReviewRepository reviewRepository;

    public CustomPermissionEvaluator() {//ReviewRepository reviewRepository) {
        //this.reviewRepository = reviewRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    // Spring calls this overload when you write expressions like:
    // @PreAuthorize("hasPermission(#id, 'Review', 'update')")
    //
    // Parameters:
    // - authentication: who is making the request (current logged-in user + roles).
    // - targetId: the ID of the domain object being accessed (here: review id).
    // - targetType: the domain type name from the expression (here: "Review").
    // - permission: the action being requested (for example: "update" or "delete").
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {

        return true;
        // if (authentication == null || targetId == null || targetType == null || permission == null) {
        //     return false;
        // }

        // if (!(authentication.getPrincipal() instanceof User currentUser)) {
        //     return false;
        // }

        // if (isAdmin(authentication)) {
        //     return true;
        // }

        // if (!"review".equalsIgnoreCase(targetType)) {
        //     return false;
        // }

        // String permissionName = permission.toString().toLowerCase();
        // if (!"update".equals(permissionName) && !"delete".equals(permissionName)) {
        //     return false;
        // }

        // Long reviewId = Long.valueOf(targetId.toString());
        // return reviewRepository.findById(reviewId)
        //         .map(Review::getUser)
        //         .map(User::getId)
        //         .filter(ownerId -> ownerId.equals(currentUser.getId()))
        //         .isPresent();
    }

    // private boolean isAdmin(Authentication authentication) {
    //     return authentication.getAuthorities().stream()
    //             .map(GrantedAuthority::getAuthority)
    //             .anyMatch("ROLE_ADMIN"::equals);
    // }
}
