package com.example.ms_proveedores.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ms_proveedores.modelo.Proveedor;
import com.example.ms_proveedores.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorService {
    private final ProveedorRepository provedorRepository;

    public List<Proveedor> obtenerProvedores() {
        return provedorRepository.findAll();
    }

    public Optional<Proveedor> obtenerPorId(Long id) {
        return provedorRepository.findById(id);
    }

    public Proveedor guardar(Proveedor  provedor) {
        return provedorRepository.save(provedor);
    }

    public void eliminar(Long id) {
        provedorRepository.deleteById(id);
    }
}