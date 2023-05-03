# TodoListApp
<h1 align="center">TodoListApp</h1>

<p align="center">  
 TodoListApp demonstrates Android Widget development with Coroutines, ViewModel, ViewBinding, Koin and Material Design based on MVVM architecture.
</p>
</br>

<table>
  <tr>
    <td>Todo List Page</td>
     <td>Add Todo Page</td>
     <td>Edit Todo Page</td>
  </tr>
  <tr>
    <td><img src="/previews/todolist.png" ></td>
    <td><img src="/previews/addtodoscreen.png" ></td>
    <td><img src="/previews/edittodoscreen.png" ></td> 
  </tr>
  </table>
<table>

  <tr>
      <td>Widget Configuration</td>
     <td>Empty Widget </td>
     <td>Widget With Item</td>
  </tr>
 
  <tr>
    <td><img src="/previews/widgetconfiguration.png" ></td>
    <td><img src="/previews/emptywidget.png" ></td>
    <td><img src="/previews/widgetwithitem.png" ></td>
  </tr>
 </table>
 
## Functionality
The app's functionality includes:
1. Displaying the list of todos in Recycler View which is added by the user. 
2. Add, edit, delete a todo to your app. 
3. Add widget with selected category (Daily, School, Office, Other) show list of todos.
4. Add, edit, delete a todo with widget.

## Tech Stack & Open-source Libraries
- Minimum SDK level 26
- [Kotlin](https://kotlinlang.org/), [Android appcompat](https://developer.android.com/jetpack/androidx/releases/appcompat)
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous operations and Network call .
- [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) for observe Android lifecycles and handle UI states upon the lifecycle changes.
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) for manage UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
- [Android View Binding](https://developer.android.com/topic/libraries/view-binding)
- [Koin] (https://insert-koin.io/) for Kotlin dependency injection.

## Architecture
TodoListApp is based on the clean architecture with MVVM(Model - View - View Model) design pattern.

## Download
Go to the [Releases](https://github.com/EsracanGungor/TodoListApp/releases) to download the APK.
