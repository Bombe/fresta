/*
 * fresta - FcpKeyServiceTest.kt - Copyright © 2020 David ‘Bombe’ Roden
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

package net.pterodactylus.fresta.key

import net.pterodactylus.fcp.SSKKeypair
import net.pterodactylus.fcp.highlevel.FcpClient
import net.pterodactylus.fresta.fcp.fcpMessage
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

internal class FcpKeyServiceTest {

	@Test
	fun `generate calls correct method on fcp client`() {
		val keyPair = GeneratedKey("public", "private")
		val fcpClient = object : FcpClient() {
			override fun generateKeyPair(): SSKKeypair {
				return SSKKeypair(fcpMessage("SSKKeypair") {
					it["RequestURI"] = "public"
					it["InsertURI"] = "private"
				})
			}
		}
		val service = FcpKeyService(fcpClient)
		assertThat(service.generateKey(), equalTo(keyPair))
	}

}
