package com.terpel.estacionesdeservicio.service;

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

    public EstacionDeServicio crear(EstacionDeServicio estacionDeServicio) {
        if (estacionDeServicio.getEstado() == null) {
            estacionDeServicio.setEstado(EstadoEstacion.ACTIVA);
        }

        return estacionDeServicioRepository.save(estacionDeServicio);
    }

    public Optional<EstacionDeServicio> consultarPorId(Long id) {
        return estacionDeServicioRepository.findById(id);
    }

    public List<EstacionDeServicio> listar() {
        return estacionDeServicioRepository.findAll();
    }

    public Optional<EstacionDeServicio> actualizar(Long id, EstacionDeServicio datosActualizados) {
        return estacionDeServicioRepository.findById(id)
                .map(estacionExistente -> {
                    estacionExistente.setCodigo(datosActualizados.getCodigo());
                    estacionExistente.setNombre(datosActualizados.getNombre());
                    estacionExistente.setDireccion(datosActualizados.getDireccion());
                    estacionExistente.setCiudad(datosActualizados.getCiudad());
                    estacionExistente.setLatitud(datosActualizados.getLatitud());
                    estacionExistente.setLongitud(datosActualizados.getLongitud());

                    if (datosActualizados.getEstado() != null) {
                        estacionExistente.setEstado(datosActualizados.getEstado());
                    }

                    return estacionDeServicioRepository.save(estacionExistente);
                });
    }

    public Optional<EstacionDeServicio> eliminarLogicamente(Long id) {
        return estacionDeServicioRepository.findById(id)
                .map(estacion -> {
                    estacion.setEstado(EstadoEstacion.INACTIVA);
                    return estacionDeServicioRepository.save(estacion);
                });
    }
}
