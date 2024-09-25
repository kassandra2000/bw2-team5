package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Provincia;
import kassandrafalsitta.bw2.repositories.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    // Correzione Province
    private final Map<String, String> CORREZIONI_NOMI_PROVINCE = new HashMap<>();

    {
        CORREZIONI_NOMI_PROVINCE.put("La-spezia", "La Spezia");
        CORREZIONI_NOMI_PROVINCE.put("Reggio-Calabria", "Reggio Calabria");
        CORREZIONI_NOMI_PROVINCE.put("Carbonia Iglesias", "Sud Sardegna");
        CORREZIONI_NOMI_PROVINCE.put("Medio Campidano", "Sud Sardegna");
        CORREZIONI_NOMI_PROVINCE.put("Bolzano", "Bolzano/Bozen");
        CORREZIONI_NOMI_PROVINCE.put("Aosta", "Valle d'Aosta/Vallée d'Aoste");
        CORREZIONI_NOMI_PROVINCE.put("Verbania", "Verbano-Cusio-Ossola");
        CORREZIONI_NOMI_PROVINCE.put("Reggio-Emilia", "Reggio nell'Emilia");
        CORREZIONI_NOMI_PROVINCE.put("Vibo-Valentia", "Vibo Valentia");
        CORREZIONI_NOMI_PROVINCE.put("Forli-Cesena", "Forlì-Cesena");
        CORREZIONI_NOMI_PROVINCE.put("Pesaro-Urbino", "Pesaro e Urbino");
        CORREZIONI_NOMI_PROVINCE.put("Ascoli-Piceno", "Ascoli Piceno");
        CORREZIONI_NOMI_PROVINCE.put("Ogliastra", "Nuoro");
        CORREZIONI_NOMI_PROVINCE.put("Olbia Tempio", "Sassari");
        CORREZIONI_NOMI_PROVINCE.put("Monza-Brianza", "Monza e della Brianza");
        //CORREZIONI_NOMI_PROVINCE.put("", "");
    }

    // Correzione Sigle
    private final Map<String, String> SIGLE_PROVINCE = Map.of(
            "Sud Sardegna", "SU",
            "Verbano-Cusio-Ossola", "VB"
    );

    public void importProvinceCSV(String filePath) {
        List<Provincia> province = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String riga;
            reader.readLine(); //Salta la prima riga che contiene e i nomi dei campi

            while ((riga = reader.readLine()) != null) {
                String[] casella = riga.split(";");

                if (casella.length >= 3) {
                    String nomeProvincia = casella[1];
                    //Se nomeProvincia contiene un nome sbagliato lo corregge, se no tiene quello passato
                    String nomeCorretto = CORREZIONI_NOMI_PROVINCE.getOrDefault(nomeProvincia, nomeProvincia);

                    // Se esiste già una provincia con il nome corretto non ne aggiunge una copia
                    //(es. non saranno create più Sud Sardegna)
                    Optional<Provincia> provinciaEsistente = provinciaRepository.findByNome(nomeCorretto);

                    if (provinciaEsistente.isEmpty()) {
                        Provincia provincia = new Provincia();
                        provincia.setSigla(SIGLE_PROVINCE.getOrDefault(nomeCorretto, casella[0]));
                        provincia.setNome(nomeCorretto);
                        provincia.setRegione(casella[2]);
                        province.add(provincia);
                    }
                }
            }

            provinciaRepository.saveAll(province);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isProvinceTableEmpty() {
        return provinciaRepository.count() == 0;
    }
}