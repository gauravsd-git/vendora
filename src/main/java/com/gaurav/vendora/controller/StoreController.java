package com.gaurav.vendora.controller;

import com.gaurav.vendora.domain.StoreStatus;
import com.gaurav.vendora.exceptions.UserException;
import com.gaurav.vendora.mapper.StoreMapper;
import com.gaurav.vendora.model.User;
import com.gaurav.vendora.payload.dto.StoreDto;
import com.gaurav.vendora.payload.response.ApiResponse;
import com.gaurav.vendora.service.StoreService;
import com.gaurav.vendora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    // to create new store
    @PostMapping
    public ResponseEntity<StoreDto> createStore(@RequestBody StoreDto storeDto, @RequestHeader("Authorization")String jwt) throws UserException {

        User user = userService.getUserFromJwtToken(jwt);
        return ResponseEntity.ok(storeService.createStore(storeDto, user));
    }

    // to get store by - id
    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStoreById(
            @PathVariable Long id,
            @RequestHeader("Authorization")String jwt) throws Exception {

        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    // to list all stores
    @GetMapping()
    public ResponseEntity<List<StoreDto>> getAllStores(
            @RequestHeader("Authorization")String jwt) throws Exception {
        return ResponseEntity.ok(storeService.getAllStores());
    }


    // to get store by admin
    @GetMapping("/admin")
    public ResponseEntity<StoreDto> getStoreByAdmin(
            @RequestHeader("Authorization")String jwt) throws Exception {
        return ResponseEntity.ok(storeService.getStoreByAdmin());
    }


    // to get store by employee
    @GetMapping("/employee")
    public ResponseEntity<StoreDto> getStoreByEmployee(
            @RequestHeader("Authorization")String jwt) throws Exception {
        return ResponseEntity.ok(storeService.getStoreByEmployee());
    }

    // to update store by id
    @PutMapping("/{id}")
    public ResponseEntity<StoreDto> updateStore(
            @PathVariable Long id,
            @RequestBody StoreDto storeDto) throws Exception {
        return ResponseEntity.ok(storeService.updateStore(id,storeDto));
    }

    // to moderate store by id
    @PutMapping("/{id}/moderate")
    public ResponseEntity<StoreDto> moderateStore(
            @PathVariable Long id,
            @RequestParam StoreStatus status) throws Exception {
        return ResponseEntity.ok(storeService.moderateStore(id,status));
    }

    // to delete store by id
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStore(
            @PathVariable Long id) throws Exception {

        storeService.deleteStore(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("store deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }





}
