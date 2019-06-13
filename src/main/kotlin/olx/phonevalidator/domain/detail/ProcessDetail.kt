package olx.phonevalidator.domain.detail

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import olx.phonevalidator.domain.file.FileProcess
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * The validation process details for given phone number.
 */
@JsonIgnoreProperties("")
@Entity
@Table(name = "process_detail")
@EntityListeners(AuditingEntityListener::class)
data class ProcessDetail(
    @field:Id
    val id: String? = null,

    val originalValue: String,

    val processedValue: String? = null,

    @field:Enumerated(EnumType.STRING)
    val status: ValidationStatus = ValidationStatus.VALID,

    @field:Enumerated(EnumType.STRING)
    val reason: ValidationError? = null,

    @field:ManyToOne
    @field:JoinColumn(name = "file_process_id")
    val fileProcess: FileProcess? = null,

    @field:CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
) {

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is ProcessDetail -> (
                other.fileProcess?.id == this.fileProcess?.id &&
                    other.id == this.id &&
                    other.status == this.status &&
                    other.reason == this.reason &&
                    other.originalValue == this.originalValue &&
                    other.processedValue == this.processedValue
                )
            else -> false
        }
    }
}

enum class ValidationStatus {
    VALID,
    INVALID,
    FIXED
}

enum class ValidationError(
    val code: String,
    val message: String
) {
    INVALID_NUMBER_FORMAT("INVALID_NUMBER_FORMAT", "Phone number format provided is invalid"),
    EMPTY_NUMBER_VALUE("EMPTY_NUMBER_VALUE", "Empty phone number value"),
    FIXED_REMOVED_SUFFIX("FIXED_REMOVED_SUFFIX", "Phone number fixed by removal of extra suffix")
}
