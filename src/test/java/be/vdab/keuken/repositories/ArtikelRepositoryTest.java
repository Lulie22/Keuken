package be.vdab.keuken.repositories;

import be.vdab.keuken.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

@DataJpaTest
/*
In je tests logt JPA nu elk SQL statements twee keer na mekaar (één keer door @DataJpaTest en één keer door de laatste regel in application.properties).
Dit is verwarrend. Je voorkomt dit met volgende wijziging in DocentRepositoryTest:
@DataJpaTest(showSql = false)
 */
@Import(ArtikelRepository.class)
@Sql({"/insertArtikelGroep.sql", "/insertArtikel.sql"})
public class ArtikelRepositoryTest
        extends AbstractTransactionalJUnit4SpringContextTests {
    private final ArtikelRepository repository;
    private Artikel artikel;
    private final EntityManager manager;
    private static final String ARTIKELS = "artikels";

    public ArtikelRepositoryTest(ArtikelRepository repository, EntityManager manager) {

        this.repository = repository;
        this.manager = manager;
    }

    private long idVanTestFoodArtikel() {
        return jdbcTemplate.queryForObject(
                "select id from artikels where naam = 'testfood'", Long.class);
    }

    private long idVanTestNonFoodArtikel() {
        return jdbcTemplate.queryForObject(
                "select id from artikels where naam = 'testnonfood'", Long.class);
    }

    @Test
    void findFoodArtikelById() {
        assertThat(repository.findById(idVanTestFoodArtikel()))
                .containsInstanceOf(FoodArtikel.class)
                .hasValueSatisfying(artikel ->
                        assertThat(artikel.getNaam())
                                .isEqualTo("testfood"));
    }

    @Test
    void findNonFoodArtikelById() {
        assertThat(repository.findById(idVanTestNonFoodArtikel()))
                .containsInstanceOf(NonFoodArtikel.class)
                .hasValueSatisfying(artikel ->
                        assertThat(artikel.getNaam())
                                .isEqualTo("testnonfood"));
    }

    @Test
    void findOnbestaandeId() {
        assertThat(repository.findById(-1)).isEmpty();
    }

    @Test
    void createFoodArtikel() {
        var groep = new ArtikelGroep("test");
        manager.persist(groep);
        var artikel = new FoodArtikel("testfood2", BigDecimal.ONE, BigDecimal.TEN, 7, groep);
        repository.create(artikel);
        assertThat(countRowsInTableWhere(ARTIKELS, "id =" + artikel.getId())).isOne();
    }

    @Test
    void createNonFoodArtikel() {
        var groep = new ArtikelGroep("test");
        manager.persist(groep);
        var artikel = new NonFoodArtikel("testnonfood2", BigDecimal.ONE, BigDecimal.TEN, 30, groep);
        repository.create(artikel);
        assertThat(countRowsInTableWhere(ARTIKELS, "id=" + artikel.getId())).isOne();
    }

    @Test
    void findBijNaamContains() {
        var artikels = repository.findByNaamContains("es");
        //manager.clear();
        assertThat(artikels)
                .hasSize(countRowsInTableWhere(ARTIKELS, "naam like '%es%'"))
                .extracting(Artikel::getNaam)
                .allSatisfy(naam -> assertThat(naam).containsIgnoringCase("es"))
                .isSortedAccordingTo(String::compareToIgnoreCase);
        assertThat(artikels)
                .extracting(Artikel::getArtikelGroep)
                .extracting(ArtikelGroep::getNaam)
                .isNotNull();
    }

    @Test
    void verhoogAlleVerkoopPrijzen() {
        assertThat(repository.verhoogAlleVerkoopPrijzen(BigDecimal.TEN))
                .isEqualTo(countRowsInTable("artikels"));
        assertThat(countRowsInTableWhere(ARTIKELS, "verkoopprijs = 132 and id = " + idVanTestFoodArtikel())).isOne();
    }

    //test of ElementCollection
    @Test
    void kortingenLezen() {
        assertThat(repository.findById(idVanTestFoodArtikel()))
                .hasValueSatisfying(artikel -> assertThat(artikel.getKortingen())
                        .containsOnly(new Korting(1, BigDecimal.TEN)));
    }

    @Test
    void artikelGroepLazyLoading() {
        assertThat(repository.findById(idVanTestFoodArtikel())).isNotEmpty();
        //.hasValueSatisfying(artikel -> assertThat(artikel.getArtikelGroep().getNaam()).isEqualTo("test"));
    }
}
