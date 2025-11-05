package domain.exception

class InvalidUrlException(message: String = "The provided URL is not valid.") : RuntimeException(message)