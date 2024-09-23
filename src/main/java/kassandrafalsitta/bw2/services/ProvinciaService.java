package kassandrafalsitta.bw2.services;


import kassandrafalsitta.bw2.entities.Provincia;
import kassandrafalsitta.bw2.repositories.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public void importProvinceCSV(String filePath) {
        List<Provincia> province = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String riga;
            reader.readLine(); // Salta la prima riga del file perchè usata per i nomi dei campi

            while ((riga = reader.readLine()) != null) {
                String[] casella = riga.split(";");

                if (casella.length >= 3) {
                    Provincia provincia = new Provincia();
                    provincia.setSigla(casella[0]);
                    provincia.setNome(casella[1]);
                    provincia.setRegione(casella[2]);
                    province.add(provincia);
                }
            }

            provinciaRepository.saveAll(province);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
