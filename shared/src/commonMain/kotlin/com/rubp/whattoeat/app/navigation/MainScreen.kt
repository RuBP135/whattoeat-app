package com.rubp.whattoeat.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rubp.whattoeat.feature.food.ui.EatScreen
import com.rubp.whattoeat.feature.food.ui.FoodEditScreen
import com.rubp.whattoeat.feature.food.viewmodel.FoodViewModel
import com.rubp.whattoeat.feature.home.HomeScreen
import com.rubp.whattoeat.feature.other.OtherScreen
import com.rubp.whattoeat.feature.settings.ui.SettingsScreen
import com.rubp.whattoeat.feature.settings.viewmodel.SettingsViewModel
import com.rubp.whattoeat.feature.websites.ui.PracticalWebsiteScreen
import kotlinx.serialization.Serializable

@Serializable
object Home
@Serializable
object Settings
@Serializable
object FoodEdit
@Serializable
object Eat
@Serializable
object PracticalWebsite
@Serializable
object Other



@Composable
fun MainScreen(
    appViewModelFactory: ViewModelProvider.Factory,
    settingsViewModel: SettingsViewModel
){
    val navController = rememberNavController()
    val foodViewModel: FoodViewModel = viewModel(factory = appViewModelFactory)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    val mainDestination: MainDestination? = when {
        currentDestination == null -> MainDestination.Home
        currentDestination.hasRoute<Home>() -> MainDestination.Home
        currentDestination.hasRoute<Settings>() -> MainDestination.Settings
        else -> null
    }

    Box{
        Box{
            NavHost(navController, Home){
                composable<Home>{ HomeScreen(
                    onNavigateToEat = { navController.navigate(Eat)}, // Home -> Eat
                    onNavigateToPracticalWebsite = { navController.navigate(PracticalWebsite) }, // Home -> PracticalWebsite
                    onNavigateToOther = { navController.navigate(Other) }
                ) }
                composable<Settings>{ SettingsScreen(settingsViewModel) }
                composable<Eat>{ EatScreen( // Home <- Eat -> FoodEdit
                    foodViewModel = foodViewModel,
                    onNavigateToFoodEdit = { navController.navigate(FoodEdit) },
                    onReturnToHome = { navController.popBackStack() }
                ) }
                composable<FoodEdit>{ FoodEditScreen(
                    foodViewModel = foodViewModel,
                    onReturnToEat = { navController.popBackStack()}
                ) } // Eat <- FoodEdit
                composable<PracticalWebsite>{ PracticalWebsiteScreen { navController.popBackStack() } }
                composable<Other>{ OtherScreen{ navController.popBackStack() } }
            }
        }

        MainBottomBar(
            mainDestination = mainDestination,
            modifier = Modifier.align(Alignment.BottomCenter)
        ){ destination ->

            val route = when(destination){
                MainDestination.Home -> Home
                MainDestination.Settings -> Settings
            }

            navController.navigate(route){
                popUpTo(navController.graph.findStartDestination().id){
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

}
