# Tweeter
a faux Twitter client implementation.

> Test User  
username: khaavren  
password: Password
### This app was created using the [Android Architecture Components][androidArchitecture]
The following Android Architecture Components components were used.
- [Room Persistence Library][room]  
Great library! very flexible easy to use: The right annotations on a few classes and interfaces and voila!

- [LiveData][liveData  ]  
[Lifecycle][lifecycle] aware observable data. Changes are delivered on the main (UI) thread. For the mutable variety changes most be set on the main thread, or posted to the main thread by a conveniently provided method.
When returned from a Room DAO query values will update when changes are made in the database.

- [ViewModel][viewModel]  
Stores and manages UI related data survives configuration changes. Use of this class greatly removes the need to persist data to survive config changes.


### Other Libraries
- [RxJava 2.x][rxJava]  
    TBH I think I could have do without this one. Since I am using [LiveData][liveData]  as the reactive component and didn't really have a need for RxJava in this one. Another thing I use RxJava for is thread management, but I started to switch over to just using executors for threading and RxJava isn't really needed that much anymore.
- [Dagger 2.x][dagger]
    Dependency Injection (DI) might be overkill on a project this small and definitely adds some boilerplate-y overhead. However, large projects start small. Also writing with DI in mind forces me to write Objects with their dependencies usually passed in to the constructor, which later makes it much easier to test things.
- [Retrofit][retrofit]
    If I'm doing networking on Android, I'm probably gonna be using Retrofit. A annotated interfaces and you're off.
- [Retrofit-mock][retrofitMock]  
    Normally this would be a testCompile dependency, not this time. I used it to create a MockwebServer to fake the remote end of Network/API calls.

#### Testing Libraries
- [Junit][junit]
- [Mockito][mockito]

## Organization of code
Mostly the code is organized into various layers.
- **data** This holds all things related to persistence: database, entities,DAO, pojos
- **dagger** DI related things
- **remote** This is where I keep the MockwebServer.
- **api** API/Network things (Retrofit)
-  **ui** This folder holds (you guessed it) UI related things. Sub-packages organized by screen/feature. top folder holds common UI related modules.

## Testing incomplete.
I have some tests in for the DAL and the ViewModel layers, of course I could have more.
I have yet to test the View layer, I would use expresso for that.

[androidArchitecture]: https://developer.android.com/topic/libraries/architecture/index.html
[room]: https://developer.android.com/topic/libraries/architecture/room.html
[liveData]: https://developer.android.com/topic/libraries/architecture/livedata.html
[dagger]: https://google.github.io/dagger
[lifecycle]: https://developer.android.com/topic/libraries/architecture/lifecycle.html
[viewModel]: https://developer.android.com/topic/libraries/architecture/viewmodel.html
[retrofit]: http://square.github.io/retrofit/
[dagger]: https://google.github.io/dagger/
[rxJava]: https://github.com/ReactiveX/RxJava
[junit]: http://junit.org/junit4/
[mockito]: http://site.mockito.org/
[retrofitMock]: https://github.com/square/retrofit/tree/master/retrofit-mock  
