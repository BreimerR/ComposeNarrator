# Compose Narrator

Create your stories to fit your needs and allows you to pass viewModels that are independent of the narration but will
be canceled once they are no longer relevant( After 5seconds 😄 ) stories.

# Coroutines

###### Desktop

Read [Missing Dispatchers.Main Exception](https://github.com/JetBrains/compose-jb/releases/tag/v1.1.1)

Known Issues
1. Unable to pass information from one story to another [FEATURE].

## Requirements
There is need to check if there is a requested link/narration.
If so then render the required screen. Else we'd have to register all the narrations in the 
UI before we are able to navigate to a page. The assumption here will be that the item being navigated to 
can be reached one way or the other.

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
                 AtHogwarts()
             }
             HarryPotter.DeathToAllMuggle({JKsMind()}){
                DeathToMuggles(lifeCycleViewModel())
             }
         }
      }
       
      @Composable
      fun AtHogwarts(){
         Text("Probably playing Quidditch!! Ron Sucked!! Please redo ending Ron can't have her.")
      }
       
      @Composable
      fun DeathToMuggles(viewModel:JKsMind){
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

<a href="https://www.paypal.com/donate/?hosted_button_id=CUHRL6CUYWRTA" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important;box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" ></a>
