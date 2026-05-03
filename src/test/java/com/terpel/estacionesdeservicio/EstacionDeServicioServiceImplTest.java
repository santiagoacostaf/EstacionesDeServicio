package com.terpel.estacionesdeservicio.service.impl;

import com.terpel.estacionesdeservicio.dto.EstacionDeServicioRequest;
import com.terpel.estacionesdeservicio.dto.EstacionDeServicioResponse;
import com.terpel.estacionesdeservicio.entity.EstacionDeServicio;
import com.terpel.estacionesdeservicio.entity.EstadoEstacion;
import com.terpel.estacionesdeservicio.repository.EstacionDeServicioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstacionDeServicioServiceImplTest {

    @Mock
    private EstacionDeServicioRepository estacionDeServicioRepository;

    @InjectMocks
    private EstacionDeServicioServiceImpl estacionDeServicioService;

    private EstacionDeServicioRequest request;
    private EstacionDeServicio estacion;

    @BeforeEach
    void setUp() {
        request = new EstacionDeServicioRequest();
        request.setCodigo("EDS-001");
        request.setNombre("Estación Principal");
        request.setDireccion("Calle 123");
        request.setCiudad("Bogotá");
        request.setLatitud("4.7110");
        request.setLongitud("-74.0721");
        request.setEstado(EstadoEstacion.ACTIVA);

        estacion = new EstacionDeServicio();
        ReflectionTestUtils.setField(estacion, "id", 1L);
        estacion.setCodigo("EDS-001");
        estacion.setNombre("Estación Principal");
        estacion.setDireccion("Calle 123");
        estacion.setCiudad("Bogotá");
        estacion.setLatitud(4.7110);
        estacion.setLongitud(-74.0721);
        estacion.setEstado(EstadoEstacion.ACTIVA);
        ReflectionTestUtils.setField(estacion, "fechaCreacion", LocalDateTime.now());
        ReflectionTestUtils.setField(estacion, "fechaActualizacion", LocalDateTime.now());
    }

    @Test
    void crear_deberiaGuardarEstacionYRetornarResponse() {
        when(estacionDeServicioRepository.save(any(EstacionDeServicio.class))).thenReturn(estacion);

        EstacionDeServicioResponse response = estacionDeServicioService.crear(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("EDS-001", response.getCodigo());
        assertEquals("Estación Principal", response.getNombre());
        assertEquals("Calle 123", response.getDireccion());
        assertEquals("Bogotá", response.getCiudad());
        assertEquals(4.7110, response.getLatitud());
        assertEquals(-74.0721, response.getLongitud());
        assertEquals(EstadoEstacion.ACTIVA, response.getEstado());

        verify(estacionDeServicioRepository).save(any(EstacionDeServicio.class));
    }

    @Test
    void crear_cuandoEstadoEsNull_deberiaAsignarEstadoActivo() {
        request.setEstado(null);

        when(estacionDeServicioRepository.save(any(EstacionDeServicio.class))).thenReturn(estacion);

        estacionDeServicioService.crear(request);

        ArgumentCaptor<EstacionDeServicio> captor = ArgumentCaptor.forClass(EstacionDeServicio.class);
        verify(estacionDeServicioRepository).save(captor.capture());

        EstacionDeServicio estacionGuardada = captor.getValue();

        assertEquals(EstadoEstacion.ACTIVA, estacionGuardada.getEstado());
    }

    @Test
    void crear_cuandoRepositorioLanzaError_deberiaLanzarRuntimeException() {
        when(estacionDeServicioRepository.save(any(EstacionDeServicio.class)))
                .thenThrow(new DataIntegrityViolationException("Código duplicado"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> estacionDeServicioService.crear(request)
        );

        assertEquals("ya existe un dato duplicado o se viola una restricción de base de datos.", exception.getMessage());
        assertNotNull(exception.getCause());

        verify(estacionDeServicioRepository).save(any(EstacionDeServicio.class));
    }

    @Test
    void consultarPorId_cuandoExiste_deberiaRetornarResponse() {
        when(estacionDeServicioRepository.findById(1L)).thenReturn(Optional.of(estacion));

        Optional<EstacionDeServicioResponse> response = estacionDeServicioService.consultarPorId(1L);

        assertTrue(response.isPresent());
        assertEquals(1L, response.get().getId());
        assertEquals("EDS-001", response.get().getCodigo());

        verify(estacionDeServicioRepository).findById(1L);
    }

    @Test
    void consultarPorId_cuandoNoExiste_deberiaRetornarOptionalVacio() {
        when(estacionDeServicioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<EstacionDeServicioResponse> response = estacionDeServicioService.consultarPorId(99L);

        assertTrue(response.isEmpty());

        verify(estacionDeServicioRepository).findById(99L);
    }

    @Test
    void listar_deberiaRetornarListaDeResponses() {
        EstacionDeServicio otraEstacion = new EstacionDeServicio();
        ReflectionTestUtils.setField(otraEstacion, "id", 2L);
        otraEstacion.setCodigo("EDS-002");
        otraEstacion.setNombre("Estación Secundaria");
        otraEstacion.setDireccion("Carrera 45");
        otraEstacion.setCiudad("Medellín");
        otraEstacion.setLatitud(6.2442);
        otraEstacion.setLongitud(-75.5812);
        otraEstacion.setEstado(EstadoEstacion.ACTIVA);
        ReflectionTestUtils.setField(otraEstacion, "fechaCreacion", LocalDateTime.now());
        ReflectionTestUtils.setField(otraEstacion, "fechaActualizacion", LocalDateTime.now());

        when(estacionDeServicioRepository.findAll()).thenReturn(List.of(estacion, otraEstacion));

        List<EstacionDeServicioResponse> responses = estacionDeServicioService.listar();

        assertEquals(2, responses.size());
        assertEquals("EDS-001", responses.get(0).getCodigo());
        assertEquals("EDS-002", responses.get(1).getCodigo());

        verify(estacionDeServicioRepository).findAll();
    }

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornarResponse() {
        EstacionDeServicio estacionActualizada = new EstacionDeServicio();
        ReflectionTestUtils.setField(estacionActualizada, "id", 1L);
        estacionActualizada.setCodigo("EDS-001-ACT");
        estacionActualizada.setNombre("Estación Actualizada");
        estacionActualizada.setDireccion("Nueva dirección");
        estacionActualizada.setCiudad("Cali");
        estacionActualizada.setLatitud(3.4516);
        estacionActualizada.setLongitud(-76.5320);
        estacionActualizada.setEstado(EstadoEstacion.INACTIVA);
        ReflectionTestUtils.setField(estacionActualizada, "fechaCreacion", LocalDateTime.now());
        ReflectionTestUtils.setField(estacionActualizada, "fechaActualizacion", LocalDateTime.now());

        EstacionDeServicioRequest requestActualizar = new EstacionDeServicioRequest();
        requestActualizar.setCodigo("EDS-001-ACT");
        requestActualizar.setNombre("Estación Actualizada");
        requestActualizar.setDireccion("Nueva dirección");
        requestActualizar.setCiudad("Cali");
        requestActualizar.setLatitud("3.4516");
        requestActualizar.setLongitud("-76.5320");
        requestActualizar.setEstado(EstadoEstacion.INACTIVA);

        when(estacionDeServicioRepository.findById(1L)).thenReturn(Optional.of(estacion));
        when(estacionDeServicioRepository.save(any(EstacionDeServicio.class))).thenReturn(estacionActualizada);

        Optional<EstacionDeServicioResponse> response = estacionDeServicioService.actualizar(1L, requestActualizar);

        assertTrue(response.isPresent());
        assertEquals("EDS-001-ACT", response.get().getCodigo());
        assertEquals("Estación Actualizada", response.get().getNombre());
        assertEquals("Cali", response.get().getCiudad());
        assertEquals(EstadoEstacion.INACTIVA, response.get().getEstado());

        verify(estacionDeServicioRepository).findById(1L);
        verify(estacionDeServicioRepository).save(any(EstacionDeServicio.class));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarOptionalVacio() {
        when(estacionDeServicioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<EstacionDeServicioResponse> response = estacionDeServicioService.actualizar(99L, request);

        assertTrue(response.isEmpty());

        verify(estacionDeServicioRepository).findById(99L);
        verify(estacionDeServicioRepository, never()).save(any(EstacionDeServicio.class));
    }

    @Test
    void eliminarLogicamente_cuandoExiste_deberiaCambiarEstadoAInactiva() {
        EstacionDeServicio estacionInactiva = new EstacionDeServicio();
        ReflectionTestUtils.setField(estacionInactiva, "id", 1L);
        estacionInactiva.setCodigo("EDS-001");
        estacionInactiva.setNombre("Estación Principal");
        estacionInactiva.setDireccion("Calle 123");
        estacionInactiva.setCiudad("Bogotá");
        estacionInactiva.setLatitud(4.7110);
        estacionInactiva.setLongitud(-74.0721);
        estacionInactiva.setEstado(EstadoEstacion.INACTIVA);
        ReflectionTestUtils.setField(estacionInactiva, "fechaCreacion", LocalDateTime.now());
        ReflectionTestUtils.setField(estacionInactiva, "fechaActualizacion", LocalDateTime.now());

        when(estacionDeServicioRepository.findById(1L)).thenReturn(Optional.of(estacion));
        when(estacionDeServicioRepository.save(any(EstacionDeServicio.class))).thenReturn(estacionInactiva);

        Optional<EstacionDeServicioResponse> response = estacionDeServicioService.eliminarLogicamente(1L);

        assertTrue(response.isPresent());
        assertEquals(EstadoEstacion.INACTIVA, response.get().getEstado());

        ArgumentCaptor<EstacionDeServicio> captor = ArgumentCaptor.forClass(EstacionDeServicio.class);
        verify(estacionDeServicioRepository).save(captor.capture());

        assertEquals(EstadoEstacion.INACTIVA, captor.getValue().getEstado());
    }

    @Test
    void eliminarLogicamente_cuandoNoExiste_deberiaRetornarOptionalVacio() {
        when(estacionDeServicioRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<EstacionDeServicioResponse> response = estacionDeServicioService.eliminarLogicamente(99L);

        assertTrue(response.isEmpty());

        verify(estacionDeServicioRepository).findById(99L);
        verify(estacionDeServicioRepository, never()).save(any(EstacionDeServicio.class));
    }
}