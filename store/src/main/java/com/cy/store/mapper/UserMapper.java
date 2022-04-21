package com.cy.store.mapper;
import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
/** Persistence layer interface for handling user data operations */
@Repository
public interface UserMapper {
        Integer insert(User user);
        User findByUsername(String username);

        /**
         * Update user's password based on uid
         * @param uid User id
         * @param password New password
         * @param modifiedUser Last modified user
         * @param modifiedTime Last modified time
         * @return Number of rows affected
         */
        Integer updatePasswordByUid(
                @Param("uid") Integer uid,
                @Param("password") String password,
                @Param("modifiedUser") String modifiedUser,
                @Param("modifiedTime") Date modifiedTime);
        /**
         * Query user data based on user id
         * @param uid User id
         * @return The matching user data, or null if there is no matching user data
         */
        User findByUid(Integer uid);

        /**
         * Update user profile based on uid
         * @param user An object that encapsulates the user id and new profile
         * @return Number of rows affected
         */
        Integer updateInfoByUid(User user);

        Integer updateAvatarByUid(@Param("uid") Integer uid, @Param("avatar") String avatar,
                                  @Param("modifiedUser") String modifiedUser,
                                  @Param("modifiedTime") Date modifiedTime);


}
