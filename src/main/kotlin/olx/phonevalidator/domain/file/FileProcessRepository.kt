package olx.phonevalidator.domain.file

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FileProcessRepository : JpaRepository<FileProcess, String> {
    fun findOneByOriginalFilename(originalFilename: String): FileProcess
}
