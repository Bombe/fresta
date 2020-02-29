/*
 * fresta - MockKeyService.kt - Copyright © 2020 David ‘Bombe’ Roden
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

val mockKeyService: KeyService = MockKeyService()

fun KeyService.overrideGenerateKey(override: () -> GeneratedKey): KeyService = object : DelegatingKeyService(this) {
	override fun generateKey() = override()
}

private class MockKeyService : KeyService {
	override fun generateKey(): GeneratedKey = GeneratedKey("public", "private")
}

private open class DelegatingKeyService(private val keyService: KeyService) : KeyService {
	override fun generateKey() = keyService.generateKey()
}
