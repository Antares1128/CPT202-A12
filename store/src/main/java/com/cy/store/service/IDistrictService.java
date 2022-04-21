package com.cy.store.service;

import com.cy.store.entity.District;

import java.util.List;

public interface IDistrictService {
    /**
     * Search area information by parent code
     * @param parent parent
     * @return
     */
    List<District> getByParent(String parent);

     String getNameByCode(String code);
}
