package net.cobaltium.magico.structures;

import java.util.Optional;

public enum StructureType {
    MANACRYSTAL(0),
    BOOKCASE(1);

    private int structureId;

    StructureType(int structureId) {
        this.structureId = structureId;
    }

    public static Optional<StructureType> getById(int id) {
        for (StructureType type : values()) {
            if (type.structureId == id) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public int getStructureId() {
        return structureId;
    }
}
