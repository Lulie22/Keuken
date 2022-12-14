package be.vdab.keuken.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class ArtikelTest {
   private Artikel artikel;
   private ArtikelGroep groep1,groep2;

   @BeforeEach
    void beforeEach(){
       groep1 = new ArtikelGroep("test");
       groep2 = new ArtikelGroep("test2");
       artikel = new FoodArtikel("test",BigDecimal.ONE,BigDecimal.TEN,7,groep1);
   }
   @Test
    void verhoogVerkoopPrijs(){
       artikel.verhoogVerkoopPrijs(BigDecimal.TEN);
       assertThat(artikel.getVerkoopprijs()).isEqualByComparingTo("20");
   }
   @Test
    void verhoogVerkoopPrijsMetNullMislukt(){
       assertThatNullPointerException().isThrownBy(()->
               artikel.verhoogVerkoopPrijs(null));
   }
   @Test
    void verhoogVerkoopPrijsMetMet0Mislukt(){
       assertThatIllegalArgumentException().isThrownBy(()->
               artikel.verhoogVerkoopPrijs(BigDecimal.ZERO));
   }
   @Test
    void verhoogVerkoopPrijsMetMetNegatieveWaardeMislukt(){
       assertThatIllegalArgumentException().isThrownBy(()->
               artikel.verhoogVerkoopPrijs(BigDecimal.valueOf(-1)));
   }
   @Test
    void groep1EnArtikelZijnVerbonden(){
       assertThat(groep1.getArtikels()).containsOnly(artikel);
       assertThat(artikel.getArtikelGroep()).isEqualTo(groep1);
   }
   @Test
    void artikelVerhuistNaarGroep2(){
       artikel.setArtikelGroep(groep2);
       assertThat(groep1.getArtikels()).doesNotContain(artikel);
       assertThat(groep2.getArtikels()).contains(artikel);
   }
   @Test
    void nullAlsArtikelGroepInDeConstructorMislukt(){
       assertThatNullPointerException().isThrownBy(
               ()-> new FoodArtikel("test",BigDecimal.ONE,BigDecimal.ONE,1,null));
   }
   @Test
    void nullAlsArtikelGroepInDeSetterMislukt(){
       assertThatNullPointerException().isThrownBy(
               () -> artikel.setArtikelGroep(null));
   }
}
