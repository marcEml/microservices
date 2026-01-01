package com.example.datasetManagerService.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attribute_id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AttributeType type;

    //  Contraintes simples
    private Integer min;      // INTEGER, DOUBLE, DATE (timestamp)
    private Integer max;

    private Integer length;   // STRING

    // @ManyToOne
    // @JoinColumn(name = "entity_id")
    // private EntityModel entityModel;
    @ManyToOne
    @JoinColumn(name = "entity_id")
    @JsonBackReference("entity-attribute")
    private EntityModel entityModel;

    public enum AttributeType {
        STRING,
        INTEGER,
        DOUBLE,
        BOOLEAN,
        DATE;

        // Méthode utilitaire pour convertir une String en AttributeType
        public static AttributeType fromString(String input) {
            if (input == null) {
                return null;
            }
            return AttributeType.valueOf(input.toUpperCase());
        }
    }
}
