/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils.services;

import com.mass.sica.publication.entities.Publication;
import com.mass.sica.publication.utils.ExportPdf;
import com.mass.sica.publication.utils.PDFMerger;
import com.mass.sica.utils.exception.FileStorageException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Aristide MASSAGA
 */
@Service
@Slf4j
public class FileStorageService {

    private final Path fileStorageLocation;

    @Value("${file.url}")
    private String SERVER_URL;
    
    @Value("${app.host}")
    private String HOST;

    public FileStorageService(@Value("${file.upload-dir}") String UPLOAD_DIR) {

        this.fileStorageLocation = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation.resolve("publications/").normalize());
            Files.createDirectories(this.fileStorageLocation.resolve("magazines/").normalize());
            Files.createDirectories(this.fileStorageLocation.resolve("tmp/").normalize());
        } catch (IOException ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public ResponseEntity<?> uploadFile(MultipartFile file, String service) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve("tmp" + File.separator + fileName).normalize();

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("uploadFile id : {} , filename : {}", fileName);

            return ResponseEntity.ok().build();

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * enregistrer les fichiers
     *
     * @param fichier
     * @param dossier
     * @param id
     * @return
     * @throws java.io.IOException
     */
    public String moveFilePublication(String fichier, String dossier, String id) throws IOException {

        //Création du dossier portant l'id du contractant en question
        boolean f = new File(this.fileStorageLocation.toString() + File.separator + dossier + File.separator + id).mkdir();

        String destination = dossier + File.separator + id + File.separator + fichier;

        Files.move(
                this.fileStorageLocation.resolve("tmp" + File.separator + fichier).normalize(),
                this.fileStorageLocation.resolve(destination).normalize(),
                StandardCopyOption.REPLACE_EXISTING);
        return SERVER_URL + destination;
    }

    /**
     * convert first page of pdf to image
     *
     * @param path
     * @param id
     * @param name
     * @return
     * @throws IOException
     */
    public String pdfToImage(String path, String id, String name) throws IOException {
        try (final PDDocument document = PDDocument.load(new File(this.fileStorageLocation.resolve(path + File.separator + name).normalize().toString()))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 40, ImageType.RGB);
            String fileName = this.fileStorageLocation.resolve(path + File.separator + "image-" + id + ".png").normalize().toString();
            ImageIOUtil.writeImage(bim, fileName, 40);
            document.close();
            return SERVER_URL + path + File.separator + "image-" + id + ".png";
        } catch (IOException e) {
            System.err.println("Exception while trying to create pdf document - " + e);
        }
        return null;
    }

    public String mergeInto(String destination, String page, String publication) throws IOException {

        String[] files = {this.fileStorageLocation.resolve(page).normalize().toString(),
            this.fileStorageLocation.resolve(publication).normalize().toString()};

        PDFMerger.mergeInto(this.fileStorageLocation.resolve(destination).normalize().toString(), files);
        return SERVER_URL + destination;
    }

    /**
     * Ce code n'est appelable que par un éditeur.Il permet de produire la fiche
     * de validation de dépôt de la publication sur la plateforme, par un
     * éditeur
     *
     * @param pub
     * @param path
     * @return
     * @throws java.lang.Exception
     */
    public String makeSicaPubPage(Publication pub, String path) throws Exception {

        String qrText = SERVER_URL + "consulter/publication/" + pub.getId();

        String destination = path + "page.pdf";

        ExportPdf export = new ExportPdf();
        export.generatePdf(this.fileStorageLocation.resolve(destination).normalize().toString(), qrText, pub, HOST);

        return destination;
    }

    /*public List<Fichier> miseAjourFichiers(List<Fichier> fichiers, List<Fichier> presents, String dos, String id) {

        List<Fichier> olds = fichiers.stream().filter(item -> item.getId() != null).collect(Collectors.toList());

        List<Fichier> news = fichiers.stream().filter(item -> item.getId() == null).collect(Collectors.toList());

        List<Fichier> retires = presents
                .stream()
                .filter(item -> olds.stream().filter(fich -> Objects.equals(fich.getId(), item.getId())).findFirst().orElse(null) == null)
                .collect(Collectors.toList());

        if (!news.isEmpty()) {
            news = this.saveFichiers(news, dos, id);
            olds.addAll(news);
        }

        if (!retires.isEmpty()) {
            for (Fichier fichier : retires) {
                fichier.setPublished(false);
            }
            log.info("enregistrement des fichiers : {}", retires);
            this.fileRepository.saveAll(retires);
            olds.addAll(retires);
        }
        return olds;
    }

    public ResponseEntity<?> uploadMultipleFiles(MultipartFile[] files, String service) {

        for (MultipartFile file : files) {
            uploadFile(file, service);
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> deleteFile(Long fileId) {
        Fichier fichier = fileRepository.getOne(fileId);

        fichier.setPublished(false);
        fileRepository.save(fichier);
        return ResponseEntity.ok().build();
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public Fichier getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }

    public ResponseEntity<Resource> downloadFile(Long id, HttpServletRequest request) {

        Fichier fichier = this.getFile(id);

        // Load file as Resource
        Resource resource = this.loadFileAsResource(fichier.getName());

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }*/
}
