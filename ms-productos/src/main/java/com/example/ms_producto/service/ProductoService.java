package com.example.ms_producto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.ms_producto.dto.DtoResponseProducto;
import com.example.ms_producto.dto.ProductoDto;
import com.example.ms_producto.modelo.Productos;
import com.example.ms_producto.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<DtoResponseProducto> obtenerProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
        //mapeo

    private DtoResponseProducto convertToDto(Productos producto) {
        return new DtoResponseProducto(
                producto.getProductoId(),
                producto.getNombre(),
                producto.getSku(),
                producto.getPrecio(),
                producto.getEstado()
        );
    }
    

    public Optional<DtoResponseProducto> obtenerPorId(Long productoId) {
        return productoRepository.findById(productoId).map(this::convertToDto);
    }


    public DtoResponseProducto guardar(ProductoDto dtoProducto){
        Productos producto = new Productos(
                dtoProducto.getProductoId(),
                dtoProducto.getNombre(),
                dtoProducto.getSku(),
                dtoProducto.getPrecio(),
                dtoProducto.getEstado()
        );
        return convertToDto(productoRepository.save(producto));
    }

      public Optional<DtoResponseProducto> actualizar(Long productoId, ProductoDto dtoProducto) {
        return productoRepository.findById(productoId).map(producto -> {
            producto.setNombre(dtoProducto.getNombre());
            producto.setSku(dtoProducto.getSku());
            producto.setPrecio(dtoProducto.getPrecio());
            producto.setEstado(dtoProducto.getEstado());
            return convertToDto(productoRepository.save(producto));
        });
    }

    public void eliminar(Long productoId) {
        productoRepository.deleteById(productoId);
    }


}