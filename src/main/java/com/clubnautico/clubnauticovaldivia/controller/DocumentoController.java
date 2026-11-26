package com.clubnautico.clubnauticovaldivia.controller;

import java.util.List;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.clubnautico.clubnauticovaldivia.DTO.DocumentoDTO;
import com.clubnautico.clubnauticovaldivia.service.DocumentoService;

import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v1/documentos")
@Tag(name = "Documentos")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;

    @GetMapping
    public ResponseEntity<CollectionModel<DocumentoDTO>> listar() {

        List<DocumentoDTO> documentos = documentoService.listar();

        CollectionModel<DocumentoDTO> recursos =
                CollectionModel.of(documentos);

        recursos.add(linkTo(DocumentoController.class)
                .withSelfRel());

        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DocumentoDTO>> buscarPorId(
            @PathVariable Integer id) {

        DocumentoDTO documento = documentoService.buscarPorId(id);

        EntityModel<DocumentoDTO> recurso =
                EntityModel.of(documento);

        recurso.add(linkTo(DocumentoController.class)
                .slash(id).withSelfRel());

        recurso.add(linkTo(DocumentoController.class)
                .slash("descargar").slash(id)
                .withRel("descargar"));

        return new ResponseEntity<>(recurso, HttpStatus.OK);
    }

    @GetMapping("/usuario/{rut}")
    public ResponseEntity<CollectionModel<DocumentoDTO>> buscarPorRut(
            @PathVariable String rut) {

        List<DocumentoDTO> documentos =
                documentoService.buscarPorRut(rut);

        CollectionModel<DocumentoDTO> recursos =
                CollectionModel.of(documentos);

        recursos.add(linkTo(DocumentoController.class)
                .slash("usuario").slash(rut).withSelfRel());

        return new ResponseEntity<>(recursos, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EntityModel<DocumentoDTO>> guardar(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("rut") String rut) {

        DocumentoDTO documento =
                documentoService.guardar(archivo, rut);

        EntityModel<DocumentoDTO> recurso =
                EntityModel.of(documento);

        recurso.add(linkTo(DocumentoController.class)
                .slash(documento.getId()).withSelfRel());

        recurso.add(linkTo(DocumentoController.class)
                .slash("descargar").slash(documento.getId())
                .withRel("descargar"));

        return new ResponseEntity<>(recurso, HttpStatus.CREATED);
    }

    //Descargar un documento
    @GetMapping("/descargar/{id}")
    public ResponseEntity<byte[]> descargar(
            @PathVariable Integer id) {

        DocumentoDTO documento =
                documentoService.buscarPorId(id);

        byte[] archivo = documentoService.descargar(id);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_OCTET_STREAM);

        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(documento.getNombre(),
                                StandardCharsets.UTF_8)
                        .build());

        headers.setContentLength(archivo.length);
        headers.add("X-Content-Type-Options", "nosniff");
        headers.add("Cache-Control", "no-store");

        return new ResponseEntity<>(
                archivo, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id) {

        documentoService.eliminar(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}