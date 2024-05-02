package br.com.fiap.pizzaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "TB_OPCIONAL")
public class Opcional {

    @Id
    @SequenceGenerator(name = "SQ_OPCIONAL", sequenceName = "SQ_OPCIONAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_OPCIONAL")
    @Column(name = "ID_OPCIONAL")
    private Long id;

    @Column(name = "NM_OPCIONAL")
    private String nome;

    @Column(name = "PRECO_OPCIONAL")
    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(
            name = "SABOR",
            referencedColumnName = "ID_SABOR",
            foreignKey = @ForeignKey(name = "FK_SABOR_OPCIONAL")
    )
    private Sabor sabor;

}
