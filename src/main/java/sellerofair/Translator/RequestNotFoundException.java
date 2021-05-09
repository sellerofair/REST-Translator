package sellerofair.Translator;

class RequestNotFoundException extends RuntimeException {

    RequestNotFoundException(Long id) {
        super("Could not find request " + id);
    }
}