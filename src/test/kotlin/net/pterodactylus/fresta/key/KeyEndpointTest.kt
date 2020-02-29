/*
 * fresta - KeyEndpointTest.kt - Copyright © 2020 David ‘Bombe’ Roden
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

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.sameInstance
import org.junit.jupiter.api.Test

internal class KeyEndpointTest {

	@Test
	fun `method to generate keys returns object from service`() {
		val generatedKey = GeneratedKey("public", "private")
		val keyService = mockKeyService
				.overrideGenerateKey { generatedKey }
		val keyEndpoint = KeyEndpoint(keyService)
		assertThat(keyEndpoint.generateKey(), sameInstance(generatedKey))
	}

}
