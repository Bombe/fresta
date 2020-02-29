/**
 * fresta - Fresta.kt - Copyright © 2020 David ‘Bombe’ Roden
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

package net.pterodactylus.fresta

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType.Text
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import net.pterodactylus.fcp.FcpConnection
import net.pterodactylus.fcp.highlevel.FcpClient
import net.pterodactylus.fresta.config.ConfigEndpoint
import net.pterodactylus.fresta.config.FcpConfigService

fun main() {
	fcpClient.connect("fresta")
	embeddedServer(Netty, 7777) {
		install(ContentNegotiation) {
			jackson {
			}
		}
		routing {
			get("/config") {
				call.respond(configEndpoint.getConfig())
			}
			get("/") {
				call.respondText("OK", Text.Plain)
			}
		}
	}.start(wait = true)
}

private const val freenetHost = "localhost"
private const val freenetPort = 9481

private val fcpConnection = FcpConnection(freenetHost, freenetPort)
private val fcpClient = FcpClient(fcpConnection, false)
private val configService = FcpConfigService(fcpClient)
private val configEndpoint = ConfigEndpoint(configService)
