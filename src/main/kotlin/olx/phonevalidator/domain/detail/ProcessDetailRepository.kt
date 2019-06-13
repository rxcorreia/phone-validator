package olx.phonevalidator.domain.detail

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProcessDetailRepository : JpaRepository<ProcessDetail, String> {
    fun findOneById(id: String): ProcessDetail
}
