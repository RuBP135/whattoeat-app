package com.rubp.whattoeat.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.filled.Home
import com.composables.icons.materialicons.filled.Settings
import com.composables.icons.materialicons.outlined.Home
import com.composables.icons.materialicons.outlined.Settings
import com.rubp.whattoeat.app.di.AppContainer.appViewModelFactory
import com.rubp.whattoeat.feature.food.ui.EatScreen
import com.rubp.whattoeat.feature.food.ui.FoodEditScreen
import com.rubp.whattoeat.feature.food.viewmodel.FoodViewModel
import com.rubp.whattoeat.feature.home.HomeScreen
import com.rubp.whattoeat.feature.other.OtherScreen
import com.rubp.whattoeat.feature.settings.ui.SettingsScreen
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


//@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val foodViewModel: FoodViewModel = viewModel(factory = appViewModelFactory)

    Scaffold(
        modifier = Modifier,
        bottomBar = { MainScreenNav(navController) }
    ){ paddingValues ->
        Box(
            Modifier.padding(paddingValues)
        ){
            NavHost(navController, Home){
                composable<Home>{ HomeScreen(
                    onNavigateToEat = { navController.navigate(Eat)}, // Home -> Eat
                    onNavigateToPracticalWebsite = { navController.navigate(PracticalWebsite) }, // Home -> PracticalWebsite
                    onNavigateToOther = { navController.navigate(Other) }
                ) }
                composable<Settings>{ SettingsScreen() }
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
    }

}



@Composable
fun MainScreenNav(
    navController: NavController
){
    val navBackStackEntry by navController.currentBackStackEntryAsState() // 获取State<NavBackStateEntry>使得能在改变的时候被监听
    val currentDestination = navBackStackEntry?.destination

    val selectedHome = currentDestination?.hasRoute<Home>() == true
    val selectedSettings = currentDestination?.hasRoute<Settings>() == true
    val isVisible = selectedHome || selectedSettings

    if(isVisible){ // 用于展示或者隐藏导航栏
        data class Item(
            val label: String,
            val selected: Boolean,
            val filledVector: ImageVector,
            val outlinedVector: ImageVector,
            val route: Any
        )

        val itemList = listOf(
            Item("首页", selectedHome, MaterialIcons.Filled.Home, MaterialIcons.Outlined.Home, Home),
            Item("设置", selectedSettings, MaterialIcons.Filled.Settings, MaterialIcons.Outlined.Settings, Settings)
        )

        NavigationBar {
            for (item in itemList){
                NavigationBarItem(
                    selected = item.selected,
                    onClick = {
                        navController.navigate(item.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = { Text(
                        item.label,
                        color = if(item.selected) MaterialTheme.colorScheme.primary else Color.Unspecified
                    ) },
                    icon = {
                        Icon(
                            imageVector = if(item.selected) item.filledVector else item.outlinedVector,
                            contentDescription = null,
                            tint = if(item.selected) MaterialTheme.colorScheme.primary else LocalContentColor.current
                        )
                    }
                )
            }

        }
    }

}




