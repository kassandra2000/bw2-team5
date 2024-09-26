package kassandrafalsitta.bw2.runners;

import kassandrafalsitta.bw2.services.ComuneService;
import kassandrafalsitta.bw2.services.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DatabaseInit implements CommandLineRunner {

    @Autowired
    private ComuneService comuneService;

    @Autowired
    private ProvinciaService provinciaService;

    @Override
    public void run(String... args) throws Exception {
        if (provinciaService.isProvinceTableEmpty()) {
            File provinciaFile = new ClassPathResource("CSVs/province-italiane.csv").getFile();
            provinciaService.importProvinceCSV(provinciaFile.getAbsolutePath());
        }
        if (comuneService.isComuniTableEmpty()) {
            File comuniFile = new ClassPathResource("CSVs/comuni-italiani.csv").getFile();
            comuneService.importComuniCSV(comuniFile.getAbsolutePath());
        }
    }
}