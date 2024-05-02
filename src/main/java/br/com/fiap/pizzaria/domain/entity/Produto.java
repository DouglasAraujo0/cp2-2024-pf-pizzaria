package br.com.fiap.pizzaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "TB_PRODUTO")
public class Produto {
    @Id
    @SequenceGenerator(name = "SQ_PRODUTO", sequenceName = "SQ_PRODUTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PRODUTO")
    @Column(name = "ID_PRODUTO")
    private Long id;

    @Column(name = "NM_PRODUTO")
    private String nome;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(
            name = "SABOR",
            referencedColumnName = "ID_SABOR",
            foreignKey = @ForeignKey(name = "FK_2SABOR_PRODUTO")
    )
    private Sabor sabor;

    @Column(name = "PRECO_PRODUTO")
    private BigDecimal preco;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "TB_PRODUTO_OPCIONAL",
            joinColumns = {
                    @JoinColumn(
                            name = "PRODUTO",
                            referencedColumnName = "ID_PRODUTO",
                            foreignKey = @ForeignKey(name = "FK_PRODUTO_OPCIONAL")
                    )
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "OPCIONAL",
                            referencedColumnName = "ID_OPCIONAL",
                            foreignKey = @ForeignKey(name = "FK_OPCIONAL_PRODUTO")
                    )
            }
    )
    private Set<Opcional> opcionais = new LinkedHashSet<>();
}