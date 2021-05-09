package sellerofair.Translator;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@RestController
class RequestController {

    private final RequestRepository repository;
    Logger logger;

    RequestController(RequestRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/translator")
    List<Request> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/translator")
    String newRequest(@RequestBody Input newInput, HttpServletRequest request) {

        Date time = new Date();

        String IP = request.getRemoteAddr();

        try {

            YandexAPIManager yandexAPIManager = new YandexAPIManager(
                    "",  // TODO Input correct OAuth-token
                    "");  // TODO Input correct folderID

            String result = yandexAPIManager.translate(
                    newInput.getFrom(),
                    newInput.getTo(),
                    newInput.getText()
            );

            System.out.println(repository.save(new Request(time.toString(),
                    IP, newInput, ConnectionResult.SUCCESS)));

            return "{\"text\"=\"" + result + "\"}";

        } catch (IOException e) {

            System.out.println("Bad connection with " +
                    repository.save(new Request(time.toString(),
                            IP, newInput, ConnectionResult.FAIL)));
            e.printStackTrace();

            return "{\"text\"=\"\"}";
        }
    }
}
