package com.cy.store.service.impl;

import com.cy.store.entity.Address;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.service.IAddressService;
//import com.cy.store.service.IDistrictService;
//import com.cy.store.service.ex.*;
import com.cy.store.service.IDistrictService;
import com.cy.store.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private IDistrictService districtService;

    @Value("${user.address.max-count}")
    private int maxCount;


    @Override
    public void addNewAddress(Integer uid, String username, Address address) {
        // Call the countByUid(Integer uid) method of addressMapper according to the parameter uid to count the number of the current user's delivery address data
        Integer count = addressMapper.countByUid(uid);
        // Determine whether the number reaches the upper limit
        if (count > maxCount) {
            // Yes: Throws AddressCountLimitException
            throw new AddressCountLimitException("The number of shipping addresses has reached the limit(" + maxCount + ")！");
        }


        // Completion data: name of province, city and district
        String provinceName = districtService.getNameByCode(address.getProvinceCode());
        String cityName = districtService.getNameByCode(address.getCityCode());
        String areaName = districtService.getNameByCode(address.getAreaCode());
        address.setProvinceName(provinceName);
        address.setCityName(cityName);
        address.setAreaName(areaName);



        // Completion data: encapsulate the parameter uid into the parameter address
        address.setUid(uid);
        // Completion data: According to the above statistics, get the correct isDefault value (whether it is default: 0-no default, 1-default), and encapsulate it
        Integer isDefault = count == 0 ? 1 : 0;
        address.setIsDefault(isDefault);
        // Completion data: 4 logs
        Date now = new Date();
        address.setCreatedUser(username);
        address.setCreatedTime(now);
        address.setModifiedUser(username);
        address.setModifiedTime(now);

        // Call the insert(Address address) method of addressMapper to insert the shipping address data, and get the returned number of affected rows
        Integer rows = addressMapper.insert(address);
        // Determine whether the number of affected rows is not 1
        if (rows != 1) {
            // yes: throw InsertException
            throw new InsertException("An unknown error occurred while inserting the shipping address data, please contact your system administrator!");
        }
    }

    @Override
    public List<Address> getByUid(Integer uid) {
        List<Address> list = addressMapper.findByUid(uid);

          /*
        for (Address address : list) {
            address.setUid(null);
            address.setProvinceCode(null);
            address.setCityCode(null);
            address.setAreaCode(null);
            address.setCreatedUser(null);
            address.setCreatedTime(null);
            address.setModifiedUser(null);
            address.setModifiedTime(null);
        }*/

        return list;
    }

    @Transactional
    @Override
    public void setDefault(Integer aid, Integer uid, String username) {
        // According to the parameter aid, call findByAid() in addressMapper to query the delivery address data
        Address result = addressMapper.findByAid(aid);
        // Determine if the query result is null
        if (result == null) {
            // yes: throw AddressNotFoundException
            throw new AddressNotFoundException("The shipping address data you are trying to access does not exist.");
        }

        // Determine whether the uid in the query result is inconsistent with the parameter uid (use equals() to judge)
        if (!result.getUid().equals(uid)) {
            // Yes：throw AccessDeniedException
            throw new AccessDeniedException("Illegal Access Exception");
        }

        // Call updateNonDefaultByUid() of addressMapper to set all shipping addresses of the user to non-default, and get and return the number of affected rows.
        Integer rows = addressMapper.updateNonDefaultByUid(uid);
        // Determine whether the number of affected rows is less than 1 (not greater than 0)
        if (rows < 1) {
            // yes：throw UpdateException
            throw new UpdateException("An unknown error occurred while setting the default shipping address[1]");
        }

        // Call updateDefaultByAid() of addressMapper to set the delivery address of the specified aid as the default, and get the returned number of affected rows
        rows = addressMapper.updateDefaultByAid(aid, username, new Date());
        // Determine whether the number of affected rows is not 1
        if (rows != 1) {
            // yes：throw UpdateException
            throw new UpdateException("An unknown error occurred while setting the default shipping address[2]");
        }
    }

    @Transactional
    @Override
    public void delete(Integer aid, Integer uid, String username) {
        // According to the parameter aid, call findByAid() to query the delivery address data
        Address result = addressMapper.findByAid(aid);
        // Determine if the query result is null
        if (result == null) {
            // Yes：throw AddressNotFoundException
            throw new AddressNotFoundException("The shipping address data you are trying to access does not exist.");
        }

        // Determine whether the uid in the query result is inconsistent with the parameter uid (use equals() to judge)
        if (!result.getUid().equals(uid)) {
            // yes：throw AccessDeniedException
            throw new AccessDeniedException("Illegal Access");
        }

        // According to the parameter aid, call deleteByAid() to delete
        Integer rows1 = addressMapper.deleteByAid(aid);
        if (rows1 != 1) {
            throw new DeleteException("An unknown error occurred while deleting the shipping address data, please contact your system administrator.");
        }

        // Determine whether isDefault in the query result is 0
        if (result.getIsDefault() == 0) {
            return;
        }

        // Call countByUid() of the persistence layer to count how many delivery addresses there are currently
        Integer count = addressMapper.countByUid(uid);
        // Determine whether the current number of shipping addresses is 0
        if (count == 0) {
            return;
        }

        // Call findLastModified() to find out the shipping address data recently modified by the user
        Address lastModified = addressMapper.findLastModified(uid);
        // Find the aid attribute value from the above query results
        Integer lastModifiedAid = lastModified.getAid();
        // Call the updateDefaultByAid() method of the persistence layer to set the default shipping address and get the number of affected rows returned.
        Integer rows2 = addressMapper.updateDefaultByAid(lastModifiedAid, username, new Date());
        // Determine whether the number of affected rows is not 1.
        if (rows2 != 1) {
            // yes：throw UpdateException
            throw new UpdateException("An unknown error occurred while updating the shipping address data, please contact your system administrator.");
        }
    }

    @Override
    public Address getByAid(Integer aid, Integer uid) {
        // According to the delivery address data id, query the details of the delivery address
        Address address = addressMapper.findByAid(aid);

        if (address == null) {
            throw new AddressNotFoundException("The shipping address data you are trying to access does not exist.");
        }
        if (!address.getUid().equals(uid)) {
            throw new AccessDeniedException("Illegal Access");
        }
        address.setProvinceCode(null);
        address.setCityCode(null);
        address.setAreaCode(null);
        address.setCreatedUser(null);
        address.setCreatedTime(null);
        address.setModifiedUser(null);
        address.setModifiedTime(null);
        return address;
    }
}
