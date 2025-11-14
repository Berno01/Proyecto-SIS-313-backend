package com.sistemastarija.api_repuestos.venta.infrastructure.adapter.in.web.controller;

import com.sistemastarija.api_repuestos.venta.application.service.VentaRepuestoService;
import com.sistemastarija.api_repuestos.venta.domain.model.Repuesto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
public class VentaRepuestoController {
    private final VentaRepuestoService ventaRepuestoService;

    public VentaRepuestoController(VentaRepuestoService ventaRepuestoService){
        this.ventaRepuestoService = ventaRepuestoService;
    }

    @GetMapping("/repuesto/findAll")
    public List<Repuesto> findAll() {
        return ventaRepuestoService.findAll();
    }

    @GetMapping("/repuesto/findById/{idRepuesto}")
    public Optional<Repuesto> findById(@PathVariable Integer idRepuesto) {
        return ventaRepuestoService.findById(idRepuesto);
    }

}
