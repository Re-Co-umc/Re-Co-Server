package umc.reco.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.reco.entity.Tree;
import umc.reco.entity.TreeLevel;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreeResponseDto {

    private Long id;
    private String member;
    private double total_ml;
    private double point;
    private TreeLevel treeLevel;


    public TreeResponseDto(Tree tree){
        this.id = tree.getId();
        this.member = tree.getMember().getEmail();
        this.total_ml = tree.getTotal_ml();
        this.treeLevel=tree.getTreelevel();
    }

}
