package kassandrafalsitta.bw2.services;

import kassandrafalsitta.bw2.entities.Fattura;
import kassandrafalsitta.bw2.repositories.FatturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FatturaService {
    @Autowired
    private FatturaRepository fatturaRepository;
    @Autowired
    private PasswordEncoder bcrypt;

    public List<Fattura> getFattureFiltraPerStato(String stato){
        return fatturaRepository.filterByStato(stato);
    }

    public List<Fattura> getFattureFiltraPerData(LocalDate data){
        return fatturaRepository.filterByData(data);
    }

    public List<Fattura> getFattureFiltraPerAnno(LocalDate startDate, LocalDate endDate){
        return fatturaRepository.filterByAnno(startDate, endDate);
    }

    public List<Fattura> getFattureFiltraPerRangeImporti(BigDecimal minimo, BigDecimal massimo){
        return fatturaRepository.filterByRangeImporto(minimo, massimo);
    }

}
