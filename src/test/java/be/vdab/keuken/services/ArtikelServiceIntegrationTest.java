package be.vdab.keuken.services;

import be.vdab.keuken.repositories.ArtikelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Import({ArtikelService.class, ArtikelRepository.class})
@Sql({"/insertArtikelGroep.sql","/insertArtikel.sql"})
public class ArtikelServiceIntegrationTest
        extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String ARTIKELS = "artikels";
    private final ArtikelService service;
    private final EntityManager manager;

    public ArtikelServiceIntegrationTest(ArtikelService service, EntityManager manager) {
        this.service = service;
        this.manager = manager;
    }
    private long idVanTestArtikel(){
        return jdbcTemplate.queryForObject(
                "select id from artikels where naam = 'testfood'", Long.class
        );
    }
    @Test
    void verhoogVerkoopPrijs(){
        service.verhoogVerkoopPrijs(idVanTestArtikel(), BigDecimal.TEN);
        manager.flush();
        assertThat(countRowsInTableWhere(ARTIKELS,
                "verkoopprijs = 130 and id ="+idVanTestArtikel())).isOne();
    }
}
