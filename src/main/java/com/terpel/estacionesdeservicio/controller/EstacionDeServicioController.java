package com.terpel.estacionesdeservicio.controller;

import com.terpel.estacionesdeservicio.entity.EstacionDeServicio;
import com.terpel.estacionesdeservicio.service.EstacionDeServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stations")
public class EstacionDeServicioController {

    private final EstacionDeServicioService estacionDeServicioService;

    public EstacionDeServicioController(EstacionDeServicioService estacionDeServicioService) {
        this.estacionDeServicioService = estacionDeServicioService;
    }

    @PostMapping
    public ResponseEntity<EstacionDeServicio> crear(@RequestBody EstacionDeServicio estacionDeServicio) {
        EstacionDeServicio estacionCreada = estacionDeServicioService.crear(estacionDeServicio);
        return ResponseEntity.status(HttpStatus.CREATED).body(estacionCreada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstacionDeServicio> consultarPorId(@PathVariable Long id) {
        return estacionDeServicioService.consultarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(estacionDeServicioService.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstacionDeServicio> actualizar(
            @PathVariable Long id,
            @RequestBody EstacionDeServicio estacionDeServicio
    ) {
        return estacionDeServicioService.actualizar(id, estacionDeServicio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EstacionDeServicio> eliminar(@PathVariable Long id) {
        return estacionDeServicioService.eliminarLogicamente(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
