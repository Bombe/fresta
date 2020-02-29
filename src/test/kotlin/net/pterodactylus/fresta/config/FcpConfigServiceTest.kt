/**
 * fresta - FcpConfigServiceTest.kt - Copyright © 2020 David ‘Bombe’ Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.fresta.config

import net.pterodactylus.fcp.highlevel.FcpClient
import net.pterodactylus.fcp.highlevel.FcpException
import net.pterodactylus.fcp.highlevel.FcpProtocolException
import net.pterodactylus.fresta.fcp.AccessDenied
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.hamcrest.collection.IsMapContaining.hasEntry
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


internal class FcpConfigServiceTest {

	@Test
	fun `config object contains current values`() {
		val fcpClient = object : FcpClient() {
			override fun getConfig() = createConfigMap("current", ::getStringValue)
		}
		val configService = FcpConfigService(fcpClient)
		val configuration = configService.config
		configuration.verifyEntries(matchByCurrent, ::getStringValue)
	}

	@Test
	fun `config object contains default values`() {
		val fcpClient = object : FcpClient() {
			override fun getConfig() = createConfigMap("default", ::getStringValue)
		}
		val configService = FcpConfigService(fcpClient)
		val configuration = configService.config
		configuration.verifyEntries(matchByDefault, ::getStringValue)
	}

	@Test
	fun `config object contains shortDescription values`() {
		val fcpClient = object : FcpClient() {
			override fun getConfig() = createConfigMap("shortDescription", ::getStringValue)
		}
		val configService = FcpConfigService(fcpClient)
		val configuration = configService.config
		configuration.verifyEntries(matchByShortDescription, ::getStringValue)
	}

	@Test
	fun `config object contains longDescription values`() {
		val fcpClient = object : FcpClient() {
			override fun getConfig() = createConfigMap("longDescription", ::getStringValue)
		}
		val configService = FcpConfigService(fcpClient)
		val configuration = configService.config
		configuration.verifyEntries(matchByLongDescription, ::getStringValue)
	}

	@Test
	fun `config object contains dataType values`() {
		val fcpClient = object : FcpClient() {
			override fun getConfig() = createConfigMap("dataType", ::getStringValue)
		}
		val configService = FcpConfigService(fcpClient)
		val configuration = configService.config
		configuration.verifyEntries(matchByDataType, ::getStringValue)
	}

	@Test
	fun `config object contains expertFlag values`() {
		val fcpClient = object : FcpClient() {
			override fun getConfig() = createConfigMap("expertFlag", ::getBooleanValue)
		}
		val configService = FcpConfigService(fcpClient)
		val configuration = configService.config
		configuration.verifyEntries(matchByExpertFlag, ::getBooleanValue)
	}

	@Test
	fun `config object contains forceWriteFlag values`() {
		val fcpClient = object : FcpClient() {
			override fun getConfig() = createConfigMap("forceWriteFlag", ::getBooleanValue)
		}
		val configService = FcpConfigService(fcpClient)
		val configuration = configService.config
		configuration.verifyEntries(matchByForceWriteFlag, ::getBooleanValue)
	}

	@Test
	fun `config object contains sortOrder values`() {
		val fcpClient = object : FcpClient() {
			override fun getConfig() = createConfigMap("sortOrder", ::getIntValue)
		}
		val configService = FcpConfigService(fcpClient)
		val configuration = configService.config
		configuration.verifyEntries(matchBySortOrder, ::getIntValue)
	}

	@Test
	fun `protocol error 24 (access denied) results in access denied exception`() {
		val fcpClient = object : FcpClient() {
			override fun getConfig() = throw FcpProtocolException(24, "", "", true)
		}
		val configService = FcpConfigService(fcpClient)
		assertThrows<AccessDenied> {
			configService.config
		}
	}

}

private fun getStringValue(option: String): String = "$option-value"
private fun getIntValue(option: String): Int = option.hashCode()
private fun getBooleanValue(option: String): Boolean = (option.hashCode() and 1) != 0

private fun <T> Configuration.verifyEntries(matcher: (Matcher<T>) -> Matcher<ConfigurationValue>, value: (String) -> T) {
	verifyEntry("option1", value("option1"), matcher)
	verifyEntry("option2", value("option2"), matcher)
	verifyEntry("sub1.option1", value("sub1.option1"), matcher)
	verifyEntry("sub1.option2", value("sub1.option2"), matcher)
	verifyEntry("sub2.option1", value("sub2.option1"), matcher)
}

private fun <T> Configuration.verifyEntry(key: String, value: T, matcher: (Matcher<T>) -> Matcher<ConfigurationValue>) =
		assertThat(this, hasEntry(equalTo(key), matcher(equalTo(value))))

private val matchByCurrent: (Matcher<String>) -> Matcher<ConfigurationValue> = { isConfigurationValue(current = it) }
private val matchByDefault: (Matcher<String>) -> Matcher<ConfigurationValue> = { isConfigurationValue(default = it) }
private val matchByShortDescription: (Matcher<String>) -> Matcher<ConfigurationValue> = { isConfigurationValue(shortDescription = it) }
private val matchByLongDescription: (Matcher<String>) -> Matcher<ConfigurationValue> = { isConfigurationValue(longDescription = it) }
private val matchByDataType: (Matcher<String>) -> Matcher<ConfigurationValue> = { isConfigurationValue(dataType = it) }
private val matchByExpertFlag: (Matcher<Boolean>) -> Matcher<ConfigurationValue> = { isConfigurationValue(expertFlag = it) }
private val matchByForceWriteFlag: (Matcher<Boolean>) -> Matcher<ConfigurationValue> = { isConfigurationValue(forceWriteFlag = it) }
private val matchBySortOrder: (Matcher<Int>) -> Matcher<ConfigurationValue> = { isConfigurationValue(sortOrder = it) }

private fun isConfigurationValue(
		current: Matcher<String> = anything(),
		default: Matcher<String> = anything(),
		shortDescription: Matcher<String> = anything(),
		longDescription: Matcher<String> = anything(),
		dataType: Matcher<String> = anything(),
		expertFlag: Matcher<Boolean> = anything(),
		forceWriteFlag: Matcher<Boolean> = anything(),
		sortOrder: Matcher<Int> = anything()
) : Matcher<ConfigurationValue> = object : TypeSafeDiagnosingMatcher<ConfigurationValue>() {
	override fun matchesSafely(item: ConfigurationValue, mismatchDescription: Description): Boolean {
		if (!current.matches(item.current)) {
			current.describeMismatch(item.current, mismatchDescription)
			return false
		}
		if (!default.matches(item.default)) {
			default.describeMismatch(item.default, mismatchDescription)
			return false
		}
		if (!shortDescription.matches(item.shortDescription)) {
			shortDescription.describeMismatch(item.shortDescription, mismatchDescription)
			return false
		}
		if (!longDescription.matches(item.longDescription)) {
			longDescription.describeMismatch(item.longDescription, mismatchDescription)
			return false
		}
		if (!dataType.matches(item.dataType)) {
			dataType.describeMismatch(item.dataType, mismatchDescription)
			return false
		}
		if (!expertFlag.matches(item.expert)) {
			expertFlag.describeMismatch(item.expert, mismatchDescription)
			return false
		}
		if (!forceWriteFlag.matches(item.forceWrite)) {
			forceWriteFlag.describeMismatch(item.forceWrite, mismatchDescription)
			return false
		}
		if (!sortOrder.matches(item.sortOrder)) {
			sortOrder.describeMismatch(item.sortOrder, mismatchDescription)
			return false
		}
		return true
	}

	override fun describeTo(description: Description) {
		description.appendText("is configuration value with current=")
				.appendValue(current).appendText(" and default=")
				.appendValue(default).appendText(" and shortDescription=")
				.appendValue(shortDescription).appendText(" and longDescription=")
				.appendValue(longDescription).appendText(" and dataType=")
				.appendValue(dataType).appendText(" and expertFlag=")
				.appendValue(expertFlag).appendText(" and forceWriteFlag=")
				.appendValue(forceWriteFlag).appendText(" and sortOrder=")
				.appendValue(sortOrder)
	}
}

@Suppress("UNCHECKED_CAST")
private fun <T> anything() : Matcher<T> = Matchers.anything() as Matcher<T>

private fun createConfigMap(prefix: String, value: (String) -> Any): Map<String, String> {
	return mapOf(
			"option1".let { "${prefix}.$it" to value(it).toString() },
			"option2".let { "${prefix}.$it" to value(it).toString() },
			"sub1.option1".let { "${prefix}.$it" to value(it).toString() },
			"sub1.option2".let { "${prefix}.$it" to value(it).toString() },
			"sub2.option1".let { "${prefix}.$it" to value(it).toString() }
	)
}
