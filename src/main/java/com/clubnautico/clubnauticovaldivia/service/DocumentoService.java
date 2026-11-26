package com.clubnautico.clubnauticovaldivia.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.clubnautico.clubnauticovaldivia.DTO.DocumentoDTO;
import com.clubnautico.clubnauticovaldivia.model.Documento;
import com.clubnautico.clubnauticovaldivia.model.Usuario;
import com.clubnautico.clubnauticovaldivia.repository.DocumentoRepository;
import com.clubnautico.clubnauticovaldivia.repository.UsuarioRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.core.exception.SdkException;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private S3Client s3Client;

    @Value("${storage.bucket}")
    private String bucket;

    public List<DocumentoDTO> listar() {
        return documentoRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public DocumentoDTO buscarPorId(Integer id) {

        Documento documento = obtenerDocumento(id);

        return convertirDTO(documento);
    }

    public List<DocumentoDTO> buscarPorRut(String rut) {

        return documentoRepository.buscarPorRut(rut)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    public DocumentoDTO guardar(MultipartFile archivo, String rut) {

        if (archivo.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El archivo está vacío");
        }

        Usuario usuario = usuarioRepository.findById(rut)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado"));

        String nombre = archivo.getOriginalFilename();

        if (nombre == null || nombre.isBlank() || nombre.length() > 255) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Nombre de archivo inválido");
        }

        //Genera una ruta para el archivo
        String rutaArchivo = "documentos/" + UUID.randomUUID();

        String tipoArchivo = archivo.getContentType();

        if (tipoArchivo == null || tipoArchivo.isBlank()) {
            tipoArchivo = "application/octet-stream";
        }

        try {
            //Sube el archivo al bucket
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(rutaArchivo)
                            .contentType(tipoArchivo)
                            .build(),
                    RequestBody.fromBytes(archivo.getBytes())
            );

        } catch (IOException | SdkException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al subir el documento", e);
        }

        Documento documento = new Documento();

        documento.setNombre(nombre);
        documento.setTipoArchivo(tipoArchivo);
        documento.setRutaArchivo(rutaArchivo);
        documento.setFechaSubida(LocalDate.now());
        documento.setUsuario(usuario);

        try {
            return convertirDTO(documentoRepository.save(documento));
        } catch (RuntimeException e) {

            try {
                s3Client.deleteObject(
                        DeleteObjectRequest.builder()
                                .bucket(bucket)
                                .key(rutaArchivo)
                                .build()
                );
            } catch (SdkException error) {
                e.addSuppressed(error);
            }

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al registrar el documento", e);
        }
    }

    public byte[] descargar(Integer id) {

        Documento documento = obtenerDocumento(id);

        try {
            return s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(documento.getRutaArchivo())
                            .build()
            ).asByteArray();

        } catch (SdkException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al descargar el documento", e);
        }
    }

    public void eliminar(Integer id) {

        Documento documento = obtenerDocumento(id);

        try {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucket)
                            .key(documento.getRutaArchivo())
                            .build()
            );

        } catch (SdkException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar el archivo", e);
        }

        documentoRepository.delete(documento);
    }

    private Documento obtenerDocumento(Integer id) {

        return documentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Documento no encontrado"));
    }

    private DocumentoDTO convertirDTO(Documento documento) {

        DocumentoDTO dto = new DocumentoDTO();

        dto.setId(documento.getId());
        dto.setNombre(documento.getNombre());
        dto.setTipoArchivo(documento.getTipoArchivo());
        dto.setFechaSubida(documento.getFechaSubida());
        dto.setUsuarioRut(documento.getUsuario().getRut());

        return dto;
    }
}