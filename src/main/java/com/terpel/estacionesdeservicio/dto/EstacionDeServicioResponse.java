package com.terpel.estacionesdeservicio.dto;

import com.terpel.estacionesdeservicio.entity.EstadoEstacion;

import java.time.LocalDateTime;

public class EstacionDeServicioResponse {

    private Long id;
    private String codigo;
    private String nombre;
    private String direccion;
    private String ciudad;
    private Double latitud;
    private Double longitud;
    private EstadoEstacion estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public EstacionDeServicioResponse(
            Long id,
            String codigo,
            String nombre,
            String direccion,
            String ciudad,
            Double latitud,
            Double longitud,
            EstadoEstacion estado,
            LocalDateTime fechaCreacion,
            LocalDateTime fechaActualizacion
    ) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public EstadoEstacion getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
}
