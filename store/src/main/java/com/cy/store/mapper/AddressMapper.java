package com.cy.store.mapper;

import com.cy.store.entity.Address;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/** handle the persistence layer interface of the shipping address data */
public interface AddressMapper {
    /**
     * Insert shipping address data
     * @param address Shipping address data
     * @return Number of rows affected
     */
    Integer insert(Address address);

    /**
     * Count the number of delivery address data of a user
     * @param uid User id
     * @return The number of shipping address data for this user
     */
    Integer countByUid(Integer uid);

    /**
     * Query a user's shipping address list data
     * @param uid The user id to which the shipping address belongs
     * @return The user's shipping address list data
     */
    List<Address> findByUid(Integer uid);


    /**
     * Set all shipping addresses for a user to non-default addresses
     * @param uid The user id to which the shipping address belongs
     * @return Number of rows affected
     */
    Integer updateNonDefaultByUid(Integer uid);

    /**
     * Set the specified shipping address as the default address
     * @param aid Delivery address id
     * @param modifiedUser Modify the user
     * @param modifiedTime Modify the time
     * @return Number of rows affected
     */
    Integer updateDefaultByAid(
            @Param("aid") Integer aid,
            @Param("modifiedUser") String modifiedUser,
            @Param("modifiedTime") Date modifiedTime);

    /**
     * Query the details of the delivery address according to the aid value of the delivery address
     * @param aid Delivery address id
     * @return The matched delivery address details, or return null if there is no matching data
     */
    Address findByAid(Integer aid);

    /**
     * Delete data based on delivery address id
     * @param aid Delivery address id
     * @return Number of rows affected
     */
    Integer deleteByAid(Integer aid);

    /**
     * Query the shipping address last modified by a user
     * @param uid Attribution user id
     * @return The shipping address last modified by the user, or return null if the user has no shipping address data
     */
    Address findLastModified(Integer uid);



}
