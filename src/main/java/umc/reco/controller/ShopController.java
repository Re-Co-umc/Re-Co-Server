package umc.reco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.reco.dto.request.MlDto;
import umc.reco.dto.request.ShopDto;
import umc.reco.dto.response.MemberAndShopResponseDto;
import umc.reco.entity.Member;
import umc.reco.entity.Tree;
import umc.reco.exception.ExceptionResponse;
import umc.reco.exception.NotQualifiedDtoException;
import umc.reco.exception.TargetNotFoundException;
import umc.reco.service.ShopService;
import umc.reco.service.TreeService;
import umc.reco.util.UserUtil;

@RestController
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;


    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }


    @PostMapping
    public ResponseEntity<?> createShop(@RequestBody ShopDto shopDto) {
        try {
            return ResponseEntity.ok(shopService.createShop(shopDto));
        } catch (NotQualifiedDtoException e) {
            return errorMessage(e.getMessage());
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeShop(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(shopService.like(id));
        } catch (TargetNotFoundException e) {
            return errorMessage(e.getMessage());
        }
    }

    @PostMapping("/{id}/ml")
    public ResponseEntity<?> addMl(@PathVariable Long id,@RequestBody MlDto mlDto){
        try{
            MemberAndShopResponseDto updatedTree = shopService.addMl(id, mlDto);
            return ResponseEntity.ok(updatedTree);
        } catch (TargetNotFoundException e){
            return errorMessage(e.getMessage());
        }
    }

    private static ResponseEntity<ExceptionResponse> errorMessage(String message) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(message));
    }
}
