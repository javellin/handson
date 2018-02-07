package br.com.azi.handson.job.estoque.batch;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Entity
@Table(name = "ESTOQUE")
@SequenceGenerator(name = "ESTOQUE_SEQ", sequenceName = "ESTOQUE_SEQ")
@NamedQueries({
        @NamedQuery(name = "Estoque.all", query = "SELECT e FROM Estoque e ORDER BY e.id ASC")
})
public class Estoque {

    @Id
    @Column(name = "ES_ID")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ESTOQUE_SEQ")
    private Integer id;

    @Column(name = "ES_SALDO")
    private BigDecimal saldo;


}
