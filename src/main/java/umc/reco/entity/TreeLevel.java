package umc.reco.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TreeLevel {
    SEEDLING("새싹"),
    SAPLING("묘목"),
    TREE("나무"),
    FOREST("숲"),
    EARTH("지구");

    private final String label;

    @Override
    public String toString() {
        return label;
    }
}
