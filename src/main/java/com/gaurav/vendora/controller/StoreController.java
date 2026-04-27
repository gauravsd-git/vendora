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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
@PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN')")
public class StoreController {

    private final StoreService storeService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StoreDto> createStore(@RequestBody StoreDto storeDto, @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(storeService.createStore(storeDto, user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN')")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable Long id, @RequestHeader("Authorization") String jwt) throws Exception {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN')")
    public ResponseEntity<List<StoreDto>> getAllStores(@RequestHeader("Authorization") String jwt) throws Exception {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN')")
    public ResponseEntity<StoreDto> getStoreByAdmin(@RequestHeader("Authorization") String jwt) throws Exception {
        return ResponseEntity.ok(StoreMapper.toDto(storeService.getStoreByAdmin()));
    }

    @GetMapping("/employee")
    @PreAuthorize("hasAnyRole('ADMIN','STORE_ADMIN')")
    public ResponseEntity<StoreDto> getStoreByEmployee(@RequestHeader("Authorization") String jwt) throws Exception {
        return ResponseEntity.ok(storeService.getStoreByEmployee());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StoreDto> updateStore(@PathVariable Long id, @RequestBody StoreDto storeDto) throws Exception {
        return ResponseEntity.ok(storeService.updateStore(id, storeDto));
    }

    @PutMapping("/{id}/moderate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StoreDto> moderateStore(@PathVariable Long id, @RequestParam StoreStatus status) throws Exception {
        return ResponseEntity.ok(storeService.moderateStore(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteStore(@PathVariable Long id) throws Exception {
        storeService.deleteStore(id);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("store deleted successfully");
        return ResponseEntity.ok(apiResponse);
    }
}