# Bersih.In Mobile App
>Implementation of the Bersih.In mobile application, written in Kotlin and Jetpack Compose, for Bangkit Batch 2's Product-Based Capstone Project


## Description
Bersih.In is an application that allows the community to report unhandled garbage piles that are disturbing and could potentially cause environmental hazard. The app aims to reduce the number of unhandled garbage piles by allowing users to report spotted garbage piles that might be otherwise overlooked. The environmental workers can check the verified reports and then work on the reported garbage pile. 

## Libraries
1. **Jetpack Compose**: Main framework for Android development, allowing declarative UI and faster development times
2. **Retrofit**: Used to adapt the API endpoints into usable HTTP interfaces in the application
3. **Coil**: Used to load image asynchronously
4. **Firebase Cloud Storage**: A cloud storage used to upload trash images, integrated with the backend server
5. **Google Maps API**: Used to allow users to see the location of reported spotted garbage piles via a map view
6.  **Accompanist**: A unified tools library providing swipe-refresh mechanism, horizontal paging, and permission requests.

## How to Run
### Pre-requisites
1. Android Studio to run the app locally, **preferably version Giraffe 2022.3.1**
2. A working Java and JVM installation on your computer
3. A configured Android device that can be connected to Android Studio (physical / AVD emulator)

### Steps
1. Clone or download this repository to your local environment
2. Open the extracted repository with your IDE, **preferably Android Studio Giraffe 2022.3.1**
3. Configure the `JAVA_HOME` path and install Gradle dependencies
4. Run the application on your e