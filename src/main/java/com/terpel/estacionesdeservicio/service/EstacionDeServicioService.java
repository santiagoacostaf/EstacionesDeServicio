package com.terpel.estacionesdeservicio.service;

import com.terpel.estacionesdeservicio.dto.EstacionDeServicioRequest;
import com.terpel.estacionesdeservicio.dto.EstacionDeServicioResponse;
import com.terpel.estacionesdeservicio.entity.EstacionDeServicio;
import com.terpel.estacionesdeservicio.entity.EstadoEstacion;
import com.terpel.estacionesdeservicio.repository.EstacionDeServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstacionDeServicioService {

    private final EstacionDeServicioRepository estacionDeServicioRepository;

    public EstacionDeServicioService(EstacionDeServicioRepository estacionDeServicioRepository) {
        this.estacionDeServicioRepository = estacionDeServicioRepository;
    }

    public EstacionDeServicioResponse crear(EstacionDeServicioRequest request) {
        EstacionDeServicio estacionDeServicio = new EstacionDeServicio();

        estacionDeServicio.setCodigo(request.getCodigo());
        estacionDeServicio.setNombre(request.getNombre());
        estacionDeServicio.setDireccion(request.getDireccion());
        estacionDeServicio.setCiudad(request.getCiudad());
        estacionDeServicio.setLatitud(request.getLatitud());
        estacionDeServicio.setLongitud(request.getLongitud());

        if (request.getEstado() == null) {
            estacionDeServicio.setEstado(EstadoEstacion.ACTIVA);
        } else {
            estacionDeServicio.setEstado(request.getEstado());
        }

        EstacionDeServicio estacionGuardada = estacionDeServicioRepository.save(estacionDeServicio);

        return convertirAResponse(estacionGuardada);
    }

    public Optional<EstacionDeServicioResponse> consultarPorId(Long id) {
        return estacionDeServicioRepository.findById(id)
                .map(this::convertirAResponse);
    }

    public List<EstacionDeServicioResponse> listar() {
        return estacionDeServicioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public Optional<EstacionDeServicioResponse> actualizar(Long id, EstacionDeServicioRequest request) {
        return estacionDeServicioRepository.findById(id)
                .map(estacionExistente -> {
                    estacionExistente.setCodigo(request.getCodigo());
                    estacionExistente.setNombre(request.getNombre());
                    estacionExistente.setDireccion(request.getDireccion());
                    estacionExistente.setCiudad(request.getCiudad());
                    estacionExistente.setLatitud(request.getLatitud());
                    estacionExistente.setLongitud(request.getLongitud());

                    if (request.getEstado() != null) {
                        estacionExistente.setEstado(request.getEstado());
                    }

                    EstacionDeServicio estacionActualizada = estacionDeServicioRepository.save(estacionExistente);

                    return convertirAResponse(estacionActualizada);
                });
    }

    public Optional<EstacionDeServicioResponse> eliminarLogicamente(Long id) {
        return estacionDeServicioRepository.findById(id)
                .map(estacion -> {
                    estacion.setEstado(EstadoEstacion.INACTIVA);

                    EstacionDeServicio estacionActualizada = estacionDeServicioRepository.save(estacion);

                    return convertirAResponse(estacionActualizada);
                });
    }

    private EstacionDeServicioResponse convertirAResponse(EstacionDeServicio estacion) {
        return new EstacionDeServicioResponse(
                estacion.getId(),
                estacion.getCodigo(),
                estacion.getNombre(),
                estacion.getDireccion(),
                estacion.getCiudad(),
                estacion.getLatitud(),
                estacion.getLongitud(),
                estacion.getEstado(),
                estacion.getFechaCreacion(),
                estacion.getFechaActualizacion()
        );
    }
}
