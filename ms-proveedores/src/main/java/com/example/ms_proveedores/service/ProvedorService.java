package com.example.ms_proveedores.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ms_proveedores.dto.DtoResponseProvedor;
import com.example.ms_proveedores.modelo.Proveedor;
import com.example.ms_proveedores.repository.ProvedorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
@RequiredArgsConstructor
public class ProvedorService {
    private final ProvedorRepository provedorRepository;


   
    private DtoResponseProvedor mapToDto(Proveedor proveedor){
        return new DtoResponseProvedor(
                proveedor.getProvedorId(),
                proveedor.getRazonSocial(),
                proveedor.getRut(),
                proveedor.getEmail(),
                proveedor.getTelefono(),
                proveedor.getEstado()
        );
    }
    
    public List<DtoResponseProvedor> obtenerProvedores() {
        return provedorRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(java.util.stream.Collectors.toList());
    }
    //verifica si el servicio existe
    public Optional<DtoResponseProvedor>obtenerPorId1(Long id) {
        return provedorRepository.findById(id).map(this::mapToDto);
    }    

    public DtoResponseProvedor guardar(DtoResponseProvedor dtoProvedor){
        Proveedor provedor = new Proveedor(
                dtoProvedor.getProvedorId(),
                dtoProvedor.getRazonSocial(),
                dtoProvedor.getRut(),
                dtoProvedor.getEmail(),
                dtoProvedor.getTelefono(),
                dtoProvedor.getEstado());
        return mapToDto(provedorRepository.save(provedor));
    }

    public Optional<DtoResponseProvedor> actualizar(Long provedorId, DtoResponseProvedor dtoProvedor) {
        return provedorRepository.findById(provedorId).map(provedor -> {
            provedor.setRazonSocial(dtoProvedor.getRazonSocial());
            provedor.setRut(dtoProvedor.getRut());
            provedor.setEmail(dtoProvedor.getEmail());
            provedor.setTelefono(dtoProvedor.getTelefono());
            provedor.setEstado(dtoProvedor.getEstado());
            return mapToDto(provedorRepository.save(provedor));
        });
    }




    public Optional<DtoResponseProvedor> obtenerProvedores(Long provedorId) {
        return provedorRepository.findById(provedorId).map(this::mapToDto);
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