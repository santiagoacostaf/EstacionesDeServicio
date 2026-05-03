package com.terpel.estacionesdeservicio.service;

import com.terpel.estacionesdeservicio.dto.EstacionDeServicioRequest;
import com.terpel.estacionesdeservicio.dto.EstacionDeServicioResponse;

import java.util.List;
import java.util.Optional;

public interface EstacionDeServicioService {

    EstacionDeServicioResponse crear(EstacionDeServicioRequest request);

    Optional<EstacionDeServicioResponse> consultarPorId(Long id);

    List<EstacionDeServicioResponse> listar();

    Optional<EstacionDeServicioResponse> actualizar(Long id, EstacionDeServicioRequest request);

    Optional<EstacionDeServicioResponse> eliminarLogicamente(Long id);
}
