package olx.phonevalidator.domain.file

import olx.phonevalidator.domain.EmbeddedDataSourceConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@Import(EmbeddedDataSourceConfig::class)
@AutoConfigureTestDatabase(replace = NONE)
@Transactional(propagation = Propagation.NEVER)
@DataJpaTest
@ActiveProfiles("test")
class FileProcessRepositoryTest {

    @Autowired
    private lateinit var fileProcessRepository: FileProcessRepository

    @Before
    fun setUp() {

        fileProcessRepository.deleteAll()
    }

    @After
    fun cleanDB() {
        fileProcessRepository.deleteAll()
    }

    @Test
    fun shouldCreateNewFileProcess() {
        val fileProcess = fileProcessRepository.saveAndFlush(
            FileProcess(
                originalFilename = "test_file.csv",
                validCount = 1,
                invalidCount = 2,
                fixedCount = 1,
                createdCount = 3,
                updatedCount = 1
            )
        )

        assertThat(fileProcess.id).isNotNull()

        val fileProcessLoaded = fileProcessRepository.findById(fileProcess.id!!).get()

        assertThat(fileProcessLoaded.createdAt).isNotNull()
        assertThat(fileProcessLoaded.id).isEqualTo(fileProcess.id)
        assertThat(fileProcessLoaded.originalFilename).isEqualTo(fileProcess.originalFilename)

        assertThat(fileProcessLoaded.validCount).isEqualTo(fileProcess.validCount)
        assertThat(fileProcessLoaded.fixedCount).isEqualTo(fileProcess.fixedCount)
        assertThat(fileProcessLoaded.invalidCount).isEqualTo(fileProcess.invalidCount)
        assertThat(fileProcessLoaded.createdCount).isEqualTo(fileProcess.createdCount)
        assertThat(fileProcessLoaded.updatedCount).isEqualTo(fileProcess.updatedCount)
    }
}
