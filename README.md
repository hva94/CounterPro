# Android Development Challenge by hva94 - [linkedin.com/in/hva94](https://www.linkedin.com/in/hva94/)

## CounterPro (Counter Management Android Application)

First of all I want to explain the main development steps of the challenge.

### Choose the architecture pattern:

- MVVM with Clean Architecture

### Set up the project structure with the necessary main libraries (which I will specify later):

- Network
- Data Persistence
- Android Components
- Dependency Injection
- Testing

### At this point, I build the features in a cycle of the next steps:

### Define the data and business model

- Mount the Local Network API and try it with Postman
- Create the data and domain layer (Model, Repository and Use Cases)

### Create the DI modules required

- DatabaseModule
- NetworkModule
- RepositoryModule
- Aux Modules

### Implement the Presentation Layer

- Navigation Component
- IU layouts
- UI components
- UI logic
- Fragment, Adapter, State and ViewModel

### And here are the main development steps based on the required features:

### Project Created and ...

- Libraries (build.gradle, app & project)
- Manifest (Permissions, Hilt App, etc.)
- Folder Structure (Clean Architecture with MVVM)
- Navigation Setup
- Resources (Strings, Dimens, Theme, etc.)
- Icon added
- Splash screen added

![Splash Screen](/assets/screenshots/splash_screen_night.png)
![Splash Screen](/assets/screenshots/splash_screen_day.png)

### Add Named Counter Functionality
- Create all the classes and files needed
- in the three layers of the project
- Add Named Counter feature (Home) added
- API consumption with Retrofit
- Persistence management with Room
- Dependency Injection Setup
- Navigation Component Setup

![Home Screen](/assets/screenshots/home_screen_night.png)
![Home Screen](/assets/screenshots/home_screen_day.png)
![Add Counter](/assets/screenshots/alert_dialog_add_named_counter.png)


### Increment - Decrement - Persist Data Functionality

- Create all the classes and files needed
- Increment, Decrement and Persist Data feature added
- UI logic, ViewModel setup added
- Use Cases, Model, Repository and Utilities added
- Repository implementation, DI and extra files needed added

![Error Empty Title](/assets/screenshots/error_empty_title.png)
![Error Network](/assets/screenshots/error_add_counter.png)

### Single and Batch Deletion - Persist Data Functionality

- Create all the classes and files needed
- Single and Batch Deletion feature added
- UI Logic, Adapter, ViewModel setup added
- Use case, repository and implementation added
- Repository implementation, DI and extra files needed added

![Confirm Delete](/assets/screenshots/alert_dialog_confirm_delete.png)
![Multiple Delete](/assets/screenshots/multiple_selection.png)

### Unit Testing added

- Auxiliary util test classes added
- Unit tests added for the most important data flow
- Repository, Use Cases and ViewModel tests added

### Main Tech Stack explanation

- Kotlin: I chose Kotlin because it is the official language for Android development
  and it is the language I have the most experience with.

- Architecture Components: I chose this library because it is the official library for Android
  development.

- MVVM Design pattern: I chose this pattern because it is the most used in Android development
  and it is the one I have the most experience with.

- Dependency Injection: I chose Dagger Hilt library because it is the recommended one by Google
  and it is the one I have the most experience with.

- Navigation Component: I chose this library because it is the official library for Android
  development and also matches perfectly with Single Activity pattern.

### Dependencies and explanation

* Network (Retrofit, OkHttp, Gson and Moshi)

- I chose Retrofit because it is the most used library for API consumption in Android development.

* Data Persistence (Room)

- I chose Room because it part of the official Android Jetpack libraries.

* Architecture Components (Lifecycle-ViewModel, Navigation Component and Splash Screen)

- I chose this libraries because it part of the Jetpack Compose libraries.

* Dependency Injection (Dagger Hilt)

- I chose this library because it is the recommended one by Google.

* Testing (Mockk, Truth and Coroutines Test)

- Additional to JUnit and related libraries, I use add this libraries for testing purposes because
  their integration, easy to use, mock and verify functionalities for Unit Testing.

### Main features

* Separation of concerns
* Single Activity pattern
* Animations and transitions
* Coroutines usage
* Extension functions
* Exception handling

### Android components

* Material design components
* Lifecycle aware components
* ListAdapter and DiffUtil
* Architecture components
* Rotation support
* DayNight Themes
* Splash Screen
* Recyclerview
* XML based

### Extra features

* Progress indicators
* Splash Screen
* Strings values
* Dimens values