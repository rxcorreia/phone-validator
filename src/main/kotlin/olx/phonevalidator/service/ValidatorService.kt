package olx.phonevalidator.service

import olx.phonevalidator.domain.detail.ProcessDetail
import olx.phonevalidator.domain.detail.ProcessDetailRepository
import olx.phonevalidator.domain.detail.ValidationStatus
import olx.phonevalidator.domain.file.FileProcess
import olx.phonevalidator.domain.file.FileProcessRepository
import olx.phonevalidator.web.advice.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

@Service
class ValidatorService(
    val phoneNumberValidator: PhoneNumberValidator,
    val fileProcessRepository: FileProcessRepository,
    val processDetailRepository: ProcessDetailRepository
) {

    fun validateNumber(phoneNumber: String): CompletableFuture<ProcessDetail> {
        return CompletableFuture.supplyAsync { validateNumberAsync(phoneNumber) }
    }

    fun processFile(file: MultipartFile): CompletableFuture<FileProcess> {
        return CompletableFuture.supplyAsync { processFileAsync(file) }
    }

    private fun validateNumberAsync(phoneNumber: String): ProcessDetail {
        return phoneNumberValidator.validate(phoneNumber)
    }

    private fun processFileAsync(file: MultipartFile): FileProcess {
        val uuid = uuid()

        validateFile(file)

        // Parse file
        val fileLines = readFile(file)

        val processDetails = fileLines?.mapNotNull { line ->
            processCsvLine(line)
        }

        val fileProcessEntity = fileProcessRepository.saveAndFlush(
            FileProcess(
                id = uuid,
                originalFilename = file.originalFilename ?: "internal_$uuid.csv"
            )
        )

        val counter = AtomicInteger(0)

        return processDetails?.parallelStream()?.forEach { processDetail ->
            if (!processDetailRepository.existsById(processDetail.id!!))
                counter.getAndIncrement()

            processDetailRepository.save(processDetail.copy(fileProcess = fileProcessEntity))
        }
            .let { summarizeProcessResults(fileProcessEntity, processDetails, counter.get()) }
            .also { fileProcessRepository.saveAndFlush(it) }
            .let { it.copy(processDetails = processDetails) }
    }

    /**
     * Calculate statistical info for file processing results
     */
    private fun summarizeProcessResults(
        fileProcessEntity: FileProcess,
        processDetails: List<ProcessDetail>?,
        createdCount: Int
    ): FileProcess {
        val processDetailsStatusMap = processDetails?.groupBy { it.status }

        return when (processDetails) {
            null -> fileProcessEntity
            else -> fileProcessEntity.copy(
                validCount = processDetailsStatusMap?.get(ValidationStatus.VALID)?.size ?: 0,
                fixedCount = processDetailsStatusMap?.get(ValidationStatus.FIXED)?.size ?: 0,
                invalidCount = processDetailsStatusMap?.get(ValidationStatus.INVALID)?.size ?: 0,
                createdCount = createdCount,
                updatedCount = processDetails.size - createdCount
            )
        }
    }

    private fun readFile(file: MultipartFile): List<String>? {
        return BufferedReader(InputStreamReader(file.inputStream, StandardCharsets.UTF_8)).lines()
            .collect(Collectors.toList())
    }

    private fun processCsvLine(line: String): ProcessDetail? {
        return when (!line.contains("id")) {
            true -> {
                val tokens = line.split(",")
                val csvLine = when (tokens.size) {
                    2 -> CsvLineValue(tokens[0], tokens[1])
                    // Invalid CSV line, throw exception to force 500/BAD_REQUEST error
                    else -> throw IllegalArgumentException("Invalid file content format")
                }


                phoneNumberValidator.validate(csvLine.let { it.phoneNumber } ?: "")
                    .copy(id = tokens[0]) // Use original ID from CSV file to uniquely identify entry
            }
            false -> null
        }
    }

    fun getProcessFile(fileProcessId: String): CompletableFuture<FileProcess> {
        return CompletableFuture.supplyAsync {
            if (!fileProcessRepository.existsById(fileProcessId)) {
                throw NotFoundException(fileProcessId)
            }
            fileProcessRepository.getOne(fileProcessId)
        }
    }
}

/**
 * Random UUID generator function
 */
fun uuid(): String = UUID.randomUUID().toString()

internal fun validateFile(file: MultipartFile) {
    if (file.isEmpty)
        throw IllegalArgumentException("File is empty")

    if (!"text/csv".equals(file.contentType, true))
        throw IllegalArgumentException("Invalid mime type ${file.contentType}, only text/csv is allowed")
}

data class CsvLineValue(
    val id: String?,
    val phoneNumber: String?
)
