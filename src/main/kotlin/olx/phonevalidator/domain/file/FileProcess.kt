package olx.phonevalidator.domain.file

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import olx.phonevalidator.domain.detail.ProcessDetail
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * Processing result for a given phone number file.
 */
@JsonIgnoreProperties("processDetails")
@Entity
@Table(name = "file_process")
@EntityListeners(AuditingEntityListener::class)
data class FileProcess(

    @field:Id
    @field:GeneratedValue(generator = "UrlKey")
    @field:GenericGenerator(
        name = "UrlKey",
        strategy = "olx.phonevalidator.domain.UrlKeyGenerator"
    )
    val id: String? = null,

    val originalFilename: String,

    @JsonIgnore
    @field:OneToMany(
        fetch = FetchType.EAGER,
        cascade = [(CascadeType.ALL)],
        mappedBy = "fileProcess"
    )
    val processDetails: List<ProcessDetail>? = emptyList(),

    val fixedCount: Int = 0,
    val validCount: Int = 0,
    val invalidCount: Int = 0,
    val createdCount: Int = 0,
    val updatedCount: Int = 0,

    @field:CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
)