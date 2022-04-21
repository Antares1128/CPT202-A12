package com.cy.store.service;
import com.cy.store.entity.User;

/** Business layer interface for processing user data */
public interface IUserService {
    /**
     * User registration
     * @param user User data
     */
    void reg(User user);

    User login(String username, String password);

    /**
     * change the password
     * @param uid The currently logged in user id
     * @param username User name
     * @param oldPassword Old password
     * @param newPassword New password
     */
    void changePassword(Integer uid, String username, String oldPassword, String newPassword);

    /**
     * Get the information of the currently logged in user
     * @param uid The id of the currently logged in user
     * @return The data of the currently logged in user
     */
    User getByUid(Integer uid);
    /**
     * Edit user profile
     * @param uid The id of the currently logged in user
     * @param username The name of the currently logged in user
     * @param user User's new data
     */
    void changeInfo(Integer uid, String username, User user);

    /**
     * Modify the user's avatar
     * @param uid User id
     * @param avatar Path to user avatar
     * @param username User name
     */
    void changeAvatar(Integer uid, String avatar, String username);

}
