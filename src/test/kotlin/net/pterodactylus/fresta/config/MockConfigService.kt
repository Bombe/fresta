/*
 * fresta - MockConfigService.kt - Copyright © 2020 David ‘Bombe’ Roden
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

val mockConfigService: ConfigService = MockConfigService()

fun ConfigService.overrideConfig(override: () -> Configuration): ConfigService = object : DelegatingConfigService(this) {
	override val config: Configuration get() = override()
}

fun ConfigService.overrideSetConfig(override: (options: List<Pair<String, String>>) -> Unit): ConfigService = object : DelegatingConfigService(this) {
	override fun setConfig(options: List<Pair<String, String>>) = override(options)
}

private class MockConfigService : ConfigService {

	override val config: Configuration =
			emptyMap()

	override fun setConfig(options: List<Pair<String, String>>) = Unit

}

private open class DelegatingConfigService(private val configService: ConfigService) : ConfigService {

	override val config: Configuration
		get() = configService.config

	override fun setConfig(options: List<Pair<String, String>>) =
			configService.setConfig(options)

}
