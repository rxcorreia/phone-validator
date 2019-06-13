package olx.phonevalidator.service

import olx.phonevalidator.domain.detail.ValidationError
import olx.phonevalidator.domain.detail.ValidationStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class PhoneNumberValidatorTest {

    @InjectMocks
    private lateinit var phoneNumberValidator: SouthAfricaMobilePhoneValidator

    @Test
    fun shouldValidateValidPhoneNumber() {
        val phoneNumber = "27736529279"
        val processDetail = phoneNumberValidator.validate(phoneNumber)

        assertThat(processDetail).isNotNull
        assertThat(processDetail.originalValue).isEqualTo(phoneNumber)
        assertThat(processDetail.processedValue).isEqualTo(phoneNumber)
        assertThat(processDetail.status).isEqualTo(ValidationStatus.VALID)
        assertThat(processDetail.id).isNull()
        assertThat(processDetail.fileProcess).isNull()
        assertThat(processDetail.reason).isNull()
        assertThat(processDetail.createdAt).isNotNull()
    }

    @Test
    fun shouldReturnInvalidStatusForEmptyPhoneNumber() {
        val phoneNumber = ""
        val processDetail = phoneNumberValidator.validate(phoneNumber)

        assertThat(processDetail).isNotNull
        assertThat(processDetail.originalValue).isEqualTo(phoneNumber)
        assertThat(processDetail.processedValue).isNull()
        assertThat(processDetail.status).isEqualTo(ValidationStatus.INVALID)
        assertThat(processDetail.id).isNull()
        assertThat(processDetail.fileProcess).isNull()
        assertThat(processDetail.reason).isEqualTo(ValidationError.EMPTY_NUMBER_VALUE)
        assertThat(processDetail.createdAt).isNotNull()
    }

    @Test
    fun shouldReturnInvalidStatusForIncorrectPhoneNumber() {
        val phoneNumber = "260955751013"
        val processDetail = phoneNumberValidator.validate(phoneNumber)

        assertThat(processDetail).isNotNull
        assertThat(processDetail.originalValue).isEqualTo(phoneNumber)
        assertThat(processDetail.processedValue).isNull()
        assertThat(processDetail.status).isEqualTo(ValidationStatus.INVALID)
        assertThat(processDetail.id).isNull()
        assertThat(processDetail.fileProcess).isNull()
        assertThat(processDetail.reason).isEqualTo(ValidationError.INVALID_NUMBER_FORMAT)
        assertThat(processDetail.createdAt).isNotNull()
    }

    @Test
    fun shouldReturnInvalidStatusForTooLongPhoneNumber() {
        val phoneNumber = "27736529279123213"
        val processDetail = phoneNumberValidator.validate(phoneNumber)

        assertThat(processDetail).isNotNull
        assertThat(processDetail.originalValue).isEqualTo(phoneNumber)
        assertThat(processDetail.processedValue).isNull()
        assertThat(processDetail.status).isEqualTo(ValidationStatus.INVALID)
        assertThat(processDetail.id).isNull()
        assertThat(processDetail.fileProcess).isNull()
        assertThat(processDetail.reason).isEqualTo(ValidationError.INVALID_NUMBER_FORMAT)
        assertThat(processDetail.createdAt).isNotNull()
    }

    @Test
    fun shouldReturnInvalidStatusForTooShortPhoneNumber() {
        val phoneNumber = "2773652927"
        val processDetail = phoneNumberValidator.validate(phoneNumber)

        assertThat(processDetail).isNotNull
        assertThat(processDetail.originalValue).isEqualTo(phoneNumber)
        assertThat(processDetail.processedValue).isNull()
        assertThat(processDetail.status).isEqualTo(ValidationStatus.INVALID)
        assertThat(processDetail.id).isNull()
        assertThat(processDetail.fileProcess).isNull()
        assertThat(processDetail.reason).isEqualTo(ValidationError.INVALID_NUMBER_FORMAT)
        assertThat(processDetail.createdAt).isNotNull()
    }

    @Test
    fun shouldReturnFixedStatusForPhoneNumberWithSuffix() {
        val phoneNumber = "27736529279_SUFFIX_123123"
        val processDetail = phoneNumberValidator.validate(phoneNumber)

        assertThat(processDetail).isNotNull
        assertThat(processDetail.originalValue).isEqualTo(phoneNumber)
        assertThat(processDetail.processedValue).isEqualTo("27736529279")
        assertThat(processDetail.status).isEqualTo(ValidationStatus.FIXED)
        assertThat(processDetail.id).isNull()
        assertThat(processDetail.fileProcess).isNull()
        assertThat(processDetail.reason).isEqualTo(ValidationError.FIXED_REMOVED_SUFFIX)
        assertThat(processDetail.createdAt).isNotNull()
    }
}
