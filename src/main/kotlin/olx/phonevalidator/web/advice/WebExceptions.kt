package olx.phonevalidator.web.advice

open class NotFoundException(
    id: String?
) : RuntimeException("File ID not found: $id")
