package com.terpel.estacionesdeservicio.dto;

import com.terpel.estacionesdeservicio.entity.EstadoEstacion;

public class EstacionDeServicioRequest {

    private String codigo;
    private String nombre;
    private String direccion;
    private String ciudad;
    private Double latitud;
    private Double longitud;
    private EstadoEstacion estado;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = Double.valueOf(latitud);
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = Double.valueOf(longitud);
    }

    public EstadoEstacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoEstacion estado) {
        this.estado = estado;
    }
}
