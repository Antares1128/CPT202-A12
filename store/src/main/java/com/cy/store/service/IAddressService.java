package com.cy.store.service;

import com.cy.store.entity.Address;

import java.util.List;

/** Business layer interface for processing shipping address data */
public interface IAddressService {
    /**
     * Create a new shipping address
     * @param uid The id of the currently logged in user
     * @param username The name of the currently logged in user
     * @param address Shipping address data submitted by the user
     */
    void addNewAddress(Integer uid, String username, Address address);

    /**
     * Query a user's shipping address list data
     * @param uid The user id to which the shipping address belongs
     * @return The user's shipping address list data
     */
    List<Address> getByUid(Integer uid);

    /**
     * Set default shipping address
     * @param aid Delivery address id
     * @param uid Attribution user id
     * @param username The name of the currently logged in user
     */
    void setDefault(Integer aid, Integer uid, String username);

    /**
     * delete shipping address
     * @param aid Delivery address id
     * @param uid Attribution user id
     * @param username The name of the currently logged in user
     */
    void delete(Integer aid, Integer uid, String username);

    /**
     * According to the id of the delivery address data, query the details of the delivery address
     * @param aid Delivery address id
     * @param uid Attribution user id
     * @return The matching shipping address details
     */
    Address getByAid(Integer aid, Integer uid);
}











