package com.gaurav.vendora.service;

import com.gaurav.vendora.domain.StoreStatus;
import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.StoreDto;

import java.util.List;

public interface StoreService {

    StoreDto createStore(StoreDto storeDto, User user);
    StoreDto getStoreById(Long id) throws Exception;
    List<StoreDto> getAllStores();
    StoreDto getStoreByAdmin() throws UserException;
    StoreDto updateStore(Long id,StoreDto storeDto) throws Exception;
    void deleteStore(Long id) throws Exception;
    StoreDto getStoreByEmployee() throws UserException;

    StoreDto moderateStore(Long id, StoreStatus status) throws Exception;
}
