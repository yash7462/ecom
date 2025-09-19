package com.ecom.service;


import com.ecom.dao.UserDao;
import com.ecom.modal.CustomUser;
import com.ecom.modal.UserModal;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    @Transactional
    public CustomUser loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModal user = userDao.findByEmailAndIsDeleted(email, 0)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with Email: " + email));
        return CustomUser.build(user);
    }

//    public    List<Permission>getUserRole(List<UUID> roleIds) {
//        List<RolePermission> rolePermission = rolePermissionRepository.findByRoleIdIn(roleIds);
//        List<UUID> permissionId = rolePermission.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
//        List<Permission> permissions = permissionRepository.findAllByIdIn(permissionId);
//        //List<String> authority = permissions.parallelStream().map(Permission::getName).collect(Collectors.toList());
//        return permissions;
//    }

}
