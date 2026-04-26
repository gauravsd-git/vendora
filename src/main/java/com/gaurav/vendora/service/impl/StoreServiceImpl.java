package com.gaurav.vendora.service.impl;

import com.gaurav.vendora.domain.StoreStatus;
import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.mapper.StoreMapper;
import com.gaurav.vendora.model.Store;
import com.gaurav.vendora.model.StoreContact;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.StoreDto;
import com.gaurav.vendora.repository.StoreRepository;
import com.gaurav.vendora.service.StoreService;
import com.gaurav.vendora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserService userService;

    // Create Store
    @Override
    public StoreDto createStore(StoreDto storeDto, User user) {

        Store store = StoreMapper.toEntity(storeDto, user);

        store.setStoreAdmin(user);                 // ensure admin
        store.setStatus(StoreStatus.PENDING);

        Store savedStore = storeRepository.save(store);

        return StoreMapper.toDto(savedStore);
    }

    // Get Store by ID
    @Override
    public StoreDto getStoreById(Long id) throws Exception {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new Exception("Store not found"));

        return StoreMapper.toDto(store);
    }

    // Get All Stores
    @Override
    public List<StoreDto> getAllStores() {

        return storeRepository.findAll()
                .stream()
                .map(StoreMapper::toDto)
                .toList();
    }

    // Get Store by Admin
    @Override
    public StoreDto getStoreByAdmin() throws UserException {

        User admin = userService.getCurrentUser();

        Store store = storeRepository.findByStoreAdminId(admin.getId());

        if (store == null) {
            throw new UserException("No store found for this admin");
        }

        return StoreMapper.toDto(store);
    }

    // Get Store by Employee
    @Override
    public StoreDto getStoreByEmployee() throws UserException {

        User currentUser = userService.getCurrentUser();

        if (currentUser.getStore() == null) {
            throw new UserException("No store assigned to this employee");
        }

        return StoreMapper.toDto(currentUser.getStore());
    }

    // Update Store
    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) throws Exception {

        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new Exception("Store not found"));

        User currentUser = userService.getCurrentUser();

        // Security check
        if (!existingStore.getStoreAdmin().getId().equals(currentUser.getId())) {
            throw new UserException("You are not allowed to update this store");
        }

        // Update fields
        existingStore.setBrand(storeDto.getBrand());
        existingStore.setDescription(storeDto.getDescription());

        if (storeDto.getStoreType() != null) {
            existingStore.setStoreType(storeDto.getStoreType());
        }

        if (storeDto.getContact() != null) {
            StoreContact contact = StoreContact.builder()
                    .address(storeDto.getContact().getAddress())
                    .phone(storeDto.getContact().getPhone())
                    .email(storeDto.getContact().getEmail())
                    .build();

            existingStore.setContact(contact);
        }

        Store updatedStore = storeRepository.save(existingStore);

        return StoreMapper.toDto(updatedStore);
    }

    // Delete Store
    @Override
    public void deleteStore(Long id) throws Exception {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new Exception("Store not found"));

        User currentUser = userService.getCurrentUser();

        // 🔒 Security check
        if (!store.getStoreAdmin().getId().equals(currentUser.getId())) {
            throw new UserException("You are not allowed to delete this store");
        }

        storeRepository.delete(store);
    }

    // Moderate Store (Admin action)
    @Override
    public StoreDto moderateStore(Long id, StoreStatus status) throws Exception {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new Exception("Store not found"));

        store.setStatus(status); // 🔥 Important fix

        Store updatedStore = storeRepository.save(store);

        return StoreMapper.toDto(updatedStore);
    }
}