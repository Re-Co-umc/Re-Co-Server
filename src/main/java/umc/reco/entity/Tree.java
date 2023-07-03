package umc.reco.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Tree extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tree_id")
    @JsonIgnore
    private long id;

    //총용량
    @Column
    private double total_ml;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    //포인트,환급액
    @Column
    private double point;


    //나무 등급
    private TreeLevel treelevel;



    //엔티티의 로딩이 완료된 후에 실행된다. 이 메서드에서 total_ml의 값을 기반으로 treelevel을 설정한다
    //Tree 엔티티를 조회할 때마다 이 메서드가 호출되어 total_ml값에 따라 treelevel이 자동으로 설정된다
    @PostLoad
    public void setTreeLevelByTotalML() {
        if (total_ml >= 40000) {
            treelevel = TreeLevel.EARTH;
        } else if (total_ml >= 20000) {
            treelevel = TreeLevel.FOREST;
        } else if (total_ml >= 10000) {
            treelevel = TreeLevel.TREE;
        } else if (total_ml >= 4000) {
            treelevel = TreeLevel.SAPLING;
        } else if (total_ml >= 1000){
            treelevel = TreeLevel.SEEDLING;
        } else{
            treelevel = TreeLevel.SEED;
        }
    }


}
