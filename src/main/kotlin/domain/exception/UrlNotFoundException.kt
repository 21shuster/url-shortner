package domain.exception

class UrlNotFoundException(message: String = "URL not found.") : RuntimeException(message)