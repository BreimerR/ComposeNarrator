# Compose Narrator

Create your stories to fit your needs and allows you to pass viewModels that are independent of the narration but will
be canceled once they are no longer relevant( After 5seconds 😄 ) stories.

# Coroutines

###### Desktop

Read [Missing Dispatchers.Main Exception](https://github.com/JetBrains/compose-jb/releases/tag/v1.1.1)

Known Issues

1. Narrations are canceled even if story is resumed [BUG].
2. Unable to pass information from one story to another [FEATURE].

## Features

1. [x] Navigate between composables
   ```kotlin
      enum class HarryPotter{
          Hogwarts,
          DeathToAllMuggle
      }
       
      @Composable
      fun OrderOfThePhoenix(){
         Narration{
             HarryPotter.Hogwarts{
                 AtHarryPotter()
             }
             HarryPotter.DeathToAllMuggle({JKsMind()})
         }
      }
       
      @Composable
      fun AtHarryPotter(){
         Text("Probably playing Quidditch!! Ron Sucked!! Please redo ending Ron can't have her.")
      }
       
      @Composable
      fun DeathToMuggles(){
           Text("Well figuratively")
      }
       
      class JKsMind : ViewModel{
          override fun onCreate(){
              searchMuggles()
         }
          
         fun searchMuggles(){
             coroutineScope.launch(Dispatchers.IO){
                 Log.d(TAG,"Point and give condisending remarks!! i.e. Believe in magic Muggle!!")
             }
         }
         
         override fun onDestroy(){
            Log.d(TAG,"Damn Binge Again PLEASE!!")
         }
      }
   ```
2. [x] Coroutine support
3. [x] Multiplatform support
    1. [x] ```android```
    2. [x] ```desktop-jvm```
    3. [ ] ```desktop-native``` Waiting for compose native support eagerly
    4. [ ] ```web``` Planning Time but should be trivial 🤔

## Publishing
Not done hehe been lazy to create a publishing repo but will do soon.

1. Build and use mavenLocal()
    1. Clone project
    2. Build Project
       > 1. ```./gradlew publishToMavenLocal```  
       OR
       > 2. ```Using ide task```

