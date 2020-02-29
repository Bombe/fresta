/**
 * fresta - FcpConfigService.kt - Copyright © 2020 David ‘Bombe’ Roden
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
import net.pterodactylus.fcp.highlevel.FcpProtocolException
import net.pterodactylus.fresta.fcp.AccessDenied

class FcpConfigService(private val fcpClient: FcpClient) : ConfigService {

	override val config: Configuration
		get() = try {
			fcpClient.config.entries
					.map { (key, value) -> key.split(".").let { it.drop(1).joinToString(".") to (it.first() to value) } }
					.groupBy(Pair<String, Pair<String, String>>::first, Pair<String, Pair<String, String>>::second)
					.mapValues { it.value.toMap() }
					.mapValues { (_, value) ->
						ConfigurationValue(current = value["current"], default = value["default"], shortDescription = value["shortDescription"],
								longDescription = value["longDescription"], dataType = value["dataType"], expert = value["expertFlag"]?.toBoolean(),
								forceWrite = value["forceWriteFlag"]?.toBoolean(), sortOrder = value["sortOrder"]?.toInt())
					}
		} catch (e: FcpProtocolException) {
			throw when (e.code) {
				24 -> AccessDenied(e)
				else -> e
			}
		}

	override fun setConfig(options: List<Pair<String, String>>) =
			try {
				fcpClient.modifyConfig(options.toMap())
			} catch (e: FcpProtocolException) {
				throw when (e.code) {
					24 -> AccessDenied(e)
					else -> e
				}
			}

}
