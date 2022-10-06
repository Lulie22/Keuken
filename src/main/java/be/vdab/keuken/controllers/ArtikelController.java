package be.vdab.keuken.controllers;

import be.vdab.keuken.domain.Artikel;
import be.vdab.keuken.exceptions.ArtikelNietGevondenException;
import be.vdab.keuken.services.ArtikelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("artikels")
public class ArtikelController {
    private final ArtikelService artikelService;

    public ArtikelController(ArtikelService artikelService) {
        this.artikelService = artikelService;
    }
    //@GetMapping
//    List<Artikel> findAll(){
//        return artikelService.findAll();
//    }
//    @GetMapping("{id}")
//    Artikel findById(@PathVariable long id){
//        return artikelService.findById(id)
//                        .orElseThrow(ArtikelNietGevondenException::new);
//    }
//    @GetMapping("aantal")
//    long findAantal() {
//        return artikelService.findAantal();
//    }
//    @GetMapping("{id}")
//    boolean existsById(@PathVariable long id){
//        return artikelService.existsById(id);
//    }
}
