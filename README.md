# Github MVVM

Minimal Github repository browser, which the main purpose is experiment MVVM architecture using:

 - Kotlin
 - Lifecycle, ViewModel and LiveData Support Library architecture components
 - Dagger2
 - RxJava
 - Retrofit
 - Roboletric
 - Mockito Kotlin

## Branches

The branch `rxlifecycle` replaces all Architecture Components to use RxLifecycle and pure RxJava components to share data between viewmodels and view.
All tests now are simple unit tests and don't require Roboletric to run.

## Dependencies

To run this project you need to have:

 - JDK 8 for compilation
 - Android Studio 2.3 or higher
 - Kotlin Plugin 1.1.3

## Setup the project

1. Install the dependencies above
2. `$ git clone https://github.com/wakim/github-mvvm.git` - Clone the project
3. `$ cd github-mvvm` - Go into the project folder
4. Open Android Studio
5. Click "Open an existing Android Studio project" and select the github-mvvm folder
6. Build the project
7. Run Unit Tests to verify if everything is working fine

## Tests

Since `LiveData` depends on Main Thread Handler, Roboletric is required to run unit tests.

Tests must run using `./gradlew :app:testDevelopmentReleaseUnitTest` command, because issues with Roboletric and Stetho

License
--------

    Copyright 2017 Wakim, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.