/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.model

/**
 * A fake data repo
 */
object PetRepo {
    fun getPets(): List<Pet> = pets
    fun getPet(petId: Long) = pets.find { it.id == petId }!!
}

/**
 * Static data
 */

val pets = listOf(
    Pet(
        id = 1L,
        name = "Chase",
        gender = MALE,
        age = 9,
        breed = "German Shepherd",
        weight = 20.0,
        height = 50.0,
        imageUrl = "https://source.unsplash.com/u6JyA-aVfPg",
        desc = "a German Shepherd who serves as a police and spy dog, who has a good sense of smell and sight and he also has a cat and feather allergy. ",
        skills = setOf("Track", "Herding sheep")
    ),
    Pet(
        id = 2L,
        name = "Marshall",
        gender = MALE,
        age = 12,
        breed = "Dalmatian",
        weight = 25.0,
        height = 55.1,
        imageUrl = "https://source.unsplash.com/6DWK3rX0J5k",
        desc = "It is a clumsy but competent Dalmatian who serves as a firefighter and paramedic dog. ",
        skills = setOf("Fire control", "First aid", "Funny")
    ),
    Pet(
        id = 3L,
        name = "Rubble",
        gender = MALE,
        age = 10,
        breed = "Bulldog",
        weight = 30.0,
        height = 46.0,
        imageUrl = "https://source.unsplash.com/F1PDaeAyr1A",
        desc = "It is a bulldog who serves as a construction dog. ",
        skills = setOf("Skiing", "Skateboard", "Dig")
    ),
    Pet(
        id = 4L,
        name = "Rocky",
        gender = MALE,
        age = 14,
        breed = "Schnauzer/Scottish Terrier mixed-breed",
        weight = 27.0,
        height = 48.0,
        imageUrl = "https://source.unsplash.com/wNQuCnFjA-U",
        desc = "Rocky is a grey-and-white Schnauzer/Scottish Terrier mixed-breed pup who serves as a recycling and handyman pup. ",
        skills = setOf("Repair", "Waste recycling")
    ),
    Pet(
        id = 5L,
        name = "Zuma",
        gender = MALE,
        age = 11,
        breed = "Labrador retriever",
        weight = 28.0,
        height = 51.0,
        imageUrl = "https://source.unsplash.com/0ARZS-6Q5OA",
        desc = "It is a chocolate Labrador retriever who serves as an aquatic rescue pup. ",
        skills = setOf("Water rescue", "Driving hovercraft", "Driving submarine")
    ),
    Pet(
        id = 6L,
        name = "Skye",
        gender = FEMALE,
        age = 14,
        breed = "Cockapoo",
        weight = 28.0,
        height = 51.0,
        imageUrl = "https://source.unsplash.com/UtrE5DcgEyg",
        desc = "It is a cockapoo who serves as an aviator and a love sight for Chase. ",
        skills = setOf("Flying", "Somersault", "Playing dog dance machine")
    ),
    Pet(
        id = 7L,
        name = "Everest",
        gender = FEMALE,
        age = 12,
        breed = "Siberian Husky",
        weight = 26.0,
        height = 47.0,
        imageUrl = "https://source.unsplash.com/5R_jmZrdMLk",
        desc = "It is a Siberian Husky who serves as a snow rescue pup in emergencies relating to snow or ice and a love sight for Marshall. ",
        skills = setOf("Skiing", "Snow rescue", "Mountain rescue")
    ),
    Pet(
        id = 8L,
        name = "Tracker",
        gender = MALE,
        age = 11,
        breed = "Chihuahua",
        weight = 26.0,
        height = 47.0,
        imageUrl = "https://source.unsplash.com/kjcivvWaD5I",
        desc = "It is a brown-and-white Chihuahua who serves as a jungle rescue pup. ",
        skills = setOf("Brave", "Can speak two languages")
    ),
    Pet(
        id = 9L,
        name = "Tuck",
        gender = MALE,
        age = 9,
        breed = "Golden Retriever",
        weight = 23.0,
        height = 42.0,
        imageUrl = "https://source.unsplash.com/OdzlBdIzgEw",
        desc = "It has super powers to shrink the body, kind-hearted, helpful, and polite-always use" +
            " polite language to others ,live with her twin sister Ella," +
            " and have a personality similar to Ella. There is a tacit understanding.",
        skills = setOf("Skiing", "Shrink body")
    ),
    Pet(
        id = 10L,
        name = "Ella",
        gender = FEMALE,
        age = 9,
        breed = "Golden Retriever",
        weight = 24.0,
        height = 42.2,
        imageUrl = "https://source.unsplash.com/wNRutxmyR8w",
        desc = "It has super powers to grow bigger, kind-hearted, helpful and polite-always use" +
            " polite language to others , lives with the twin brother Tuck, " +
            "and has a personality similar to Tuck Very tacit.",
        skills = setOf("Grow bigger")
    ),
    Pet(
        id = 11L,
        name = "Rex",
        gender = MALE,
        age = 14,
        breed = "Bernese Mountain",
        weight = 29.0,
        height = 56.0,
        imageUrl = "https://source.unsplash.com/mSAS-L7x_Y8",
        desc = "It is a Bernese Mountain Dog who serves as an expert on dinosaurs. ",
        skills = setOf("Dinosaur expert")
    )
)
