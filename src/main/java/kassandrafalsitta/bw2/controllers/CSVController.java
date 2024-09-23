package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.services.ComuneService;
import kassandrafalsitta.bw2.services.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/importData")
public class CSVController {

    @Autowired
    private ProvinciaService provinciaService;

    @Autowired
    private ComuneService comuneService;

    @PostMapping()
    public String importCSVFiles() {
        try {
            // Preleviamo i file dalla cartella resources/CSVs
            File provinciaFile = new ClassPathResource("CSVs/province-italiane.csv").getFile();
            File comuniFile = new ClassPathResource("CSVs/comuni-italiani.csv").getFile();

            // forniamo i dati ai service che si occupano di salvare i dati
            provinciaService.importProvinceCSV(provinciaFile.getAbsolutePath());
            comuneService.importComuniCSV(comuniFile.getAbsolutePath());

            return "L'import dei Dati di Province and Comuni è andato a buon fine!";
        } catch (Exception e) {
            return "Errore nell'import dai file CSV: " + e.getMessage();
        }
    }
}
