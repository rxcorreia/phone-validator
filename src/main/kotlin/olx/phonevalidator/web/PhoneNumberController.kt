package olx.phonevalidator.web

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import olx.phonevalidator.service.ValidatorService
import olx.phonevalidator.web.responses.FileProcessResponse
import olx.phonevalidator.web.responses.ProcessDetailResponse
import java.util.concurrent.CompletableFuture

/**
 * A REST Controller exposing public API for all relevant mobile phone validation endpoints:
 * - File processor
 * - Processed file info
 * - Single phone number validation
 */
@RestController
@Validated
@RequestMapping(
    value = ["/api/v1/phone-numbers"]
)
class FileUploadController(
    private val validatorService: ValidatorService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(FileUploadController::class.java)
    }

    @GetMapping(
        value = ["/{number}"],
        produces = [(APPLICATION_JSON_VALUE)]
    )
    fun validateNumber(
        @PathVariable number: String
    ): CompletableFuture<ResponseEntity<ProcessDetailResponse>> {
        return validatorService.validateNumber(number).thenApplyAsync { validationResult ->
            ResponseEntity(ProcessDetailResponse.fromModel(validationResult), HttpStatus.OK)
        }
    }

    @GetMapping(
        value = ["/file/{id}"],
        produces = [(APPLICATION_JSON_VALUE)]
    )
    fun getProcessedFile(
        @PathVariable id: String
    ): CompletableFuture<ResponseEntity<FileProcessResponse>>? {
        // Validate File ID exist
        // Get upload results for file
        return validatorService.getProcessFile(id).thenApplyAsync { validationResult ->
            ResponseEntity(FileProcessResponse.fromModel(validationResult), HttpStatus.OK)
        }
    }

    @PostMapping(
        value = ["/file"],
        produces = [(APPLICATION_JSON_VALUE)]
    )
    fun handleFileUpload(
        @RequestParam("file") file: MultipartFile
    ): CompletableFuture<ResponseEntity<FileProcessResponse>> {
        return validatorService.processFile(file).thenApplyAsync { processResult ->
            ResponseEntity(FileProcessResponse.fromModel(processResult), HttpStatus.CREATED)
        }
    }
}
