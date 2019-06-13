package olx.phonevalidator.domain

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable
import java.nio.ByteBuffer
import java.util.Base64
import java.util.UUID

/**
 * URL friendly key generator.
 *
 */
class UrlKeyGenerator : IdentifierGenerator {
    override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): Serializable {
        return getRandomAlphaNumericString(UUID.randomUUID()).substring(0, 11)
    }

    private fun getRandomAlphaNumericString(uuid: UUID): String {
        val byteBuffer = ByteBuffer.allocate(16)
        byteBuffer.putLong(uuid.mostSignificantBits)
        byteBuffer.putLong(uuid.leastSignificantBits)
        return String(Base64.getUrlEncoder().encode(byteBuffer.array()))
            .replace("=", "")
    }
}
