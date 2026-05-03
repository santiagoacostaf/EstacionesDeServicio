package com.terpel.estacionesdeservicio.controller;

import com.terpel.estacionesdeservicio.dto.EstacionDeServicioRequest;
import com.terpel.estacionesdeservicio.dto.EstacionDeServicioResponse;
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
    public ResponseEntity<?> crear(@RequestBody EstacionDeServicioRequest request) {
        try {
            EstacionDeServicioResponse estacionCreada = estacionDeServicioService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(estacionCreada);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se pudo crear la estación de servicio porque " + exception.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstacionDeServicioResponse> consultarPorId(@PathVariable Long id) {
        return estacionDeServicioService.consultarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(estacionDeServicioService.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody EstacionDeServicioRequest request
    ) {
        try {
            return estacionDeServicioService.actualizar(id, request)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se pudo actualizar la estación de servicio porque " + exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EstacionDeServicioResponse> eliminar(@PathVariable Long id) {
        return estacionDeServicioService.eliminarLogicamente(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
