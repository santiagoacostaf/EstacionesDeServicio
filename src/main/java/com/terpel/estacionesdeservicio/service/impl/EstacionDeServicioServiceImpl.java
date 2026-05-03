package com.terpel.estacionesdeservicio.service.impl;

import com.terpel.estacionesdeservicio.dto.EstacionDeServicioRequest;
import com.terpel.estacionesdeservicio.dto.EstacionDeServicioResponse;
import com.terpel.estacionesdeservicio.entity.EstacionDeServicio;
import com.terpel.estacionesdeservicio.entity.EstadoEstacion;
import com.terpel.estacionesdeservicio.repository.EstacionDeServicioRepository;
import com.terpel.estacionesdeservicio.service.EstacionDeServicioService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
public class EstacionDeServicioServiceImpl implements EstacionDeServicioService {

    private final EstacionDeServicioRepository estacionDeServicioRepository;

    public EstacionDeServicioServiceImpl(EstacionDeServicioRepository estacionDeServicioRepository) {
        this.estacionDeServicioRepository = estacionDeServicioRepository;
    }

    @Override
    @Caching(
            put = {
                    @CachePut(value = "estacionesPorId", key = "#result.id", unless = "#result == null")
            },
            evict = {
                    @CacheEvict(value = "estacionesListado", allEntries = true)
            }
    )
    public EstacionDeServicioResponse crear(EstacionDeServicioRequest request) {
        try {
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
        } catch (Exception exception) {
            throw new RuntimeException("ya existe un dato duplicado o se viola una restricción de base de datos.", exception);
        }
    }

    @Override
    @Cacheable(value = "estacionesPorId", key = "#id", unless = "#result == null")
    public Optional<EstacionDeServicioResponse> consultarPorId(Long id) {
        return estacionDeServicioRepository.findById(id)
                .map(this::convertirAResponse);
    }

    @Override
    @Cacheable(value = "estacionesListado")
    public List<EstacionDeServicioResponse> listar() {
        return estacionDeServicioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Override
    @Caching(
            put = {
                    @CachePut(value = "estacionesPorId", key = "#id", unless = "#result == null")
            },
            evict = {
                    @CacheEvict(value = "estacionesListado", allEntries = true)
            }
    )
    public Optional<EstacionDeServicioResponse> actualizar(Long id, EstacionDeServicioRequest request) {
        try {
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
        }catch (Exception exception) {
            throw new RuntimeException("ya existe un dato duplicado o se viola una restricción de base de datos.", exception);
        }
    }

    @Override
    @Caching(
            put = {
                    @CachePut(value = "estacionesPorId", key = "#id", unless = "#result == null")
            },
            evict = {
                    @CacheEvict(value = "estacionesListado", allEntries = true)
            }
    )
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
