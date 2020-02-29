/**
 * fresta - ConfigService.kt - Copyright © 2020 David ‘Bombe’ Roden
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

interface ConfigService {

	val config: Configuration

	fun setConfig(options: List<Pair<String, String>>)

}

typealias Configuration = Map<String, ConfigurationValue>

data class ConfigurationValue(
		val current: String? = null,
		val default: String? = null,
		val shortDescription: String? = null,
		val longDescription: String? = null,
		val dataType: String? = null,
		val sortOrder: Int? = null,
		val expert: Boolean? = null,
		val forceWrite: Boolean? = null
)
