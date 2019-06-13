package olx.phonevalidator.web.responses

import olx.phonevalidator.domain.detail.ProcessDetail
import olx.phonevalidator.domain.detail.ValidationError
import olx.phonevalidator.domain.detail.ValidationStatus
import olx.phonevalidator.domain.file.FileProcess
import java.time.LocalDateTime

/**
 * API response objects.
 */
data class FileProcessResponse(
    val id: String,
    val originalFilename: String,
    val processDetails: List<ProcessDetailResponse>,
    val processSummary: ProcessSummaryResponse? = null
) {
    companion object {
        fun fromModel(fileProcessing: FileProcess): FileProcessResponse {
            with(fileProcessing) {
                return FileProcessResponse(
                    id = id!!,
                    originalFilename = originalFilename,
                    processDetails = processDetails?.map {
                        ProcessDetailResponse.fromModel(
                            it
                        )
                    }
                        ?: emptyList(),
                    processSummary = ProcessSummaryResponse(
                        valid = validCount,
                        fixed = fixedCount,
                        invalid = invalidCount,
                        created = createdCount,
                        updated = updatedCount
                    )
                )
            }
        }
    }
}

/**
 * Auxiliary class to hold processing statistical info
 */
data class ProcessSummaryResponse(
    val fixed: Int = 0,
    val valid: Int = 0,
    val invalid: Int = 0,
    val created: Int = 0,
    val updated: Int = 0
)

/**
 * The file processing detail, per processed CSV line
 */
data class ProcessDetailResponse(
    val id: String?,
    val originalValue: String,
    val processedValue: String?,
    val status: ValidationStatus,
    val error: ValidationErrorResponse?,
    val createdAt: LocalDateTime? = null
) {
    companion object {
        fun fromModel(processDetail: ProcessDetail): ProcessDetailResponse {
            with(processDetail) {
                return ProcessDetailResponse(
                    id = id,
                    originalValue = originalValue,
                    processedValue = processedValue,
                    status = status,
                    error = ValidationErrorResponse.fromModel(reason),
                    createdAt = createdAt
                )
            }
        }
    }
}

data class ValidationErrorResponse(
    val code: String,
    val message: String
) {
    companion object {
        fun fromModel(validationResult: ValidationError?): ValidationErrorResponse? {
            return validationResult?.let {
                with(validationResult) {
                    return ValidationErrorResponse(
                        code = code,
                        message = message
                    )
                }
            }
        }
    }
}