package olx.phonevalidator.service

import org.springframework.stereotype.Service
import olx.phonevalidator.domain.detail.ProcessDetail
import olx.phonevalidator.domain.detail.ValidationError
import olx.phonevalidator.domain.detail.ValidationStatus

interface PhoneNumberValidator {
    fun validate(number: String): ProcessDetail
}

@Service
class SouthAfricaMobilePhoneValidator : PhoneNumberValidator {

    companion object {
        /**
         * Assuming valid SA mobile phone number always contains "27" Country prefix.
         */
        val regex = "^27((60[3-9]|64[0-5]|66[0-5])\\d{6}|(7[1-4689]|6[1-3]|8[1-4])\\d{7})(_\\w*)?$".toRegex()
    }

    override fun validate(number: String): ProcessDetail {

        return when (number.isNullOrBlank()) {
            true -> {
                ProcessDetail(
                    originalValue = number,
                    processedValue = null,
                    status = ValidationStatus.INVALID,
                    reason = ValidationError.EMPTY_NUMBER_VALUE
                )
            }
            false -> {
                val matchResult = regex.find(number)

                when (matchResult) {
                    null -> {
                        ProcessDetail(
                            originalValue = number,
                            processedValue = null,
                            status = ValidationStatus.INVALID,
                            reason = ValidationError.INVALID_NUMBER_FORMAT
                        )
                    }
                    else -> {
                        processValidFormat(matchResult)
                    }
                }
            }
        }
    }

    internal fun processValidFormat(matchResult: MatchResult): ProcessDetail {

        val result = ProcessDetail(
            originalValue = matchResult.value,
            processedValue = "27" + matchResult.groupValues[1]
        )

        // Assuming extra suffix after '_' character in last regex matching group
        return when (matchResult.groupValues.last().startsWith("_")) {
            true -> {
                result.copy(
                    status = ValidationStatus.FIXED,
                    reason = ValidationError.FIXED_REMOVED_SUFFIX
                )
            }
            false -> {
                result
            }
        }
    }
}