package com.cy.store.service.impl;
import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

/** Business layer implementation class for processing user data */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        // Get the registered user name according to the parameter user object
        String username = user.getUsername();
        // Call the User findByUsername(String username) method of the persistence layer to query user data according to the username
        User result = userMapper.findByUsername(username);
        // Check if the query result is not null
        if (result != null) {
            // Yes: Indicates that the user name is already occupied, throw UsernameDuplicateException
            throw new UsernameDuplicateException("Username trying to register[" + username + "]is already taken");
        }
        // Create current time object
        Date now = new Date();
        // Implementation of password encryption processing: the form of the MD5 algorithm:873uhdjcsjji82-sksjdn7827
        // (string + password + string) ----The md5 algorithm is encrypted and loaded three times in a row
        // salt + password + salt ----The salt value is a random string
        String oldPassword = user.getPassword();
        // Get the salt value (randomly generate a salt value)
        String salt = UUID.randomUUID().toString().toUpperCase();
        // Completion data: record of salt value
        user.setSalt(salt);
        // Encrypt the password and salt value as a whole, ignoring the strength of the original password to improve data security
        String md5Password = getMd5Password(user.getPassword(), salt);
        // Re-complete the encrypted password to the user object
        user.setPassword(md5Password);

        // Completion data：isDelete(0)
        user.setIsDelete(0);
        // Completion data: 4 log attributes
        user.setCreatedUser(username);
        user.setCreatedTime(now);
        user.setModifiedUser(username);
        user.setModifiedTime(now);
        // Indicates that the user name is not occupied, then registration is allowed
        // Call the Integer insert(User user) method of the persistence layer, perform registration and get the return value (number of affected rows)
        Integer rows = userMapper.insert(user);
        // Determine whether the number of affected rows is not 1
        if (rows != 1) {
            // Yes: there is some kind of error when inserting data, then InsertException is thrown
            throw new InsertException("An unknown error occurred while adding user data, please contact your system administrator");
        }
    }

    @Override
    public User login(String username, String password) {
        // Call the findByUsername() method of userMapper to query user data according to the parameter username
        User result = userMapper.findByUsername(username);
        // Determine if the query result is null
        if (result == null) {
            // yes: throw UserNotFoundException
            throw new UserNotFoundException("wrong user name or password");
        }
        // Determine whether isDelete in the query result is 1
        if (result.getIsDelete() == 1) {
            // yes: throw UserNotFoundException
            throw new UserNotFoundException("User data does not exist error");
        }
        // Get salt value from query result
        String oldPassword = result.getPassword();
        String salt = result.getSalt();
        // Call the getMd5Password() method to combine the parameters password and salt for encryption
        String md5Password = getMd5Password(password, salt);
        // Determine whether the password in the query result is inconsistent with the password obtained by the above encryption
        if (!result.getPassword().equals(md5Password)) {
            // yes: throw PasswordNotMatchException
            throw new PasswordNotMatchException("Password verification failed error");
        }
        // Create a new User object
        User user = new User();
        // Encapsulate the uid, username, and avatar in the query result into a new user object
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        // return the new user object


        return user;
    }

    @Override
    public void changePassword(Integer uid, String username, String oldPassword, String newPassword) {

        User result = userMapper.findByUid(uid);
        if (result == null || result.getIsDelete() == 1){
            throw new UserNotFoundException("User data does not exist.");
        }

        // Compare the original password with the password in the database
        String oldMd5Password = getMd5Password(oldPassword,result.getSalt());
        if (!result.getPassword().equals(oldMd5Password)){
            throw new PasswordNotMatchException("wrong password");
        }

        // Set the new password to the database and encrypt the new password
        // Encrypt the parameter newPassword with the salt value to get newMd5Password
        String newMd5Password = getMd5Password(newPassword, result.getSalt());
        // Create current time object
        Date now = new Date();
        // Call updatePasswordByUid() of userMapper to update the password and get the return value
        Integer rows = userMapper.updatePasswordByUid(uid, newMd5Password, username,
                now);
        // Determine whether the number of affected rows returned by the above is not 1
        if (rows != 1) {
            // yes: throw UpdateException
            throw new UpdateException("An unknown error occurred while updating user data, please contact your system administrator.");
        }
    }

    @Override
    public User getByUid(Integer uid) {
        // Call the findByUid() method of userMapper to query user data according to the parameter uid
        User result = userMapper.findByUid(uid);
        // Determine if the query result is null
        if (result == null) {
            // yes: throw UserNotFoundException
            throw new UserNotFoundException("User data does not exist.");
        }
        // Determine whether isDelete in the query result is 1
        if (result.getIsDelete().equals(1)) {
            // yes: throw UserNotFoundException
            throw new UserNotFoundException("User data does not exist.");
        }
        // Create a new User object
        User user = new User();
        // Encapsulate the username/phone/email/gender in the above query result into a new User object
        user.setUsername(result.getUsername());
        user.setPhone(result.getPhone());
        user.setEmail(result.getEmail());
        user.setGender(result.getGender());
        // Return a new User object
        return user;
    }

    @Override
    public void changeInfo(Integer uid, String username, User user) {
        // Call the findByUid() method of userMapper to query user data according to the parameter uid
        User result = userMapper.findByUid(uid);
        // Determine if the query result is null
        if (result == null) {
            // yes: throw UserNotFoundException
            throw new UserNotFoundException("User data does not exist.");
        }
        // Determine whether isDelete in the query result is 1
        if (result.getIsDelete().equals(1)) {
            // YES：throw UserNotFoundException
            throw new UserNotFoundException("User data does not exist.");
        }
        // Complete data to the parameter user: uid
        user.setUid(uid);
        // Complete data to the parameter user: uid：modifiedUser(username)
        user.setModifiedUser(username);
        // Complete data to the parameter user: uid：modifiedTime(new Date())
        user.setModifiedTime(new Date());
        // Call the updateInfoByUid(User user) method of userMapper to perform the modification and get the return value
        Integer rows = userMapper.updateInfoByUid(user);
        // Determine whether the number of affected rows returned by the above is not 1
        if (rows != 1) {
            // yes：throw UpdateException
            throw new UpdateException("An unknown error occurred while updating user data, please contact your system administrator.");
        }


    }

    @Override
    public void changeAvatar(Integer uid, String avatar, String username) {
        //Query whether the current user data exists
        User result = userMapper.findByUid(uid);
        if (result == null) {
            // yes：throw UserNotFoundException
            throw new UserNotFoundException("User data does not exist.");
        }
        // Determine whether isDelete in the query result is 1
        if (result.getIsDelete().equals(1)) {
            // yes：throw UserNotFoundException
            throw new UserNotFoundException("User data does not exist.");
        }

        Integer rows = userMapper.updateAvatarByUid(uid, avatar,username,new Date());
        if(rows != 1){
            throw new UpdateException("An unknown exception occurred when updating the user avatar!");
        }


    }

    /** Define an encryption process for the md5 algorithm*/
    private String getMd5Password(String password, String salt) {
        /*
         * Encryption rules:
         * 1. Ignore the strength of the original password
         * 2. Use UUID as the salt value, splicing on the left and right sides of the original password
         * 3. Cyclic encryption 3 times
         */
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }
}

