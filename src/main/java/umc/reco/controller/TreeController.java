package umc.reco.controller;

import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.reco.dto.response.TreeResponseDto;
import umc.reco.entity.Member;
import umc.reco.entity.Tree;
import umc.reco.service.TreeService;
import umc.reco.util.UserUtil;

@RestController
@RequestMapping("/tree")
@RequiredArgsConstructor
public class TreeController {

    //Get /tree하면 tree Info가져오는것
    private final UserUtil userUtil;
    private final TreeService treeService;

    @GetMapping
    public ResponseEntity<TreeResponseDto> getTreeInfo(){
        Member loggedInMember = userUtil.getLoggedInMember();

        //로그인한 회원에 대한 처리
        if(loggedInMember==null){
            //로그인되지 않은 상태 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Tree tree = treeService.getTreeByMember(loggedInMember);
        if(tree==null){
            //tree가 없는 경우 처리
            return ResponseEntity.notFound().build();
        }

        // TreeInfo 객체 생성 및 반환
        TreeResponseDto treeInfo = TreeResponseDto.builder()
                .id(tree.getId())
                .total_ml(tree.getTotal_ml())
                .point(tree.getPoint())
                .treeLevel(tree.getTreelevel())
                .build();

        return ResponseEntity.ok(treeInfo);

    }






}
