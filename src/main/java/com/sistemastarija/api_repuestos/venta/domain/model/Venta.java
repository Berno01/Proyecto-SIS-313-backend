package com.sistemastarija.api_repuestos.venta.domain.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class Venta
{
    private Integer idVenta;
    private String nombreCliente;
    private LocalDateTime fechaVenta;
    private Double total;
    private Double descuentoTotal;
    private List<DetalleVenta> detalleVenta;
    private Boolean estadoVenta;
    private Integer idUsuario; // ID del usuario para auditoría
    private String username; // Username del usuario que creó/actualizó

    public Venta(Integer idVenta, String nombreCliente, LocalDateTime fechaVenta, List<DetalleVenta> detalleVenta, Integer idUsuario){
        this.idVenta = idVenta;
        this.nombreCliente = nombreCliente;
        this.fechaVenta = fechaVenta;
        this.detalleVenta = detalleVenta;
        this.idUsuario = idUsuario;
        this.total = this.detalleVenta.stream().mapToDouble(DetalleVenta::getTotal).sum();
        this.descuentoTotal = this.detalleVenta.stream().mapToDouble(DetalleVenta::getDescuento).sum();
        this.estadoVenta = true;
    }

}