package nl.ecbp.dare2date.Registration.endpoint;

import nl.ecbp.dare2date.Registration.*;
import nl.ecbp.dare2date.Registration.entity.Calculation;
import nl.ecbp.dare2date.Registration.entity.CalculationRepository;
import nl.ecbp.dare2date.Registration.gateway.IMoviePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class LidWordenEndpoint {
    private static final String NAMESPACE_URI = "http://www.han.nl/registration";

    private CalculationRepository calculationDao;
    private IMoviePrinter moviePrinter;

    @Autowired
    public LidWordenEndpoint(CalculationRepository calculationDao, IMoviePrinter moviePrinter)
    {
        this.calculationDao = calculationDao;
        this.moviePrinter = moviePrinter;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrationDataRequest")
    @ResponsePayload
    public RegistrationDataResponse calculateSumForName(@RequestPayload RegistrationDataRequest req) {       

        RegistrationDataResult result = new RegistrationDataResult();
        result.setMessage("Here are the results of the jury for the calculation " + req.getInput().getName().getVoornaam() + " " + req.getInput().getName().getAchternaam());
        result.setValue(1);
        RegistrationDataResponse resp = new RegistrationDataResponse();
        resp.setResult(result);

        return resp;
    }
}
