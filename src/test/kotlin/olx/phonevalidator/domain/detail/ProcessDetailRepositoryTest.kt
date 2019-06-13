package olx.phonevalidator.domain.detail

import olx.phonevalidator.domain.EmbeddedDataSourceConfig
import olx.phonevalidator.domain.file.FileProcess
import olx.phonevalidator.domain.file.FileProcessRepository
import olx.phonevalidator.service.uuid
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
class ProcessDetailRepositoryTest {

    @Autowired
    private lateinit var processDetailRepository: ProcessDetailRepository

    @Autowired
    private lateinit var fileProcessRepository: FileProcessRepository

    private lateinit var baseFileProcess: FileProcess

    private lateinit var baseProcessDetails: List<ProcessDetail>

    @Before
    fun setUp() {
        processDetailRepository.deleteAll()
        fileProcessRepository.deleteAll()


        baseFileProcess = fileProcessRepository.saveAndFlush(
            FileProcess(
                originalFilename = "test_file.csv"
            )
        )

        baseProcessDetails = listOf(
            ProcessDetail(
                id = uuid(),
                originalValue = "123",
                processedValue = "123",
                status = ValidationStatus.VALID,
                fileProcess = baseFileProcess
            ),
            ProcessDetail(
                id = uuid(),
                originalValue = "123_XYZ",
                processedValue = "123",
                status = ValidationStatus.FIXED,
                reason = ValidationError.FIXED_REMOVED_SUFFIX,
                fileProcess = baseFileProcess
            ),
            ProcessDetail(
                id = uuid(),
                originalValue = "INVALID_FORMAT",
                status = ValidationStatus.INVALID,
                reason = ValidationError.INVALID_NUMBER_FORMAT,
                fileProcess = baseFileProcess
            ),
            ProcessDetail(
                id = uuid(),
                originalValue = "",
                status = ValidationStatus.INVALID,
                reason = ValidationError.EMPTY_NUMBER_VALUE,
                fileProcess = baseFileProcess
            )
        )
    }

    @After
    fun cleanDB() {
        fileProcessRepository.deleteAll()
        processDetailRepository.deleteAll()
    }

    @Test
    fun shouldCreateNewProcessDetailsForFileProcess() {
        val savedProcessDetails = baseProcessDetails.map { processDetailRepository.saveAndFlush(it) }

        assertThat(savedProcessDetails.size).isEqualTo(baseProcessDetails.size)
        assertThat(savedProcessDetails).containsAll(baseProcessDetails)
    }
}
