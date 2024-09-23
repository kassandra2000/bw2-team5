package kassandrafalsitta.bw2.services;

import com.opencsv.CSVReader;
import kassandrafalsitta.bw2.entities.Provincia;
import kassandrafalsitta.bw2.repositories.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public void importProvinceCSV(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] riga;
            List<Provincia> province = new ArrayList<>();
            reader.readNext(); // Salta la prima riga del file perchè usata per i nomi dei campi
            while ((riga = reader.readNext()) != null) {
                Provincia provincia = new Provincia();
                provincia.setSigla(riga[0]);
                provincia.setNome(riga[1]);
                provincia.setRegione(riga[2]);
                province.add(provincia);
            }
            provinciaRepository.saveAll(province);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
