package com.cy.store.mapper;

import com.cy.store.entity.District;

import java.util.List;

public interface DistrictMapper {
    /**
     * Search area information based on parent
     * @param parent
     * @return area information
     */
    List<District> findByParent(String parent);

    String findNameByCode(String code);
}
